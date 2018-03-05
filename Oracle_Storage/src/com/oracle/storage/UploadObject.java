package com.oracle.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.oracle.storage.helper.GetURLs;

public class UploadObject {


	final static Logger logger = Logger.getLogger(UploadObject.class.getSimpleName()) ;
	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";


	public String uploadObjectImpl(String cloud_container_name, InputStream cloud_input_stream, String fileName) {
		logger.info("-------UPLOAD FILE---------");
		StringBuffer result = null;


		try {
			
			File tempfile = createTempFile(cloud_input_stream);
			
			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().getContainerURL().toString()+"/"+cloud_container_name);
			HttpPut putFileRequest = new HttpPut(new GetURLs().	getContainerURL().toString()+"/"+cloud_container_name+"/"+fileName);
			
			putFileRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));
			
			 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			 builder.addBinaryBody("file", tempfile,
			      ContentType.APPLICATION_OCTET_STREAM, "file.ext");
			 
			    HttpEntity multipart = builder.build();
			    putFileRequest.setEntity(multipart);


			HttpResponse containerResponse = client.execute(putFileRequest);


			//BufferedReader rd = new BufferedReader(new InputStreamReader(containerResponse.getEntity().getContent()));
			
			int response_code = containerResponse.getStatusLine().getStatusCode();
			String respose_status = containerResponse.getStatusLine().getReasonPhrase().toString();

			if(response_code==201) {
				result = new StringBuffer();
				result.append("{\n");
				
				result.append("\"created_response\" :\""+respose_status+"\",\n");
				result.append("\"created_code\" :\""+response_code+"\"}");
			}
			else {
				
				new StorageAuth().getAuthToken();
				
				result = new StringBuffer();
				result.append("{\n");
				
				result.append("\"created_response\" :\"Unsuccessful upload\",\n");
				result.append("\"created_code\" :\"451\"}");
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();

	}
	
	
	
	 private File createTempFile (InputStream in) throws IOException {
	        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
	        tempFile.deleteOnExit();
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(in, out);
	        }
	        return tempFile;
	    } 

	private Map<String, String> getAuthHeaders() {
		Map<String, String> authMap = new HashMap<>();

		Properties prop = new Properties();
		InputStream input = null;
		String auth_url = "";

		try {

			input = new FileInputStream("D:\\Workspace_I\\Oracle_Storage\\WebContent\\properties\\auth.properties");

			prop.load(input);

			authMap.put("X-Auth-Token", prop.getProperty("X-Auth-Token"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return authMap;

	}

	private String getContainerURL() {
		Properties prop = new Properties();
		InputStream input = null;
		String auth_url = "";

		try {

			input = new FileInputStream("D:\\Workspace_I\\Oracle_Storage\\WebContent\\properties\\urls.properties");

			prop.load(input);

			auth_url = prop.getProperty("CONTAINER_URL");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return auth_url;

	}

	public static void main(String[] args) {
		GetContainers containers = new GetContainers();
		System.out.println(containers.getContainerList());
	}
	



}
