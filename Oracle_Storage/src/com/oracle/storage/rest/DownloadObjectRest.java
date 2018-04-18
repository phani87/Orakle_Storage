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

import com.oracle.storage.BulkUploadObjects;
import com.oracle.storage.DownloadObjects;
import com.oracle.storage.rest.pojo.ContainerObjects;

@Path("/downloadObj")
public class DownloadObjectRest {

	DownloadObjects downloadObjs;
	final static Logger logger = Logger.getLogger(UploadObjectRest.class.getSimpleName());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response uploadToContainer(ContainerObjects container) throws IOException {
		logger.info("---------API CALL START---------");
		downloadObjs = new DownloadObjects();
		String containerRespose = downloadObjs.downloadObjectsImpl(container.getContainerName(), container.getObjectName(), container.getFileName());
		logger.info("---------API CALL END---------");
		return Response.ok().entity(containerRespose).build();
	}


}
