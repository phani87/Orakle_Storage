package com.oracle.storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.oracle.storage.helper.GetURLs;

public class UploadObject {


	final static Logger logger = Logger.getLogger(UploadObject.class.getSimpleName()) ;


	public String uploadObjectImpl(String cloud_container_name, InputStream cloud_object_name) {
		logger.info("-------CREATE CONTAINER---------");
		StringBuffer result = null;


		try {

			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().getContainerURL().toString()+"/"+cloud_container_name);
			HttpPut putFileRequest = new HttpPut(new GetURLs().	getContainerURL().toString()+"/"+cloud_container_name+"/"+cloud_object_name);

			// add request header
//			putFileRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));
//			MultipartEntity entity = new MultipartEntity();
//			entity.addPart("file", new FileBody(new File()));
//			post.setEntity(entity);

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
				result = new StringBuffer();
				result.append("{\n");
				
				result.append("\"created_response\" :\"Unsuccessful upload\",\n");
				result.append("\"created_code\" :\"\"+451+\"\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();

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
