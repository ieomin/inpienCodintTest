package org.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Parser {
    public static String getParsedString(String url, String name, String phoneNumber, String eMail) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String body = "{\"NAME\": \"" + name + "\",\"PHONE_NUMBER\": \"" + phoneNumber + "\",\"E_MAIL\": \"" + eMail + "\"}";
        OutputStream os = con.getOutputStream();
        os.write(body.getBytes("UTF-8"));
        os.flush();
        os.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer res = new StringBuffer();
        while ((inputLine = in.readLine()) != null) res.append(inputLine);
        in.close();
        return res.toString();
    }
}
