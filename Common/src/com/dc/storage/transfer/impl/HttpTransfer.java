package com.dc.storage.transfer.impl;

import java.io.Serializable;

import com.dc.storage.transfer.AbstractTransfer;

public class HttpTransfer extends AbstractTransfer {
	public HttpTransfer(String url) {
		super(url);
	}

	@Override
	public boolean transfer(Serializable data) {
		return false;
	}

}
