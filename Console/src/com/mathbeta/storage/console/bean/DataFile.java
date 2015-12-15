package com.mathbeta.storage.console.bean;

import java.util.List;

public class DataFile {
	private long id;
	private String name;
	private long lastModified;
	private long length;
	private boolean isDir;
	private long parentId;
	private List<DataFile> children;

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

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<DataFile> getChildren() {
		return children;
	}

	public void setChildren(List<DataFile> children) {
		this.children = children;
	}
}
