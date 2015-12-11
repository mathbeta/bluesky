package com.dc.storage.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	public static File save(InputStream is, String path) {
		OutputStream os = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			os = new FileOutputStream(file);
			byte[] buf = new byte[2048];
			int len = -1;
			while ((len = is.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			os.flush();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
		return null;
	}

	public static void writeData(File file, byte[] data, boolean append) {
		try {
			OutputStream os = new FileOutputStream(file, append);
			os.write(data);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create file associated with contained directories
	 * 
	 * @param path
	 * @return
	 */
	public static File createFile(String path) {
		File f = new File(path);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		return f;
	}

	public static String getFileName(String path) {
		int i = path.lastIndexOf("/");
		if (i >= 0 && !path.endsWith("/")) {
			return path.substring(i + 1);
		}
		return path;
	}

	public static String getFilePath(String path) {
		int i = path.lastIndexOf("/");
		if (i >= 0) {
			return path.substring(0, i);
		}
		return path;
	}
}
