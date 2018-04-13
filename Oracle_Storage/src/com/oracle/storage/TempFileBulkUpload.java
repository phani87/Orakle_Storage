package com.oracle.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.oracle.storage.helper.GetURLs;

public class TempFileBulkUpload {

	final static Logger logger = Logger.getLogger(UploadObject.class.getSimpleName());
	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";

	public String uploadObjectImpl(String cloud_container_name, InputStream cloud_input_stream, String fileName) {
		logger.info("-------UPLOAD FILE---------");
		StringBuffer result = null;

		try {

			File tempfile = createTempFile(cloud_input_stream);

			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().getContainerURL().toString() + "/" + cloud_container_name
					+ "?extract-archive=tar.gz");
			HttpPut putFileRequest = new HttpPut(new GetURLs().getContainerURL().toString() + "/" + cloud_container_name
					+ "?extract-archive=tar.gz");

			putFileRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));
			putFileRequest.addHeader("Content-Type", "application/x-www-form-urlencoded, charset=utf-8");
			putFileRequest.addHeader("Accept", "*/*");

			File file = new File("D://TestBulkUpload.tar.gz");

			FileEntity entity = new FileEntity(file, "binary/octet-stream");

			putFileRequest.setEntity(entity);

			HttpResponse containerResponse = client.execute(putFileRequest);

			System.out.println(new BasicResponseHandler().handleResponse(containerResponse));

			int response_code = containerResponse.getStatusLine().getStatusCode();
			String respose_status = containerResponse.getStatusLine().getReasonPhrase().toString();

			if (response_code == 201) {
				result = new StringBuffer();
				result.append("{\n");

				result.append("\"created_response\" :\"" + respose_status + "\",\n");
				result.append("\"created_code\" :\"" + response_code + "\"}");
			} else {

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

	private File createTempFile(InputStream in) throws IOException {
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

	public static void main(String[] args) throws FileNotFoundException {

		String fileName = "TestBulkUpload.tar.gz";

		File initialFile = new File("D://" + fileName);
		InputStream targetStream = new FileInputStream(initialFile);
		TempFileBulkUpload temp = new TempFileBulkUpload();
		temp.uploadObjectImpl("testingPhani", targetStream, fileName);
	}

}
