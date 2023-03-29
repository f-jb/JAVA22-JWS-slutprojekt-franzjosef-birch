package client;

import java.io.IOException;
import java.net.URL;
import api.JSONPayload;

public class Main {
    public static void main(String[] args) throws IOException {
        JSONPayload jsonPayload = new JSONPayload();
        URL baseurl = new URL("http://127.0.0.1:8080/enigma/v1/api");
        ApiClient apiClient = new ApiClient(baseurl);
        apiClient.setPayload(jsonPayload);
        apiClient.getData();


    }

}
