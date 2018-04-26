package com.oracle.storage.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class GetURLs {

	public Map<String, String> getAuthHeaders() {
		Map<String, String> authMap = new HashMap<>();

		Properties prop = new Properties();
		InputStream input = null;
		String auth_url = "";

		try {
			/*String filename = "properties/auth.properties";
			input = this.getClass().getClassLoader().getResourceAsStream(filename);*/
			//input =  getClass().getClassLoader().getResourceAsStream(filename);
			//input = new FileInputStream("WebContent/properties/auth.properties");
			input = new FileInputStream("C:\\Tomcat\\properties\\auth.properties");

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

	public String getContainerURL() {
		Properties prop = new Properties();
		InputStream input = null;
		String auth_url = "";

		try {
			/*String filename = "properties/urls.properties";
			input = this.getClass().getClassLoader().getResourceAsStream(filename);*/
			//input =  getClass().getClassLoader().getResourceAsStream(filename);
			//input = new FileInputStream("WebContent/properties/urls.properties");
			input = new FileInputStream("C:\\Tomcat\\properties\\urls.properties");

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
	
	public String getAuthURL() {
		Properties prop = new Properties();
		InputStream input = null;
		String auth_url = "";

		try {
			//ResourceBundle bundle = ResourceBundle.getBundle("properties/urls.properties");
			/*String filename = "properties/urls.properties";
			input = this.getClass().getClassLoader().getResourceAsStream(filename);*/
			//input =  getClass().getClassLoader().getResourceAsStream(filename);
			//input = new FileInputStream("WebContent/properties/urls.properties");
			input = new FileInputStream("C:\\Tomcat\\properties\\urls.properties");

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
	
}
