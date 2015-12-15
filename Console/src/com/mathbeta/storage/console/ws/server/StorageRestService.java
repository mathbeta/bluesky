package com.mathbeta.storage.console.ws.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mathbeta.storage.console.bean.DataFile;
import com.mathbeta.storage.console.bean.User;
import com.mathbeta.storage.console.service.IStorageService;
import com.mathbeta.storage.util.BeanContext;
import com.mathbeta.storage.util.MessageHelper;

@Path("storage")
public class StorageRestService {
	private static Logger log = LoggerFactory
			.getLogger(StorageRestService.class);
	private IStorageService storageService = (IStorageService) BeanContext
			.getInstance().getBean("storageService");

	@GET
	@Path("getUserRootId")
	public String getUserRootId(@Context HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("receive get user root request...");
		}
		User user = (User) request.getSession().getAttribute("user");
		return String.valueOf(storageService.getUserRootId(user.getId()));
	}

	@GET
	@Path("list")
	public String list(@QueryParam("path") @DefaultValue("/") String path,
			@Context HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("receive list path request: path=" + path);
		}
		User user = (User) request.getSession().getAttribute("user");
		List<DataFile> list = storageService.list(path, user.getId());
		return JSON.toJSONString(list);
	}

	@GET
	@Path("list/{parentId}")
	public String listDir(@PathParam("parentId") long parentId,
			@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		List<DataFile> list = storageService.listDir(parentId, user.getId());
		return JSON.toJSONString(list);
	}

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void upload(@FormDataParam("file") InputStream is,
			@FormDataParam("file") FormDataContentDisposition disposition,
			@FormDataParam("description") String description,
			@FormDataParam("path") @DefaultValue("/") String path,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		String fileName = disposition.getFileName();
		if (log.isDebugEnabled()) {
			log.debug("receive upload file request: file name=" + fileName);
		}
		User user = (User) request.getSession().getAttribute("user");
		String json = storageService.saveUpload(is, disposition, description,
				path, user);
		Map<String, Object> map = MessageHelper.extract(json);
		request.setAttribute("result", map);
		forward("/pages/my.jsp", request, response);
	}

	private void forward(String path, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("download/{id}")
	public Response download(@PathParam("id") String id,
			@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return storageService.download(id, user);
	}

	@GET
	@Path("download")
	public Response downloadByPath(@QueryParam("path") String path,
			@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return storageService.downloadByPath(path, user);
	}

	@POST
	@Path("add-dir")
	public String addDir(@FormParam("path") String path,
			@FormParam("curPath") String curPath,
			@FormParam("parentId") long parentId,
			@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return storageService.saveDir(path, curPath, parentId, user);
	}

	@POST
	@Path("delete")
	public String delete(@FormParam("ids") String ids) {
		return storageService.delete(ids);
	}
}
