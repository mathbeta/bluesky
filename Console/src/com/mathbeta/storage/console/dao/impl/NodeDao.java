package com.mathbeta.storage.console.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mathbeta.storage.console.bean.Node;
import com.mathbeta.storage.console.dao.INodeDao;

@Component("nodeDao")
public class NodeDao extends SqlSessionDaoSupport implements INodeDao {
	@Autowired
	public NodeDao(
			@Qualifier("sqlSession") SqlSessionTemplate sqlSessionTemplate) {
		super();
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public int save(Node node) {
		return getSqlSession().insert("node.save", node);
	}

	@Override
	public List<Node> findAll() {
		return getSqlSession().selectList("node.findAll");
	}

	@Override
	public int delete(String id) {
		return getSqlSession().delete("node.delete", id);
	}

	@Override
	public void updateTransferPorts(Node node) {
		getSqlSession().insert("node.updateTransferPorts", node);
	}

	@Override
	public Node findById(Object id) {
		return getSqlSession().selectOne("node.findById", id);
	}

	@Override
	public List<Node> findAllRunning() {
		return getSqlSession().selectList("node.findAllRunning");
	}

	@Override
	public int updateStatus(Node node) {
		return getSqlSession().update("node.updateStatus", node);
	}

	@Override
	public List<Node> findFileNodes(long fileId) {
		return getSqlSession().selectList("node.findFileNodes", fileId);
	}

}
