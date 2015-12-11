package com.dc.storage.console.dao;

import java.util.List;
import java.util.Map;

import com.dc.storage.console.bean.DataFile;

public interface IStorageDao {

	long save(String fileName, long length, long userId, Long parentId, String description);

	int updateReplCount(long fileId, int size);

	void saveFileNode(long fileId, long id);

	List<String> getConsoleUrls();

	List<DataFile> list(long parentId, long userId);

	long getPathId(String fileName, long userId, Long parentId, boolean isDir);

	int saveDir(Map<String, Object> params);

	long getUserRootId(long id);

	Map<String, Object> getFileInfo(String id);

	boolean delete(String id);

	List<String> getFileNodes(String id);

	int deleteFileNode(String id, String nodeId);

	String getAbsPath(String id);

}
