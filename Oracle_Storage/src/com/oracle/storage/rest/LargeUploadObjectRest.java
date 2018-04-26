package com.oracle.storage.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.oracle.storage.LargeUploadObjects;
import com.oracle.storage.StorageAuth;
import com.oracle.storage.UploadObject;
import com.oracle.storage.helper.FileSplitter;

@Path("/largeuploadObj")
public class LargeUploadObjectRest extends Application{

	FileSplitter fileSplitter;
	final static Logger logger = Logger.getLogger(LargeUploadObjectRest.class.getSimpleName());
	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public Response uploadToContainer(@FormDataParam("containerName") String cloud_container_name,
            @FormDataParam("file") InputStream cloud_input_stream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		logger.info("---------API CALL START---------");
		File tempfile = createTempFile(cloud_input_stream);
		fileSplitter = new FileSplitter();
		fileSplitter.splitFile(tempfile);
		String tempFolderName = tempfile.getName(); 
		if (tempFolderName.indexOf(".") > 0)
			tempFolderName = tempFolderName.substring(0, tempFolderName.lastIndexOf("."));
		String tempDirPath = tempfile.getParent()+"/"+tempFolderName;
		String parentDirName = tempDirPath;
		System.out.println(parentDirName);
		String containerRespose =  fileSplitter.readAllFiles(cloud_container_name, parentDirName);
		//String containerRespose ="";
		logger.info("---------API CALL END---------");
		
		return Response.ok().entity(containerRespose).build();
	}

	private File createTempFile (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    } 

}
