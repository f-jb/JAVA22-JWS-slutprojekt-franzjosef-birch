package api;

import api.Api;
import hthurow.tomcatjndi.TomcatJNDI;
import jakarta.ws.rs.core.Response;
import javax.naming.NamingException;
import java.io.File;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiTest {
    @BeforeEach
    void setup(){
        TomcatJNDI tomcatJNDI = new TomcatJNDI();
        tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
        tomcatJNDI.start();
    }
    @Test
    void invalidMessageId() throws SQLException, NamingException {
        Api api = new Api();
        Response response = api.getMessage(-1);
        assertEquals(404,response.getStatus());
    }

}
