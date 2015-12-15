package com.mathbeta.storage.console.dao;

import java.util.List;

import com.mathbeta.storage.console.bean.Node;

public interface INodeDao {

	int save(Node node);

	List<Node> findAll();

	int delete(String id);

	void updateTransferPorts(Node node);

	Node findById(Object id);

	List<Node> findAllRunning();

	int updateStatus(Node node);

	List<Node> findFileNodes(long fileId);

}
