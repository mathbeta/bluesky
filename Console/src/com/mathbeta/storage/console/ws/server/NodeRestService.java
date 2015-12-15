package com.mathbeta.storage.console.ws.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mathbeta.storage.console.bean.Node;
import com.mathbeta.storage.console.service.INodeService;
import com.mathbeta.storage.util.BeanContext;
import com.mathbeta.storage.util.ServletUtil;

@Path("node")
public class NodeRestService {
	private static Logger log = LoggerFactory.getLogger(NodeRestService.class);
	private INodeService nodeService = (INodeService) BeanContext.getInstance()
			.getBean("nodeService");

	@GET
	@Path("list")
	public void gotoNodeList(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		List<Node> nodes = nodeService.findAllNodes();
		request.setAttribute("nodes", nodes);
		ServletUtil.forward("/pages/node-list.jsp", request, response);
	}

	@POST
	@Path("add")
	public void add(@FormParam("name") String name, @FormParam("ip") String ip,
			@FormParam("isolated") boolean isolated,
			@FormParam("storageRoot") String storageRoot,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("add node: " + name + ", " + ip + ", " + storageRoot);
		}
		nodeService.save(name, ip, isolated, storageRoot);
		// List<Node> nodes = nodeService.findAllNodes();
		// request.setAttribute("nodes", nodes);
		// ServletUtil.forward("/pages/node-list.jsp", request, response);
		ServletUtil.redirect("list", response);
	}

	@GET
	@Path("delete")
	public String delete(@QueryParam("ids") String ids) {
		return nodeService.delete(ids);
	}
}
