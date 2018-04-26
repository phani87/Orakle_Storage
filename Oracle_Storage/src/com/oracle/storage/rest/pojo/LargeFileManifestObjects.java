package com.oracle.storage.rest.pojo;

public class LargeFileManifestObjects {

	private String etags;
	private String path;
	private long size_bytes;
	
	
	public String getEtags() {
		return etags;
	}
	public void setEtags(String etags) {
		this.etags = etags;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getSize_bytes() {
		return size_bytes;
	}
	public void setSize_bytes(long size_bytes) {
		this.size_bytes = size_bytes;
	}
	
}
