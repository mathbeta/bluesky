package com.dc.storage.console.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

@ApplicationPath("/ws/*")
public class MyApplication extends Application {
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<Class<?>>();
		set.add(MyExceptionMapper.class);
		set.add(MultiPartFeature.class);
		return set;
	}
	
	public Set<Object> getSingletons() {
		Set<Object> set = new HashSet<Object>();
//		Logger logger = Logger.getLogger(LoggingFilter.class.getName());
//		set.add(new LoggingFilter(logger, true));
		return set;
	}
}
