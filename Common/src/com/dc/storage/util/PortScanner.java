package com.dc.storage.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 端口探测，未占用端口返回true，否则返回false
 * 
 * @author xuxy
 * 
 */
public class PortScanner {
	public static boolean scan(int port) {
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", port);
			socket.setTcpNoDelay(true);
			InputStream is = socket.getInputStream();
			int i = is.read();
			if (i != -1) {
				return false;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(socket);
		}

		return true;
	}
}
