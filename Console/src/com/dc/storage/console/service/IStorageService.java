package com.dc.storage.console.service;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.dc.storage.console.bean.DataFile;
import com.dc.storage.console.bean.User;

public interface IStorageService {

	String saveUpload(InputStream is, FormDataContentDisposition disposition,
			String description, String path, User user);

	List<String> getConsoleUrls();

	Response download(String id, User user);

	Response downloadByPath(String path, User user);

	List<DataFile> list(String path, long userId);

	List<DataFile> listDir(long parentId, long userId);

	String saveDir(String path, String curPath, long parentId, User user);

	long getUserRootId(long id);

	String delete(String ids);

}
