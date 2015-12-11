package com.dc.storage.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigHelper {
	private static Logger log = LoggerFactory.getLogger(ConfigHelper.class);

	private static Properties configs;

	private ConfigHelper() {
	}

	public static String getValue(String key) {
		if (configs == null) {
			configs = new Properties();
			InputStream is = ConfigHelper.class.getClassLoader()
					.getResourceAsStream("config.properties");
			try {
				configs.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return configs.getProperty(key);
	}

	public static Object setValue(String key, String value) {
		return configs.setProperty(key, value);
	}

	public static void write() {
		File file = new File(ConfigHelper.class.getClassLoader().getResource(
				"config.properties").getFile());
		try {
			Writer writer = new FileWriter(file, false);
			configs.store(writer, "updated at: " + new Date());
			log.info("overwrite config.properties successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
