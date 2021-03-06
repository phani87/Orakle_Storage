package com.oracle.storage.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.oracle.storage.StorageAuth;
import com.oracle.storage.UploadObject;

@Path("/uploadObj")
public class UploadObjectRest extends Application{

	UploadObject uploadObject;
	final static Logger logger = Logger.getLogger(UploadObjectRest.class.getSimpleName());
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public Response uploadToContainer(@FormDataParam("containerName") String cloud_container_name,
            @FormDataParam("file") InputStream cloud_input_stream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		logger.info("---------API CALL START---------");
		uploadObject = new UploadObject();
		String containerRespose = uploadObject.uploadObjectImpl(cloud_container_name, cloud_input_stream, fileDetail.getFileName());
		logger.info("---------API CALL END---------");
		
		return Response.ok().entity(containerRespose).build();
	}

}
