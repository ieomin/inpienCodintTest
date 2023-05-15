package org.example.service;

import org.json.JSONObject;
import java.nio.charset.Charset;
import java.util.Base64;

public class Separator {

    public static String getXmlString(String parsedString){
        JSONObject jsonObject = new JSONObject(parsedString);
        String xmlData = jsonObject.getString("XML_DATA");
        byte[] decodedBytes = Base64.getDecoder().decode(xmlData);
        String xmlString = new String(decodedBytes, Charset.forName("EUC-KR"));
        return xmlString;
    }

    public static String getJsonString(String parsedString){
        JSONObject jsonObject = new JSONObject(parsedString);
        String jsonData = jsonObject.getString("JSON_DATA");
        byte[] decodedBytes = Base64.getDecoder().decode(jsonData);
        String jsonString = new String(decodedBytes, Charset.forName("EUC-KR"));
        return jsonString;
    }
}
