package com.oracle.storage.rest.pojo;

public class ContainerObjects {

	private String containerName;

	public String getContainerName() {
		return containerName;
	}


	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	@Override
	public String toString() {
		return "Objects [containeName=" + containerName +"]";
	}

}
