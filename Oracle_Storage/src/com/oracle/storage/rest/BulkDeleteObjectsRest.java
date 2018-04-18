package com.oracle.storage.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.oracle.storage.BulkDeleteObjects;


@Path("/bulkdeleteObj")
public class BulkDeleteObjectsRest {


	BulkDeleteObjects bulkDeleteObjects;
	final static Logger logger = Logger.getLogger(UploadObjectRest.class.getSimpleName());

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadToContainer(String toDelete) throws IOException {
		logger.info("---------API CALL START---------");
		bulkDeleteObjects = new BulkDeleteObjects();
		String containerRespose = bulkDeleteObjects.bulkDeleteObjectImpl(toDelete);
		logger.info("---------API CALL END---------");

		return Response.ok().entity(containerRespose).build();
	}


}
