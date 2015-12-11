package com.dc.storage.transfer.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import com.dc.storage.transfer.AbstractTransfer;
import com.dc.storage.util.IOUtils;
import com.dc.storage.util.TransferUtil;

public class TcpTransfer extends AbstractTransfer {
	public TcpTransfer(String url) {
		super(url);
	}

	@Override
	public boolean transfer(Serializable data) {
		Socket socket = null;
		try {
			URI uri = URI.create(url);
			socket = new Socket(uri.getHost(), uri.getPort());
			TransferUtil.writeObject(socket.getOutputStream(), data);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(socket);
		}

		return false;
	}

}
