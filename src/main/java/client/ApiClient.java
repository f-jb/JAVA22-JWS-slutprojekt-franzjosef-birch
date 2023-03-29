package client;

import api.JSONPayload;
import com.google.gson.Gson;
import jakarta.ws.rs.core.MediaType;

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
        this.baseurl = baseurl;
    }

    public String getMessage(int messageId) throws IOException {
        // appends the message ID and sends a get request
        URL serverUrl = new URL(baseurl + "/" + messageId);
        HttpURLConnection apiconnection = (HttpURLConnection) serverUrl.openConnection();
        apiconnection.setRequestMethod("GET");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.connect();

        // Return an empty string on a 404 response
        if (apiconnection.getResponseCode() == 404) {
            return "";
        }

        // reads the response
        String response = responseToString(apiconnection);

        // converts the jsonobject-as-string to a javaobject that we can manipulate
        Gson gson = new Gson();
        JSONPayload jsonPayload = gson.fromJson(response, JSONPayload.class);
        return jsonPayload.getMessage().getEncryptedText();
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
        sendPayloadToServer(apiconnection,new Gson().toJson(jsonPayload));

        // Send the request to the server
        apiconnection.connect();

        // Translate the response to String
        String response = responseToString(apiconnection);

            Gson gson = new Gson();
            JSONPayload decryptedJson = gson.fromJson(response, JSONPayload.class);
            return decryptedJson.getMessage().getPlainText();
        }

    boolean messageRead(int messageId) throws IOException {
        URL serverUrl = new URL(baseurl + "/" + messageId);
        HttpURLConnection apiconnection = (HttpURLConnection) serverUrl.openConnection();
        apiconnection.setRequestMethod("PATCH");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.connect();
        return apiconnection.getResponseCode() == 200;

    }

    boolean deleteMessage(int messageId) throws IOException {
        URL serverUrl = new URL(baseurl + "/" + messageId);
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
        // stops redirection so we can get the created resource
        apiconnection.setInstanceFollowRedirects(false);

        // Translate json to data for post
        sendPayloadToServer(apiconnection,new Gson().toJson(jsonPayload));

        // Send the request to the server
        apiconnection.connect();

        // Returns the URI of the created message
        return apiconnection.getHeaderField("Location");

    }

    String responseToString(HttpURLConnection httpURLConnection) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    void sendPayloadToServer(HttpURLConnection httpURLConnection, String payload) {
        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input =payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}