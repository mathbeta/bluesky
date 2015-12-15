package com.mathbeta.storage.console.ws.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mathbeta.storage.console.bean.User;
import com.mathbeta.storage.console.service.IUserService;
import com.mathbeta.storage.util.BeanContext;
import com.mathbeta.storage.util.ConfigHelper;

@Path("user")
public class UserRestService {
	private static Logger log = LoggerFactory.getLogger(UserRestService.class);
	private IUserService userService = (IUserService) BeanContext.getInstance()
			.getBean("userService");

	@POST
	@Path("login")
	public void login(@FormParam("userName") String userName,
			@FormParam("password") String password,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("receive login request: " + userName);
		}
		User user = userService.validate(userName, password);
		if (user != null) {
			request.getSession().setAttribute("user", user);
			try {
				response.sendRedirect(ConfigHelper.getValue("server.name")
						+ "/pages/my.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				response
						.getWriter()
						.write(
								"<p>用户名或密码错误</p><p><a href=\"/index.jsp\">重新登录</a></p>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
