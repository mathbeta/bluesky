package com.mathbeta.storage.console.service.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mathbeta.storage.console.bean.Node;
import com.mathbeta.storage.console.dao.INodeDao;
import com.mathbeta.storage.console.service.INodeService;
import com.mathbeta.storage.console.service.IStorageService;
import com.mathbeta.storage.util.ConfigHelper;
import com.mathbeta.storage.util.ConnectorMaps;
import com.mathbeta.storage.util.Message;
import com.mathbeta.storage.util.MessageHelper;
import com.mathbeta.storage.util.Op;
import com.mathbeta.storage.util.StatusCode;
import com.mathbeta.storage.util.StorageProperties;
import com.mathbeta.storage.util.TransferUtil;

@Service("nodeService")
public class NodeService implements INodeService {
	private static Logger log = LoggerFactory.getLogger(NodeService.class);

	@Autowired
	@Qualifier("nodeDao")
	private INodeDao nodeDao;
	@Autowired
	@Qualifier("storageService")
	private IStorageService storageService;

	@Override
	public List<Node> findAllNodes() {
		List<Node> nodes = nodeDao.findAll();
		return nodes;
	}

	@Override
	public void save(String name, String ip, boolean isolated,
			String storageRoot) {
		Node node = new Node();
		node.setName(name);
		node.setIp(ip);
		node.setIsolated(isolated);
		node.setStatus(true);
		node.setStorageRoot(storageRoot);
		nodeDao.save(node);

		initNode(node.getId(), ip, storageRoot);
	}

	private void initNode(long nodeId, String ip, String storageRoot) {
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
			content.put(StorageProperties.NODE_ID, nodeId);

			Message message = new Message();
			message.setOp(Op.INIT);
			message.setContent(content);
			TransferUtil.writeMessage(socket, message);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String delete(String ids) {
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			nodeDao.delete(id);
		}
		return MessageHelper.wrap(true, StatusCode.OK, "");
	}

	@Override
	public void updateTransferPorts(Node node) {
		nodeDao.updateTransferPorts(node);
	}

	@Override
	public Node findById(Object id) {
		return nodeDao.findById(id);
	}

	@Override
	public int updateStatus(Node node) {
		return nodeDao.updateStatus(node);
	}

}
