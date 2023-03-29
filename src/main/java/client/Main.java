package client;

import java.io.IOException;
import java.net.URL;
import api.JSONPayload;

public class Main {
    public static void main(String[] args) throws IOException {
        JSONPayload jsonPayload = new JSONPayload();
        jsonPayload.getSettings().setRotorsReflectorCounter(1,2,3,1,"AAA");
        String serverUrl = "http://127.0.0.1:8080/enigma/v1/api";
        ApiClient apiClient = new ApiClient(serverUrl);
        System.out.println(apiClient.getMessage(1));
        System.out.println(apiClient.decryptMessage(jsonPayload,1));



    }

}
