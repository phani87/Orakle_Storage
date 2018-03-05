package com.oracle.storage.rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.oracle.storage.GetContainers;
import com.oracle.storage.ListContainerObjects;
import com.oracle.storage.StorageAuth;
import com.oracle.storage.rest.pojo.ContainerObjects;
import com.oracle.storage.rest.pojo.LoginUser;

@Path("/listObjs")
public class ListContainerObjectsRest{

	
	StorageAuth storageAuth;
	ListContainerObjects listContainerObjects;
	final static Logger logger = Logger.getLogger(ListContainerObjectsRest.class.getSimpleName());
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getContainerListLogin(ContainerObjects container,
		    @Context HttpServletResponse servletResponse) throws IOException{
			logger.info("---------API CALL START---------");
			listContainerObjects = new ListContainerObjects();
			String resonse = listContainerObjects.getContainerListObjects(container.getContainerName());
			logger.info("---------API CALL END---------");
			 
		return Response.ok().entity(resonse).build();
	}
	
	
	

}
