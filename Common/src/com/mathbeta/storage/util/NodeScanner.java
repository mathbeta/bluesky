package com.mathbeta.storage.util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mathbeta.storage.console.bean.Node;
import com.mathbeta.storage.console.service.INodeService;
import com.mathbeta.storage.console.service.IStorageService;

public class NodeScanner {
	private static Logger log = LoggerFactory.getLogger(NodeScanner.class);

	private static INodeService nodeService = (INodeService) BeanContext
			.getInstance().getBean("nodeService");
	private static IStorageService storageService = (IStorageService) BeanContext
			.getInstance().getBean("storageService");

	public static void scan() {
		List<Node> nodes = nodeService.findAllNodes();
		if (nodes != null && !nodes.isEmpty()) {
			for (Node node : nodes) {
				// node未连接，需要重连
				if (!node.isStatus()) {
					ExecutorServicePool.getInstance()
							.submit(new ScanTask(node));
				} else {
					// node正常，需要确认node仍活着
					ExecutorServicePool.getInstance().submit(new AckTask(node));
				}
			}
		}
	}

	private static class AckTask implements Runnable {
		private Node node;

		public AckTask(Node node) {
			this.node = node;
		}

		public void run() {
			String ip = node.getIp();
			int port = Integer.parseInt(ConfigHelper
					.getValue(StorageProperties.NODE_COMMAND_PORT));
			Socket socket = null;
			try {
				socket = new Socket(ip, port);
				ConnectorMaps.getInstance().putCommandConnector(ip, socket);

				Message message = new Message();
				message.setOp(Op.ACK);
				TransferUtil.writeMessage(socket, message);

				Message ret = TransferUtil
						.readObject(TransferUtil.read(socket));
				if (ret.getOp() == Op.ACK) {
					Map<String, Object> retContent = ret.getContent();
					if ((Boolean) retContent.get("result")) {
						Integer[] transferPorts = (Integer[]) retContent
								.get(StorageProperties.NODE_TRANSFER_PORTS);
						node.setTransferPorts(StringUtils.join(transferPorts,
								','));
						nodeService.updateTransferPorts(node);
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (ConnectException e) {
				node.setStatus(false);
				nodeService.updateStatus(node);

				log.error("node [" + node.getName() + ", " + node.getIp()
						+ "] can not be connected, will reconnect later.");
				// node can not be connected, should reconnect later
				long period = Long.parseLong(ConfigHelper
						.getValue(StorageProperties.NODE_SCAN_PERIOD));
				try {
					Thread.sleep(period);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				node = nodeService.findById(node.getId());
				if (!node.isStatus()) {
					ExecutorServicePool.getInstance()
							.submit(new ScanTask(node));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(socket);
			}
		}
	}

	private static class ScanTask implements Runnable {
		private Node node;

		public ScanTask(Node node) {
			this.node = node;
		}

		@Override
		public void run() {
			String ip = node.getIp();
			String storageRoot = node.getStorageRoot();

			int port = Integer.parseInt(ConfigHelper
					.getValue(StorageProperties.NODE_COMMAND_PORT));
			Socket socket = null;
			try {
				socket = new Socket(ip, port);
				ConnectorMaps.getInstance().putCommandConnector(ip, socket);
				Map<String, Object> content = new HashMap<String, Object>();
				// node params
				content
						.put(
								StorageProperties.NODE_TRANSFER_PORT_COUNT,
								Integer
										.parseInt(ConfigHelper
												.getValue(StorageProperties.NODE_TRANSFER_PORT_COUNT)));
				content
						.put(
								StorageProperties.NODE_TRANSFER_PORT_RANGE,
								ConfigHelper
										.getValue(StorageProperties.NODE_TRANSFER_PORT_RANGE));
				content.put(StorageProperties.NODE_STORAGE_DIR, storageRoot);
				List<String> consoleUrls = storageService.getConsoleUrls();
				content.put(StorageProperties.CONSOLE_URLS, consoleUrls);

				Message message = new Message();
				message.setOp(Op.INIT);
				message.setContent(content);
				TransferUtil.writeMessage(socket, message);

				Message ret = TransferUtil
						.readObject(TransferUtil.read(socket));
				if (ret.getOp() == Op.INIT) {
					Map<String, Object> retContent = ret.getContent();
					if ((Boolean) retContent.get("result")) {
						Integer[] transferPorts = (Integer[]) retContent
								.get(StorageProperties.NODE_TRANSFER_PORTS);
						node.setTransferPorts(StringUtils.join(transferPorts,
								','));
						nodeService.updateTransferPorts(node);
					}
				}
				node.setStatus(true);
				nodeService.updateStatus(node);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (ConnectException e) {
				log.error("node [" + node.getName() + ", " + node.getIp()
						+ "] can not be connected, will reconnect later.");
				// node can not be connected, should reconnect later
				long period = Long.parseLong(ConfigHelper
						.getValue(StorageProperties.NODE_SCAN_PERIOD));
				try {
					Thread.sleep(period);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				node = nodeService.findById(node.getId());
				if (!node.isStatus()) {
					ExecutorServicePool.getInstance()
							.submit(new ScanTask(node));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(socket);
			}

		}

	}

}
