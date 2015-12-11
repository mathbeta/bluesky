package com.dc.storage.console.start;

import java.net.InetAddress;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.storage.util.ConfigHelper;

public class StartConsole {
	
	public static void main(String[] args) {
		StartConsole.getInstance().startJetty();
	}
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StartConsole.class);
	
	private static StartConsole  console = null;
	
	private StartConsole(){
		
	}
	
	public static StartConsole getInstance(){
		if ( console == null ) {
			synchronized( StartConsole.class ){
				if( console == null ){
					console = new StartConsole();
				}
			}
		}
		return console;
	}
	
	public void startJetty(){
		
		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(Integer.parseInt(ConfigHelper.getValue("server.port")));
		server.setConnectors(new Connector[]{connector});
		 
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath(ConfigHelper.getValue("server.name"));
        webapp.setResourceBase(ConfigHelper.getValue("server.resource"));
//        webapp.setDescriptor(ConfigHelper.getValue("server.descriptor"));
        webapp.setParentLoaderPriority(true);
        webapp.setClassLoader(Thread.currentThread().getContextClassLoader());
        
        server.setHandler(webapp);

        try {
			server.start();
			System.out.println("==================================================================");
			System.out.println("Console has been started. Please visit: http://" + InetAddress.getLocalHost().getHostAddress() + ":" + ConfigHelper.getValue("server.port") + ConfigHelper.getValue("server.name"));
			System.out.println("==================================================================");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
