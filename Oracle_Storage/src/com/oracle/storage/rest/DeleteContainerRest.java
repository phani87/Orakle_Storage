package com.oracle.storage.rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.oracle.storage.DeleteContainer;
import com.oracle.storage.StorageAuth;
import com.oracle.storage.rest.pojo.ContainerObjects;

@Path("/deleteContainer")
public class DeleteContainerRest {


	
	StorageAuth storageAuth;
	DeleteContainer deleteContainer;
	final static Logger logger = Logger.getLogger(DeleteContainerRest.class.getSimpleName());
	

	
	@POST
    @Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getContainerList(ContainerObjects container,
			@Context HttpServletResponse servletResponse) throws IOException {
		logger.info("---------API CALL START---------");
		deleteContainer = new DeleteContainer();
		String containerRespose = deleteContainer.deleteContainerImpl(container.getContainerName());
		logger.info("---------API CALL END---------");

		return  Response.ok().entity(containerRespose).build();
	}
	
	




}
