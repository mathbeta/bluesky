package com.mathbeta.storage.transfer;

import java.net.URI;

import com.mathbeta.storage.transfer.impl.HttpTransfer;
import com.mathbeta.storage.transfer.impl.TcpTransfer;
import com.mathbeta.storage.util.Schemes;

public class TransferFactory {
	public static AbstractTransfer getTransfer(String urlStr) {
		URI uri = URI.create(urlStr);
		String scheme = uri.getScheme();
		if (Schemes.HTTP.equalsIgnoreCase(scheme)) {
			return new HttpTransfer(urlStr);
		} else if (Schemes.TCP.equalsIgnoreCase(scheme)) {
			return new TcpTransfer(urlStr);
		}
		return null;
	}
}
