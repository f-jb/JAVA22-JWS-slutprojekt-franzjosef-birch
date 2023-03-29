package api;

import com.google.gson.Gson;
import database.DatabaseConnector;

import javax.naming.NamingException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, MalformedURLException, NamingException {
        JSONPayload jsonPayload = new JSONPayload();
        Gson gson = new Gson();
        System.out.println(gson.toJson(jsonPayload));
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.getMessage(23);






    }
}
