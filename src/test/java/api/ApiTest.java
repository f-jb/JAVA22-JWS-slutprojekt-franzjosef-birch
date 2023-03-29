package api;

import com.google.gson.Gson;
import jakarta.ws.rs.core.*;

import javax.naming.NamingException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {
    @Test
    void getInvalidMessageId() throws SQLException, NamingException {
        Api api = new Api();
        Response response = api.getMessage(-1);
        assertEquals(404,response.getStatus());
    }
    @Test
    void getValidMessageId() throws SQLException, NamingException {
        Api api = new Api();
        Response response = api.getMessage(1);
        assertEquals(200,response.getStatus());
    }
    @Test
    void decryptionOfMessage() throws SQLException, NamingException {

        JSONPayload jsonPayload = new JSONPayload();
        jsonPayload.getSettings().setRotorsReflectorCounter(1,2,3,1,"AAA");
        Gson gson = new Gson();
        Api api = new Api();

       Response response = api.decryptionPost(1,gson.toJson(jsonPayload));
       JSONPayload decryptedJson = gson.fromJson((String) response.getEntity(),JSONPayload.class);
       assertEquals("TEISISANENGIMAMACHINEJETHINKS", decryptedJson.getMessage().getPlainText());
    }

}
