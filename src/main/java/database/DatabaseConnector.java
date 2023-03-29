package database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.sql.*;

public class DatabaseConnector {
    private Connection connect() throws NamingException, SQLException {
        // Get the Database settings from JNDI in context
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:/comp/env/jdbc/mariadb");
        Connection connection = dataSource.getConnection();
        return connection;

    }
    public String getMessage(int messageId) throws SQLException, NamingException {
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT message FROM messages WHERE message_id = (?);");
        preparedStatement.setInt(1,messageId);

        preparedStatement.executeQuery();
        ResultSet resultSet = preparedStatement.getResultSet();
        connection.close();
        if(resultSet.next()) {
            return resultSet.getString("message");
        }
        return "";

    }

    public int insertMessage(String message) throws SQLException, NamingException {
        Connection connection = connect();

        // Prepares the SQL-statement and we want it to return the message_id of the inserted message
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages (message) VALUES (?);",Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,message);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        connection.close();

        // Return the ID of the message
        return resultSet.getInt("insert_id");
    }
    public int deleteMessage(int messageId) throws SQLException, NamingException {
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages WHERE message_id = (?);");
        preparedStatement.setInt(1,messageId);

        if(preparedStatement.executeUpdate() == 0) {
            connection.close();
            return 404;
        }
        return 204;


    }

    public void messageRead(int messageId) {

    }
}
