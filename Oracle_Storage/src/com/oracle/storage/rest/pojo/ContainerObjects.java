package com.oracle.storage.rest.pojo;

public class ContainerObjects {

	private String containerName;
	private String objectName;

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


	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}


	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

}
