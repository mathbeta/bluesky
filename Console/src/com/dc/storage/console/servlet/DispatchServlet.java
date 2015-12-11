package com.dc.storage.console.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import com.dc.storage.util.BeanContext;
import com.dc.storage.util.NodeInitListener;
import com.dc.storage.util.NodeScanner;

public class DispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) {
		BeanContext.getInstance().loadContext("applicationContext.xml");
		// scan nodes
		NodeScanner.scan();
		// start console command port
		NodeInitListener.listen();
	}
}
