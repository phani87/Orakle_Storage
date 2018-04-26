package com.oracle.storage.helper;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import com.oracle.storage.LargeUploadObjects;
import com.oracle.storage.rest.pojo.LargeFileManifestObjects;

public class FileSplitter {
	List<LargeFileManifestObjects> manifest;

	LargeUploadObjects largeUploadObjects;

	public void splitFile(File f) throws IOException {
		int partCounter = 1;// I like to name parts from 001, 002, 003, ...
							// you can change it to 0 if you want 000, 001, ...

		int sizeOfFiles = 1024 * 1024;// 1MB
		byte[] buffer = new byte[sizeOfFiles];

		String fileName = f.getName();

		// try-with-resources to ensure closing stream
		try (FileInputStream fis = new FileInputStream(f); BufferedInputStream bis = new BufferedInputStream(fis)) {

			int bytesAmount = 0;
			while ((bytesAmount = bis.read(buffer)) > 0) {
				// write each chunk of data into separate file with different number in name
				String filePartName = String.format("%s.%03d-Split", fileName, partCounter++);
				String tempFolderName = f.getName(); 
				if (tempFolderName.indexOf(".") > 0)
					tempFolderName = tempFolderName.substring(0, tempFolderName.lastIndexOf("."));
				String tempDirPath = f.getParent()+"/"+tempFolderName;
				File tempDir = new File(tempDirPath);
				if(! tempDir.exists()) tempDir.mkdirs();
				File newFile = new File(tempDir, filePartName);
				try (FileOutputStream out = new FileOutputStream(newFile)) {
					out.write(buffer, 0, bytesAmount);
				}
			}
		}
	}

	public String readAllFiles(String containeName, String tempFolderPath) {
		File folder = new File(tempFolderPath);
		File[] listOfFiles = folder.listFiles();
		manifest = new ArrayList<>();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				if (file.getName().contains("Split")) {
					largeUploadObjects = new LargeUploadObjects();
					LargeFileManifestObjects objects = new LargeFileManifestObjects();
					objects.setPath("testPhani/" + file.getName());
					objects.setEtags(largeUploadObjects.largeUploadObjectImpl("testPhani", file.getName().toString(),
							file.getAbsolutePath().toString()));
					objects.setSize_bytes(file.length());
					manifest.add(objects);
				}
			}
		}
		createManifestFileLO(manifest, containeName, tempFolderPath);

		StringBuffer result = new StringBuffer();
		result.append("{\n");

		result.append("\"created_response\" :\"Uploded Large File\",\n");
		result.append("\"created_code\" :\"205\"}");

		return result.toString();

	}

	private void createManifestFileLO(List<LargeFileManifestObjects> manifestElements, String containerName, String tempfolderName) {

		JSONArray array = new JSONArray();
		for (LargeFileManifestObjects manifestObject : manifestElements) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("path", manifestObject.getPath());
			jsonObject.put("etag", manifestObject.getEtags());
			jsonObject.put("size_bytes", manifestObject.getSize_bytes());
			array.put(jsonObject);
		}

		FileWriter fw;
		try {
			fw = new FileWriter(tempfolderName+"/manifest.json");
			array.write(fw);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder(5);
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		output = output+".manitest";
		System.out.println(output);
		 
		largeUploadObjects.largeUploadManifestImpl(containerName, output, tempfolderName+"/manifest.json");
		System.out.println(array);
	}

	public static void main(String[] args) throws IOException {
		FileSplitter fs = new FileSplitter();
		fs.splitFile(new File("D:\\test\\NodeJS.zip"));
		//fs.readAllFiles(null, null);
	}
}
