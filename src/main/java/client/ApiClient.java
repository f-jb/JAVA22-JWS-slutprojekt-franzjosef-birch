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
    private URL server;
    private String jsonPayload;

    public ApiClient(URL server) {
        this.server = server;
    }

    void setPayload(JSONPayload jsonPayload) {
        Gson gson = new Gson();
        this.jsonPayload = gson.toJson(jsonPayload);
    }

    public String getData() throws IOException {
        // creates connection and sets properties to JSON and method to POST and enables attaching payload
        HttpURLConnection apiconnection = (HttpURLConnection) server.openConnection();
        apiconnection.setRequestMethod("POST");
        apiconnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
        apiconnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        apiconnection.setDoOutput(true);

        // Translate json to data for post
        try (OutputStream os = apiconnection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
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
