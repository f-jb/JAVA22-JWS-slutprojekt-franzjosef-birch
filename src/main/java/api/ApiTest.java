package api;

import com.google.gson.Gson;
import org.junit.Test;


class ApiTest {
    @Test
    void objectToJson(){
        String jsonString = "{\"data\":{\"settings\":{\"leftRotor\":1,\"middleRotor\":2,\"rightRotor\":3,\"reflector\":1,\"counterSettings\":\"AAA\"},\"message\":\"This is an enigma machine\"}}";
        Gson gson = new Gson();
    }

}
