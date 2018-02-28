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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import com.oracle.storage.helper.GetURLs;	

public class DeleteContainer {

	final static Logger logger = Logger.getLogger(DeleteContainer.class.getSimpleName()) ;


	public String deleteContainerImpl(String cloud_container_name) {
		logger.info("-------CREATE CONTAINER---------");
		StringBuffer result = null;


		try {

			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().getContainerURL().toString()+"/"+cloud_container_name);
			HttpDelete getContinerRequest = new HttpDelete(new GetURLs().getContainerURL().toString()+"/"+cloud_container_name);

			// add request header
			getContinerRequest.addHeader("X-Auth-Token", getAuthHeaders().get("X-Auth-Token"));

			HttpResponse containerResponse = client.execute(getContinerRequest);


			//BufferedReader rd = new BufferedReader(new InputStreamReader(containerResponse.getStatusLine());
			int response_code = containerResponse.getStatusLine().getStatusCode();
			String respose_status = containerResponse.getStatusLine().getReasonPhrase().toString();
			if(response_code==204) {
				result = new StringBuffer();
				result.append("{\n");
				
				result.append("\"delete_response\" :\""+respose_status+"\",\n");
				result.append("\"delete_code\" :\""+respose_status+"\"}");
			}
			else {
				result = new StringBuffer();
				result.append("{\n");
				
				result.append("\"delete_response\" :\"Unsuccessful Delete\",\n");
				result.append("\"delete_code\" :\"\"+450+\"\"}");
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

	/*private String getContainerURL() {
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

	}*/

	public static void main(String[] args) {
		GetContainers containers = new GetContainers();
		System.out.println(containers.getContainerList());
	}
	




}
