package api;

import com.google.gson.Gson;
import database.DatabaseConnector;
import jakarta.ws.rs.core.*;

import javax.naming.NamingException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {
    private Connection testConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/test",
                "enigmaAPI", "password123"
        );
        return connection;
    }
    @Test
    void getInvalidMessageId() throws SQLException, NamingException {
        Api api = new Api(new DatabaseConnector(testConnection()));
        Response response = api.getMessage(-1);
        assertEquals(404,response.getStatus());
    }
    @Test
    void getValidMessageId() throws SQLException, NamingException {
        Api api = new Api(new DatabaseConnector(testConnection()));
        Response response = api.getMessage(1);
        assertEquals(200,response.getStatus());
    }
    @Test
    void decryptionOfMessage() throws SQLException, NamingException {
        Api api = new Api(new DatabaseConnector(testConnection()));

        JSONPayload jsonPayload = new JSONPayload();
        jsonPayload.getSettings().setRotorsReflectorCounter(1,2,3,1,"AAA");
        Gson gson = new Gson();

       Response response = api.decryptionPost(1,gson.toJson(jsonPayload));
       JSONPayload decryptedJson = gson.fromJson((String) response.getEntity(),JSONPayload.class);
       assertEquals("TEISISANENGIMAMACHINEJETHINKS", decryptedJson.getMessage().getPlainText());
    }

}
