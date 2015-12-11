package com.dc.storage.console.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dc.storage.console.bean.DataFile;
import com.dc.storage.console.dao.IStorageDao;

@Component("storageDao")
public class StorageDao extends SqlSessionDaoSupport implements IStorageDao {
	@Autowired
	public StorageDao(
			@Qualifier("sqlSession") SqlSessionTemplate sqlSessionTemplate) {
		super();
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public long save(String fileName, long length, long userId, Long parentId,
			String description) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileName", fileName);
		params.put("length", length);
		params.put("userId", userId);
		params.put("parentId", parentId);
		params.put("description", description);
		params.put("originalName", fileName);
		getSqlSession().insert("storage.save", params);
		return (Long) params.get("id");
	}

	@Override
	public void saveFileNode(long fileId, long id) {
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("fileId", fileId);
		params.put("nodeId", id);
		getSqlSession().insert("storage.saveFileNode", params);
	}

	@Override
	public int updateReplCount(long fileId, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", fileId);
		params.put("replCount", size);
		return getSqlSession().update("storage.updateReplCount", params);
	}

	@Override
	public List<String> getConsoleUrls() {
		return getSqlSession().selectList("storage.getConsoleUrls");
	}

	@Override
	public List<DataFile> list(long parentId, long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		params.put("userId", userId);
		return getSqlSession().selectList("storage.list", params);
	}

	@Override
	public long getPathId(String fileName, long userId, Long parentId,
			boolean isDir) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileName", fileName);
		params.put("userId", userId);
		params.put("parentId", parentId);
		params.put("isDir", isDir);
		return (Long) getSqlSession().selectOne("storage.getPathId", params);
	}

	@Override
	public int saveDir(Map<String, Object> params) {
		return getSqlSession().insert("storage.saveDir", params);
	}

	@Override
	public long getUserRootId(long id) {
		return (Long) getSqlSession().selectOne("storage.getUserRootId", id);
	}

	@Override
	public boolean delete(String id) {
		return getSqlSession().delete("storage.delete", id) > 0;
	}

	@Override
	public Map<String, Object> getFileInfo(String id) {
		return getSqlSession().selectOne("storage.getFileInfo", id);
	}

	@Override
	public int deleteFileNode(String id, String nodeId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileId", id);
		params.put("nodeId", nodeId);
		return getSqlSession().delete("storage.deleteFileNode", params);
	}

	@Override
	public List<String> getFileNodes(String id) {
		return getSqlSession().selectList("storage.getFileNodes", id);
	}

	@Override
	public String getAbsPath(String id) {
		List<String> dirs = new ArrayList<String>();
		String pid = id;
		while (!pid.equals("-1")) {
			Map<String, Object> info = getFileInfo(pid);
			pid = info.get("parent_id").toString();
			dirs.add((String) info.get("file_name"));
		}
		StringBuilder sb = new StringBuilder();
		for (int i = dirs.size() - 1; i >= 0; i--) {
			sb.append(dirs.get(i));
			if (!"/".equals(dirs.get(i)) && i > 0) {
				sb.append("/");
			}
		}
		return sb.toString();
	}

}
