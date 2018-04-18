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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.oracle.storage.helper.GetURLs;

public class ListContainerObjects {

	final static Logger logger = Logger.getLogger(ListContainerObjects.class.getSimpleName()) ;


	public String getContainerListObjects(String cloud_container_name) {
		logger.info("-------GET LIST OBJECTS---------");
		StringBuilder result = null;


		try {

			HttpClient client = new DefaultHttpClient();
			HttpGet getContinerRequest = new HttpGet(new GetURLs().getContainerURL().toString()+"/"+cloud_container_name);

			// add request header
			getContinerRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));

			HttpResponse containerResponse = client.execute(getContinerRequest);

			int container_response = containerResponse.getStatusLine().getStatusCode();
			
			if(container_response==204) {
				result = new StringBuilder();
				result.append("{\"Objects\": [");
				result.append("{\"objName\" : \"No\" }");
				result.append("],\"totalbjs\" :\"0\"");
				result.append("}");	
			}else {
				BufferedReader rd = new BufferedReader(new InputStreamReader(containerResponse.getEntity().getContent()));

				/*result = new StringBuffer();
				result.append("{\n");
				int i=1;
				String line = "";
				while ((line = rd.readLine()) != null) {
						result.append("\"objName"+Integer.toString(i)+"\" : \""+line+"\", \n");
						i++;
				}
				result.append("\"totalObjs\" :\""+Integer.toString(i-1)+"\"\n}");
				// return result.toString();
	*/		
				result = new StringBuilder();
				result.append("{\"Objects\": [");
				int i=1;
				String line = "";
				while ((line = rd.readLine()) != null) {
					
					if("Token not found in cache".equalsIgnoreCase(line)) {
						new StorageAuth().getAuthToken();
						new ListContainerObjects().getContainerListObjects(cloud_container_name);
					}
						result.append("{\"objName\" : \""+line+"\" },");
						i++;
				}
				result.deleteCharAt((result.length()-1));
				result.append("],\"totalbjs\" :\""+Integer.toString(i-1)+"\"");
				result.append("}");	
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
