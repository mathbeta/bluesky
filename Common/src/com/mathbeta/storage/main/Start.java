package com.mathbeta.storage.main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Start {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("Usage: Start [<console>|<node>]");
			return;
		}
		String excp = System.getProperty("storage.classpath");
		String[] pathes = excp.split(";");
		List<URL> urls = new ArrayList<URL>();

		for (String cp : pathes) {
			File cpdir = new File(cp);
			if (!cpdir.exists() || !cpdir.isDirectory()) {
				System.err
						.println("Please set system property \"storage.classpath\" properly, pointed to local directories, seperated by ';'!");
			} else {
				getJars(urls, cpdir);
			}
		}
		URL[] urlArray = new URL[urls.size()];
		for (int i = 0; i < urlArray.length; i++) {
			urlArray[i] = urls.get(i);
		}

		URLClassLoader cl = new URLClassLoader(urlArray);
		Thread.currentThread().setContextClassLoader(cl);
		if ("console".equalsIgnoreCase(args[0])) {
			try {
				Class clz = Class.forName(
						"com.mathbeta.storage.console.start.StartConsole", false, cl);
				Method method = clz.getDeclaredMethod("main", String[].class);
				method.setAccessible(true);
				method.invoke(null, new Object[] { new String[0] });
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if ("node".equalsIgnoreCase(args[0])) {
			try {
				Class clz = Class.forName(
						"com.mathbeta.storage.node.start.StartNode", false, cl);
				Method method = clz.getDeclaredMethod("main", String[].class);
				method.setAccessible(true);
				method.invoke(null, new Object[] { new String[0] });
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private static void getJars(List<URL> urls, File cpdir) {
		System.out.println("Loading JAR file from " + cpdir);

		for (File f : cpdir.listFiles()) {
			if (f.isDirectory()) {
				getJars(urls, f);
			}
			if (f.getName().endsWith(".jar")) {
				try {
					URL url = f.toURI().toURL();
					urls.add(url);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
