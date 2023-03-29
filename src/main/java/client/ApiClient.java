package client;

import com.google.gson.Gson;
import jakarta.ws.rs.core.MediaType;
import api.JSONPayload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private String baseurl;

    public ApiClient(String baseurl) {
        this.baseurl= baseurl;
    }

    public String getMessage(int messageId) throws IOException {
        URL serverUrl = new URL(baseurl+"/" +messageId);
        HttpURLConnection apiconnection = (HttpURLConnection) serverUrl.openConnection();
        apiconnection.setRequestMethod("GET");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.connect();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(apiconnection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            Gson gson = new Gson();
            JSONPayload jsonPayload = gson.fromJson(response.toString(), JSONPayload.class);
            return jsonPayload.getMessage().getEncryptedText();
        }


    }
    String decryptMessage(JSONPayload jsonPayload, int messageId) throws IOException {
        // creates connection and sets properties to JSON and method to POST and enables attaching payload
        URL serverUrl = new URL(baseurl + "/" + messageId);
        HttpURLConnection apiconnection = (HttpURLConnection) serverUrl.openConnection();
        apiconnection.setRequestMethod("POST");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.setDoOutput(true);

        // Translate json to data for post
        try (OutputStream os = apiconnection.getOutputStream()) {
            Gson gson = new Gson();
            byte[] input = gson.toJson(jsonPayload).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Send the request to the server
        apiconnection.connect();

        // Translate the response to String
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(apiconnection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Gson gson = new Gson();
            JSONPayload decryptedJson = gson.fromJson(response.toString(), JSONPayload.class);
            return decryptedJson.getMessage().getPlainText();
        }
    }
    boolean messageRead(int messageId) throws IOException {
        URL serverUrl = new URL(baseurl+"/" +messageId);
        HttpURLConnection apiconnection = (HttpURLConnection) serverUrl.openConnection();
        apiconnection.setRequestMethod("PATCH");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.connect();
        return apiconnection.getResponseCode() == 200;

    }
    boolean deleteMessage(int messageId) throws IOException {
        URL serverUrl = new URL(baseurl+"/" +messageId);
        HttpURLConnection apiconnection = (HttpURLConnection) serverUrl.openConnection();
        apiconnection.setRequestMethod("DELETE");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.connect();
        return apiconnection.getResponseCode() == 200;

    }
    public String postMessage(JSONPayload jsonPayload) throws IOException {
        // creates connection and sets properties to JSON and method to POST and enables attaching payload
        URL serverUrl = new URL(baseurl);
        HttpURLConnection apiconnection = (HttpURLConnection) serverUrl.openConnection();
        apiconnection.setRequestMethod("POST");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.setDoOutput(true);

        // Translate json to data for post
        try (OutputStream os = apiconnection.getOutputStream()) {
            Gson gson = new Gson();
            byte[] input = gson.toJson(jsonPayload).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Send the request to the server
        apiconnection.connect();

        // Translate the response to String
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(apiconnection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            return response.toString();
        }
    }
}
