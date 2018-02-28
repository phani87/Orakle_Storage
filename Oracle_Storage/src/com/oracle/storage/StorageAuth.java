package com.oracle.storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.oracle.storage.helper.GetURLs;

public class StorageAuth {

	private String AUTH_URL = "https://Storage-gse00013232.storage.oraclecloud.com/auth/v1.0";
	final static Logger logger = Logger.getLogger(StorageAuth.class.getSimpleName()) ;
	

	public void getAuthToken(String cloud_username, String cloud_password, String cloud_storage_container) {
		logger.info("--------GET AUTH TOKEN AND STORE--------");
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getAuthReq = new HttpGet(new GetURLs().getAuthURL());
			logger.info(new GetURLs().getAuthURL().toString());
			// add request header
			getAuthReq.addHeader("X-Storage-User", "Storage-"+cloud_storage_container+":"+cloud_username);
			getAuthReq.addHeader("X-Storage-Pass", cloud_password);
			// request.addHeader("cache-control", "no-cache");

			HttpResponse authResponse = client.execute(getAuthReq);

			Header[] responseHeaders = authResponse.getAllHeaders();
			FileOutputStream fileOutputStream = new FileOutputStream("D:\\Oracle_Storage\\Oracle_Storage\\WebContent\\properties\\auth.properties");
			Properties prop = new Properties();
			prop.setProperty("X-Storage-User", "Storage-"+cloud_storage_container+":"+cloud_username);
			prop.setProperty("X-Storage-Pass", cloud_password);
			

			for (Header header : responseHeaders) {
				if ((header.getName().equalsIgnoreCase("X-Auth-Token"))
						|| (header.getName().equalsIgnoreCase("X-Storage-Token"))) {
					prop.setProperty(header.getName(), header.getValue());
				}
			}
			prop.store(fileOutputStream, null);
			// return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	public void getAuthToken() {
		logger.info("--------GET AUTH TOKEN AND STORE--------");
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getAuthReq = new HttpGet(new GetURLs().getAuthURL());
			String[] auth_props = getAuthProps();

			// add request header
			getAuthReq.addHeader("X-Storage-User", auth_props[0]);
			getAuthReq.addHeader("X-Storage-Pass", auth_props[1]);
			// request.addHeader("cache-control", "no-cache");

			HttpResponse authResponse = client.execute(getAuthReq);

			Header[] responseHeaders = authResponse.getAllHeaders();
			FileOutputStream fileOutputStream = new FileOutputStream("D:\\Oracle_Storage\\Oracle_Storage\\WebContent\\properties\\auth.properties");
			Properties prop = new Properties();
			prop.setProperty("X-Storage-User",  auth_props[0]);
			prop.setProperty("X-Storage-Pass", 	auth_props[1]);
			

			for (Header header : responseHeaders) {
				if ((header.getName().equalsIgnoreCase("X-Auth-Token"))
						|| (header.getName().equalsIgnoreCase("X-Storage-Token"))) {
					prop.setProperty(header.getName(), header.getValue());
				}
			}
			prop.store(fileOutputStream, null);
			// return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	private String getAuthURL() {
		Properties prop = new Properties();
		InputStream input = null;
		String auth_url = "";

		try {

			input = new FileInputStream("D:\\Oracle_Storage\\Oracle_Storage\\WebContent\\properties\\urls.properties");

			prop.load(input);

			auth_url = prop.getProperty("AUTH_URL");
			
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
	
	private String[] getAuthProps() {
		Properties prop = new Properties();
		InputStream input = null;
		String[] auth_props = new String[2];

		try {

			input = new FileInputStream("D:\\Oracle_Storage\\Oracle_Storage\\WebContent\\properties\\auth.properties");

			prop.load(input);

			auth_props[0] = prop.getProperty("X-Storage-User");
			auth_props[1]= prop.getProperty("X-Storage-Pass");
			
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
		return auth_props;

	}
	
	

	public static void main(String[] args) {
		StorageAuth sa = new StorageAuth();
	//	sa.getAuthToken();
	}

}
