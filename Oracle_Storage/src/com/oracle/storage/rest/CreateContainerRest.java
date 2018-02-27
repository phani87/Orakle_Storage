package com.oracle.storage.rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.oracle.storage.CreateContainer;
import com.oracle.storage.StorageAuth;


@Path("/createContainer")
public class CreateContainerRest {

	
	StorageAuth storageAuth;
	CreateContainer createContainer;
	final static Logger logger = Logger.getLogger(GetContainerListRest.class.getSimpleName());
	

	
	@POST
    @Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getContainerList(@FormParam("containerName") String cloud_container_name,
			@Context HttpServletResponse servletResponse) throws IOException {
		logger.info("---------API CALL START---------");
		createContainer = new CreateContainer();
		String containerRespose = createContainer.createContainerImpl(cloud_container_name);
		logger.info("---------API CALL END---------");

		return containerRespose;
	}
	
	


}
