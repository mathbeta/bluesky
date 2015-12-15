package com.mathbeta.storage.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanContext {
	private ApplicationContext appContext;
	private static BeanContext context;

	private BeanContext() {
	}

	public static BeanContext getInstance() {
		if (context == null) {
			synchronized (BeanContext.class) {
				if (context == null) {
					context = new BeanContext();
				}
			}
		}
		return context;
	}

	public void loadContext(String path) {
		try {
			appContext = new ClassPathXmlApplicationContext(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getBean(String name) {
		return appContext.getBean(name);
	}
}
