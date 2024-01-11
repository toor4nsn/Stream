package com.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestTest {
    public static void main(String[] args) throws Exception {
        // Create URL 
        URL url = new URL("https://api.deeplx.org/translate");

        // Create connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the properties of the request
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        // Create the request body
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("text", "沉浸式的学习");
        requestBody.put("source_lang", "ZH");
        requestBody.put( "target_lang", "EN");

        // Convert the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(requestBody);

        // Write the request body
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);           
        }

        // Get the response
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code :  " + responseCode);

        // Read the response
        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response :  " + response.toString());

            // Parse the response
            Map<String, Object> jsonResponse = objectMapper.readValue(response.toString(), new TypeReference<Map<String, Object>>(){});
            String data = (String) jsonResponse.get("data");
            Object alternatives = jsonResponse.get("alternatives");

            System.out.println("Data : " + data);
            System.out.println("Alternatives : " + alternatives);
        }
    }
}
