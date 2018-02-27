package com.oracle.storage.rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.oracle.storage.CreateContainer;
import com.oracle.storage.DeleteContainer;
import com.oracle.storage.StorageAuth;

@Path("/deleteContainer")
public class DeleteContainerRest {


	
	StorageAuth storageAuth;
	DeleteContainer deleteContainer;
	final static Logger logger = Logger.getLogger(DeleteContainerRest.class.getSimpleName());
	

	
	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getContainerList(@FormParam("containerName") String cloud_container_name,
			@Context HttpServletResponse servletResponse) throws IOException {
		logger.info("---------API CALL START---------");
		deleteContainer = new DeleteContainer();
		String containerRespose = deleteContainer.deleteContainerImpl(cloud_container_name);
		logger.info("---------API CALL END---------");

		return containerRespose;
	}
	
	




}
