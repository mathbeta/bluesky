package com.mathbeta.storage.node.init;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mathbeta.storage.transfer.AbstractTransfer;
import com.mathbeta.storage.transfer.TransferFactory;
import com.mathbeta.storage.util.ConfigHelper;
import com.mathbeta.storage.util.DataTransferTask;
import com.mathbeta.storage.util.ExecutorServicePool;
import com.mathbeta.storage.util.IOUtils;
import com.mathbeta.storage.util.Message;
import com.mathbeta.storage.util.Op;
import com.mathbeta.storage.util.PortScanner;
import com.mathbeta.storage.util.StorageProperties;
import com.mathbeta.storage.util.TransferUtil;

public class NodeInit {
	private static Logger log = LoggerFactory.getLogger(NodeInit.class);
	private static Integer[] transferPorts = null;
	private static ServerSocket[] transfers = null;

	/**
	 * 启动命令端口，接收console发送的命令或向console发送命令。
	 */
	public static void start() {
		ExecutorServicePool.getInstance().submit(new CommandChannelStartTask());
		if (!"false".equals(ConfigHelper.getValue(StorageProperties.NODE_INIT))) {
			ExecutorServicePool.getInstance().submit(
					new TransferChannelStartTask());
		}
	}

	/**
	 * 启动命令传输通道
	 * 
	 * @author xuxy
	 * 
	 */
	private static class CommandChannelStartTask extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			int port = Integer.parseInt(ConfigHelper
					.getValue(StorageProperties.NODE_COMMAND_PORT));
			if (log.isDebugEnabled()) {
				log.debug("start command port: " + port);
			}
			ServerSocket server = null;
			Socket socket = null;
			try {
				server = new ServerSocket(port);
				// 接受一个console连接
				socket = server.accept();
				byte[] buf = TransferUtil.read(socket);
				while (buf != null) {
					Message message = TransferUtil.readObject(buf);
					if (log.isDebugEnabled()) {
						log.debug("received message: " + message);
					}
					boolean nodeInit = !"false".equals(ConfigHelper
							.getValue(StorageProperties.NODE_INIT));
					if (message.getOp() == Op.INIT) {
						Map<String, Object> params = message.getContent();
						int portCount = (Integer) params
								.get(StorageProperties.NODE_TRANSFER_PORT_COUNT);
						ConfigHelper.setValue(
								StorageProperties.NODE_TRANSFER_PORT_COUNT,
								String.valueOf(portCount));
						long id = (Long) params.get(StorageProperties.NODE_ID);
						ConfigHelper.setValue(StorageProperties.NODE_ID, String
								.valueOf(id));
						String storageRoot = (String) params
								.get(StorageProperties.NODE_STORAGE_DIR);
						ConfigHelper
								.setValue(StorageProperties.NODE_STORAGE_DIR,
										storageRoot);
						List<String> urls = (List<String>) params
								.get(StorageProperties.CONSOLE_URLS);
						ConfigHelper.setValue(StorageProperties.CONSOLE_URLS,
								StringUtils.join(urls.iterator(), ','));
						ConfigHelper.setValue(StorageProperties.NODE_INIT,
								"true");
						ConfigHelper.write();

						if (!nodeInit) {
							ExecutorServicePool.getInstance().submit(
									new TransferChannelStartTask());
						}
					} else if (message.getOp() == Op.DELETE) {
						String filePath = (String) message.getContent().get(
								StorageProperties.FILE_PATH);
						File f = new File(ConfigHelper
								.getValue(StorageProperties.NODE_STORAGE_DIR)
								+ filePath);
						boolean b = f.delete();

						Message ret = new Message();
						ret.setOp(Op.DELETE);
						Map<String, Object> retContent = Message.buildContent(
								StorageProperties.FILE_DELETE_RESULT, b);
						ret.setContent(retContent);
						TransferUtil.writeMessage(socket, ret);
					}

					buf = TransferUtil.read(socket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(socket);
				IOUtils.closeQuietly(server);
			}
		}
	}

	/**
	 * 开启数据传输通道
	 * 
	 * @author xuxy
	 * 
	 */
	private static class TransferChannelStartTask extends Thread {
		public void run() {
			int portCount = Integer.parseInt(ConfigHelper
					.getValue(StorageProperties.NODE_TRANSFER_PORT_COUNT));
			transferPorts = new Integer[portCount];
			for (int j = 0; j < portCount; j++) {
				transferPorts[j] = -1;
			}
			String[] ranges = ConfigHelper.getValue(
					StorageProperties.NODE_TRANSFER_PORT_RANGE).split("\\-");
			int start = Integer.parseInt(ranges[0]);
			int end = Integer.parseInt(ranges[1]);
			int count = 0;
			for (int j = start; j <= end; j++) {
				if (count < portCount) {
					if (PortScanner.scan(j)) {
						transferPorts[count] = j;
						count++;
					}
				} else {
					break;
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("node port scan end, available ports are: "
						+ StringUtils.join(transferPorts, ',')
						+ ", port count needed: " + portCount);
			}

			ConfigHelper.setValue(StorageProperties.NODE_TRANSFER_PORTS,
					StringUtils.join(transferPorts, ','));
			ConfigHelper.write();

			// send back init result
			Message ret = new Message();
			ret.setOp(Op.INIT);
			Map<String, Object> content = Message.buildContent("result", true,
					StorageProperties.NODE_TRANSFER_PORTS, transferPorts,
					StorageProperties.NODE_ID, Long.parseLong(ConfigHelper
							.getValue(StorageProperties.NODE_ID)));
			ret.setContent(content);
			String[] urls = ConfigHelper.getValue(
					StorageProperties.CONSOLE_URLS).split(",");
			boolean success = false;
			int index = 0;
			while (!success) {
				AbstractTransfer transfer = TransferFactory
						.getTransfer(urls[index]);
				success = transfer.transfer(ret);
				index++;
				if (index == urls.length) {
					index = 0;
				}
			}
			log.info("node restart, notify console successfully!");

			// start transfer server socket
			transfers = new ServerSocket[count];
			count = 0;
			for (int j = 0; j < portCount; j++) {
				if (transferPorts[j] != -1) {
					try {
						transfers[count] = new ServerSocket(transferPorts[j]);
					} catch (IOException e) {
						e.printStackTrace();
					}
					startTransfer(transfers[count]);
					if (log.isDebugEnabled()) {
						log.debug("start transfer port: " + transferPorts[j]);
					}
				}
			}
		}
	}

	/**
	 * 启动数据传输端口
	 * 
	 * @param server
	 */
	private static void startTransfer(final ServerSocket server) {
		ExecutorServicePool.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = server.accept();
						ExecutorServicePool.getInstance().submit(
								new DataTransferTask(socket));
					} catch (SocketException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
