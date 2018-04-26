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

public class LargeUploadObjects {

	final static Logger logger = Logger.getLogger(UploadObject.class.getSimpleName());
	public static String PREFIX = "stream2file";
	public static String SUFFIX = ".tar.gz";
	public StringBuilder etags = new StringBuilder();

	public String largeUploadObjectImpl(String cloud_container_name, String filename, String filePath) {
		logger.info("-------LARGE UPLOAD FILE---------");
		StringBuffer result = null;
		String e_tag = "";
		try {

			File tempfile = new File(filePath);

			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().getContainerURL().toString() + "/" + cloud_container_name+"/" +
					 filename);
			HttpPut putFileRequest = new HttpPut(new GetURLs().getContainerURL().toString() + "/" + cloud_container_name+"/" +
					 filename);

			putFileRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));
			putFileRequest.addHeader("Accept", "*/*");

			FileEntity entity = new FileEntity(tempfile, "binary/octet-stream");

			putFileRequest.setEntity(entity);

			HttpResponse containerResponse = client.execute(putFileRequest);

			System.out.println(new BasicResponseHandler().handleResponse(containerResponse));

			int response_code = containerResponse.getStatusLine().getStatusCode();
			

			if (response_code == 201) {
				String respose_status = containerResponse.getStatusLine().getReasonPhrase().toString();
				Header[] etag = containerResponse.getAllHeaders();
				for(int i=0; i<etag.length; i++) {
					if(etag[i].getName().equalsIgnoreCase("Etag")) {
						e_tag = etag[i].getValue();
					}
				}
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
		return e_tag;

	}
	
	public String largeUploadManifestImpl(String cloud_container_name, String manifestFileName, String filePath) {
		logger.info("-------LARGE MANIFEST UPLOAD FILE---------");
		StringBuffer result = null;
		String e_tag = "";
		try {

			File tempfile = new File(filePath);

			HttpClient client = new DefaultHttpClient();
			logger.info(new GetURLs().getContainerURL().toString() + "/" + cloud_container_name+"/" +
					manifestFileName+"?multipart-manifest=put");
			HttpPut putFileRequest = new HttpPut(new GetURLs().getContainerURL().toString() + "/" + cloud_container_name+"/" +
					manifestFileName+"?multipart-manifest=put");

			putFileRequest.addHeader("X-Auth-Token", new GetURLs().getAuthHeaders().get("X-Auth-Token"));
			putFileRequest.addHeader("Accept", "*/*");

			FileEntity entity = new FileEntity(tempfile, "binary/octet-stream");

			putFileRequest.setEntity(entity);

			HttpResponse containerResponse = client.execute(putFileRequest);

			System.out.println(new BasicResponseHandler().handleResponse(containerResponse));

			int response_code = containerResponse.getStatusLine().getStatusCode();
			

			if (response_code == 201) {
				String respose_status = containerResponse.getStatusLine().getReasonPhrase().toString();
				Header[] etag = containerResponse.getAllHeaders();
				for(int i=0; i<etag.length; i++) {
					if(etag[i].getName().equalsIgnoreCase("Etag")) {
						e_tag = etag[i].getValue();
					}
				}
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
		return e_tag;

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
		LargeUploadObjects blk = new LargeUploadObjects();
		blk.bulkUploadFile("TestBulkUpload.tar.gz");
		;
	}

}
