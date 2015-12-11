package com.dc.storage.console.service;

import java.util.List;

import com.dc.storage.console.bean.Node;

public interface INodeService {

	List<Node> findAllNodes();

	void save(String name, String ip, boolean isolated, String storageRoot);

	String delete(String ids);

	void updateTransferPorts(Node node);

	int updateStatus(Node node);

	Node findById(Object id);

}
