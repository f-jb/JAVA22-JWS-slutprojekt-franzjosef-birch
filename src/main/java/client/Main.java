package client;

import java.io.IOException;
import java.util.Scanner;

import api.JSONPayload;
import com.google.gson.Gson;

public class Main {
    static String serverUrl = "http://127.0.0.1:8080/enigma/v1/api";
    static ApiClient apiClient = new ApiClient(serverUrl);
    public static void main(String[] args) throws IOException {
        Boolean menuloop = true;

        while (menuloop){
            System.out.println("""
                    Hello, please make your selection:
                    1. Get a message
                    2. Decode a message
                    3. Post a message
                    4. Mark a message as read
                    5. Delete a message
                    0. Exit
                    """);
        int choice = UserInput.getInt();
            switch (choice) {
                case 1 -> getAMessage();
                case 2 -> decodeAMessage();
                case 3 -> postAMessage();
                case 4 -> markMessageAsRead();
                case 5 -> deleteMessage();
                case 0 -> menuloop = false;
                default -> System.out.println("Please choose a valid number.");
            }

        }
    }
    static void deleteMessage() throws IOException {
        System.out.println("Please enter the ID of the message");
        int messageId = UserInput.getInt();
        if( apiClient.deleteMessage(messageId)){
            System.out.println("The message was deleted");
        } else {
            System.out.println("The message was not found");
        }
    }
    static void markMessageAsRead() throws IOException {
        System.out.println("Please enter the ID of the message");
        int messageId = UserInput.getInt();
        if( apiClient.messageRead(messageId)){
            System.out.println("The message was marked as read");
        } else {
            System.out.println("The message was not found");
        }
    }
    static void decodeAMessage() throws IOException {
       JSONPayload jsonPayload = prepareAJsonPayload();
        System.out.println("Please enter the ID of the message you wish to decode");
        int messageId = UserInput.getInt();
        String message = apiClient.decryptMessage(jsonPayload,messageId);
        if(message.isEmpty()){
            System.out.println("The message was not found");
        } else {
            System.out.println("The message is " + message);
        }
    }
    static void getAMessage() throws IOException {
        System.out.println("Please enter the ID of the message");
        int messageId = UserInput.getInt();
        String returnedMessage = apiClient.getMessage(messageId);
        if(returnedMessage.isEmpty()){
            System.out.println("Message not found");
        } else {
            System.out.println("Here is your message");
            System.out.println(returnedMessage);
        }
    }
    static void postAMessage() throws IOException {
        JSONPayload jsonPayload = prepareAJsonPayload();
        System.out.println("Please enter the message [A-Z]:");
        Scanner scanner = new Scanner(System.in);
        String plainText = scanner.nextLine();
        jsonPayload.getMessage().setPlainText(plainText);
        String location = apiClient.postMessage(jsonPayload);
        System.out.println("Your message is located at: " + location);
    }
    static JSONPayload prepareAJsonPayload(){
        System.out.println("Please enter left rotor (1-3):");
        int leftRotor = UserInput.getInt(1,3);
        System.out.println("Please enter middle rotor (1-3):");
        int middleRotor= UserInput.getInt(1,3);
        System.out.println("Please enter right rotor (1-3):");
        int rightRotor= UserInput.getInt(1,3);
        System.out.println("Please enter the counter settings ([AAA-ZZZ]):");
        StringBuilder counters = new StringBuilder();
        counters.append(UserInput.getChar());
        counters.append(UserInput.getChar());
        counters.append(UserInput.getChar());
        JSONPayload jsonPayload = new JSONPayload(leftRotor,middleRotor,rightRotor,1,counters.toString());
        System.out.println(new Gson().toJson(jsonPayload));
        return jsonPayload;
    }
}