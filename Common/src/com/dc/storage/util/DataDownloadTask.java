package com.dc.storage.util;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDownloadTask implements Runnable {
	private static Logger log = LoggerFactory
			.getLogger(DataDownloadTask.class);

	private Socket socket;

	public DataDownloadTask(Socket socket) {
		this.socket = socket;
	}

	public void run() {}
}
