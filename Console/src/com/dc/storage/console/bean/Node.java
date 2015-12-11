package com.dc.storage.console.bean;

public class Node {
	private long id;
	private String name;
	private String ip;
	private boolean isolated;
	private boolean status;
	private String storageRoot;
	private String transferPorts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isIsolated() {
		return isolated;
	}

	public void setIsolated(boolean isolated) {
		this.isolated = isolated;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getStorageRoot() {
		return storageRoot;
	}

	public void setStorageRoot(String storageRoot) {
		this.storageRoot = storageRoot;
	}

	public String getTransferPorts() {
		return transferPorts;
	}

	public void setTransferPorts(String transferPorts) {
		this.transferPorts = transferPorts;
	}
}
