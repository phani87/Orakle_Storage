package com.oracle.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.oracle.storage.helper.GetURLs;

public class DownloadObjects {


	final static Logger logger = Logger.getLogger(DownloadObjects.class.getSimpleName()) ;
	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";


	public String downloadObjectsImpl(String cloud_container_name, String cloud_objectName, String cloud_fileName) {
		logger.info("-------DOWNLOAD FILE---------");
		StringBuffer result = null;
		String output = null;
		try {
			
			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().	getContainerURL().toString()+"/"+cloud_container_name+"/"+cloud_objectName);
			HttpGet getObjectRequest = new HttpGet(new GetURLs().	getContainerURL().toString()+"/"+cloud_container_name+"/"+cloud_objectName);
			
			getObjectRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));
			
			

			HttpResponse containerResponse = client.execute(getObjectRequest);
			
			output = new BasicResponseHandler().handleResponse(containerResponse);

			int response_code = containerResponse.getStatusLine().getStatusCode();
			String respose_status = containerResponse.getStatusLine().getReasonPhrase().toString();

			if(response_code==200) {
				result = new StringBuffer();
				result.append("{\n");
				
				result.append("\"filedownload\" :\"success\",\n");
				result.append("\"created_code\" :\""+response_code+"\"}");
				
				//saveToFile(output, cloud_fileName);
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
		return output;

	}
	
	
	
	 private File createTempFile (InputStream in) throws IOException {
	        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
	        tempFile.deleteOnExit();
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(in, out);
	        }
	        return tempFile;
	    } 
	 
	 private void saveToFile(String output, String fileName) {
		 String home = System.getProperty("user.home");
		 String filepath = home+"/Downloads/" + fileName; 
		 try {
			Files.write( Paths.get(filepath), output.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
