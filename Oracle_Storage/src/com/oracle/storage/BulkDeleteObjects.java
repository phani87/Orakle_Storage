package com.oracle.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.oracle.storage.helper.GetURLs;
import com.oracle.storage.helper.RequestBodyUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;

import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class BulkDeleteObjects {

	final static Logger logger = Logger.getLogger(UploadObject.class.getSimpleName());
	public static String PREFIX = "deletetemp";
	public static String SUFFIX = ".txt";

	public String bulkDeleteObjectImpl(String toDelete) {
		logger.info("-------Delete FILE---------");
		StringBuffer result = null;

		try {
			
			//File tempfile = createTempFile(cloud_input_stream);
			
			//String toDelete = IOUtils.toString(cloud_input_stream, "UTF-8");
			
			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().	getContainerURL().toString()+"/?bulk-delete");
			HttpPost postDelRequest = new HttpPost(new GetURLs().	getContainerURL().toString()+"/?bulk-delete");
			
			postDelRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));
			postDelRequest.addHeader("Accept", "*/*");
			postDelRequest.addHeader("Content-Type", "text/plain");
			
			 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			/* builder.addBinaryBody("file", tempfile,
					 ContentType.create("text/plain", "UTF-8"), "file.ext");*/
			 builder.addTextBody("del",toDelete);
			 
			    HttpEntity multipart = builder.build();
			    postDelRequest.setEntity(multipart);


			HttpResponse containerResponse = client.execute(postDelRequest);
			
			System.out.println(new BasicResponseHandler().handleResponse(containerResponse));
			
			int response_code = containerResponse.getStatusLine().getStatusCode();
			String respose_status = containerResponse.getStatusLine().getReasonPhrase().toString();

			if(response_code==200) {
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

	private File createTempFile(InputStream in) throws IOException {
		final File tempFile = File.createTempFile(PREFIX, SUFFIX);
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}

	public void bulkUploadFile(String fileName) throws IOException {

		new StorageAuth().getAuthToken();
		OkHttpClient client = new OkHttpClient();

		com.squareup.okhttp.MediaType MEDIA_TYPE = com.squareup.okhttp.MediaType.parse("application/octet-stream");

		// RequestBody body = RequestBody.create(MEDIA_TYPE, new File("D://" +
		// fileName));

		File initialFile = new File("D://" + fileName);
		InputStream targetStream = new FileInputStream(initialFile);

		RequestBody reqBody = RequestBodyUtil.create(MEDIA_TYPE, targetStream);

		Request request = new Request.Builder().url(
				"https://Storage-164df683b55549d3af2007b71b3c37cc.storage.oraclecloud.com/v1/Storage-164df683b55549d3af2007b71b3c37cc/testingPhani?extract-archive=tar.gz")
				.put(reqBody).addHeader("x-auth-token", new GetURLs().getAuthHeaders().get("X-Auth-Token"))
				.addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cache-Control", "no-cache")
				.build();

		Response response = client.newCall(request).execute();
		ResponseBody responseBody = response.body();

		// int response_code = response.body();
		// String respose_status = response.body().bytes().toString();
		System.out.println(responseBody.string());

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

	public static void main(String[] args) throws IOException {
		BulkDeleteObjects blk = new BulkDeleteObjects();
		blk.bulkUploadFile("TestBulkUpload.tar.gz");
		;
	}

}
