package com.oracle.storage.rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.oracle.storage.GetContainers;
import com.oracle.storage.StorageAuth;
import com.oracle.storage.rest.pojo.LoginUser;

@Path("/getContainer")
public class GetContainerListRest extends Application{
	
	StorageAuth storageAuth;
	GetContainers getContainers;
	final static Logger logger = Logger.getLogger(GetContainerListRest.class.getSimpleName());
	
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	public Response getContainerList() throws IOException{
			logger.info("---------API CALL START---------");
			//storageAuth = new StorageAuth();
			getContainers = new GetContainers();
			//storageAuth.getAuthToken(cloud_username,cloud_password,cloud_storage_container);
			String containerRespose = getContainers.getContainerList();
			logger.info("---------API CALL END---------");
			 
			//JsonNode jsonNode = JsonLoader.fromString(containerRespose.toString());
			//JSONObject jsonObject = new JSONObject(containerRespose.toString());
			
			return Response.ok().entity(containerRespose).build();
	}

/*	
	@POST
	@Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getContainerListLogin(@FormParam("username") String cloud_username,
		    @FormParam("password") String cloud_password,
		    @FormParam("storage_container") String cloud_storage_container,
		    @Context HttpServletResponse servletResponse) throws IOException{
			logger.info("---------API CALL START---------");
			storageAuth = new StorageAuth();
			getContainers = new GetContainers();
			storageAuth.getAuthToken(cloud_username,cloud_password,cloud_storage_container);
			String containerRespose = getContainers.getContainerList();
			logger.info("---------API CALL END---------");
			 
			//JsonNode jsonNode = JsonLoader.fromString(containerRespose.toString());
			//JSONObject jsonObject = new JSONObject(containerRespose.toString());
			
		return Response.ok().entity(containerRespose).build();
	}*/
	
	
	@POST
	@Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getContainerListLogin(LoginUser user,
		    @Context HttpServletResponse servletResponse) throws IOException{
			logger.info("---------API CALL START---------");
			storageAuth = new StorageAuth();
			getContainers = new GetContainers();
			storageAuth.getAuthToken(user.getUsername(),user.getPassword(),user.getIdentityDomain());
			String containerRespose = getContainers.getContainerList();
			logger.info("---------API CALL END---------");
			 
			//JsonNode jsonNode = JsonLoader.fromString(containerRespose.toString());
			//JSONObject jsonObject = new JSONObject(containerRespose.toString());
			
		return Response.ok().entity(containerRespose).build();
	}
	
	
	
	
}
