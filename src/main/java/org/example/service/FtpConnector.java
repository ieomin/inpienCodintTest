package org.example.service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FtpConnector {

    private static final String HOST = "211.106.171.36";
    private static final String PORT = "20421";
    private static final String USER = "inspien01";
    private static final String PASSWORD = "inspien01";
    private static final String FILE_PATH = "/";

    public static String getFile(String jsonString, String name) {
        JSONArray recordArray = new JSONObject(jsonString).getJSONArray("record");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recordArray.length(); i++) {
            JSONObject record = recordArray.getJSONObject(i);
            sb.append(record.getString("Names"));
            sb.append("^");
            sb.append(record.getString("Phone"));
            sb.append("^");
            sb.append(record.getString("Email"));
            sb.append("^");
            sb.append(record.getString("BirthDate"));
            sb.append("^");
            sb.append(record.getString("Company"));
            sb.append("^");
            sb.append(record.getString("PersonalNumber"));
            sb.append("^");
            sb.append(record.getString("OrganisationNumber"));
            sb.append("^");
            sb.append(record.getString("Country"));
            sb.append("^");
            sb.append(record.getString("Region"));
            sb.append("^");
            sb.append(record.getString("City"));
            sb.append("^");
            sb.append(record.getString("Street"));
            sb.append("^");
            try{
                sb.append(record.getString("ZipCode"));
            } catch (JSONException e){
                sb.append(record.getBigInteger("ZipCode"));
            }
            sb.append("^");
            try{
                sb.append(record.getString("CreditCard"));
            } catch (JSONException e){
                sb.append(record.getBigInteger("CreditCard"));
            }
            sb.append("^");
            sb.append(record.getString("GUID"));
            sb.append("\n");
        }
        String fileName = "INSPIEN_JSON_" + name + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static void insert(String fileName) throws IOException {
        OutputStream outputStream = new URL("ftp://" + USER + ":" + PASSWORD + "@" + HOST + ":" + Integer.parseInt(PORT) + FILE_PATH + "/" + fileName).openConnection().getOutputStream();
        FileInputStream inputStream = new FileInputStream(fileName);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
    }
}
