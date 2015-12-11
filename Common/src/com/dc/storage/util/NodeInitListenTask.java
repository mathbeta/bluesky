package com.dc.storage.util;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.storage.console.bean.Node;
import com.dc.storage.console.service.INodeService;

public class NodeInitListenTask extends Thread {
	private static Logger log = LoggerFactory
			.getLogger(NodeInitListenTask.class);

	private Socket socket;

	public NodeInitListenTask(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			Message message = TransferUtil
					.readObject(TransferUtil.read(socket));
			if (message == null) {
				return;
			}
			if (Op.INIT == message.getOp()) {
				Map<String, Object> content = message.getContent();
				if ((Boolean) content.get("result")) {
					long nodeId = (Long) content.get(StorageProperties.NODE_ID);
					Integer[] transferPorts = (Integer[]) content
							.get(StorageProperties.NODE_TRANSFER_PORTS);
					Node node = new Node();
					node.setId(nodeId);
					node.setTransferPorts(StringUtils.join(transferPorts, ','));
					INodeService nodeService = (INodeService) BeanContext
							.getInstance().getBean("nodeService");
					nodeService.updateTransferPorts(node);
					node.setStatus(true);
					nodeService.updateStatus(node);

					log
							.info("node init, update transfer ports successfully! node id: "
									+ nodeId);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(socket);
		}
	}
}
