package com.mathbeta.storage.transfer;

import java.io.Serializable;

public abstract class AbstractTransfer {
	protected String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public AbstractTransfer(String url) {
		this.url = url;
	}

	public abstract boolean transfer(Serializable data);
}
