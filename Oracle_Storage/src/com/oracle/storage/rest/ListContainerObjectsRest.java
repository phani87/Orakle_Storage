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

import com.oracle.storage.ListContainerObjects;
import com.oracle.storage.StorageAuth;

@Path("/listObjs")
public class ListContainerObjectsRest {

	
	StorageAuth storageAuth;
	ListContainerObjects listContainerObjects;
	final static Logger logger = Logger.getLogger(ListContainerObjectsRest.class.getSimpleName());
	
	
	/*@GET
    @Produces({MediaType.APPLICATION_JSON})
	public String getContainerList() throws IOException{
			logger.info("---------API CALL START---------");
			//storageAuth = new StorageAuth();
			listContainerObjects = new ListContainerObjects();
			//storageAuth.getAuthToken(cloud_username,cloud_password,cloud_storage_container);
			String containerRespose = listContainerObjects.getContainerListObjects(cloud_container_name);
			logger.info("---------API CALL END---------");
			 
			//JsonNode jsonNode = JsonLoader.fromString(containerRespose.toString());
			//JSONObject jsonObject = new JSONObject(containerRespose.toString());
			
		return containerRespose;
	}
*/
	
	@POST
    @Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getContainerListLogin(@FormParam("container_name") String cloud_container_name,
		    @Context HttpServletResponse servletResponse) throws IOException{
			logger.info("---------API CALL START---------");
			//storageAuth = new StorageAuth();
			listContainerObjects = new ListContainerObjects();
			//storageAuth.getAuthToken(cloud_username,cloud_password,cloud_storage_container);
			String containerRespose = listContainerObjects.getContainerListObjects(cloud_container_name);
			logger.info("---------API CALL END---------");
			 
			//JsonNode jsonNode = JsonLoader.fromString(containerRespose.toString());
			//JSONObject jsonObject = new JSONObject(containerRespose.toString());
			
		return containerRespose;
	}
	
	
	
	

}
