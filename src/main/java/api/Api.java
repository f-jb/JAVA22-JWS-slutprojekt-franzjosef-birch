package api;

import com.google.gson.Gson;
import database.DatabaseConnector;
import enigma.Engine;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import javax.naming.NamingException;
import java.sql.SQLException;

@Path("/v1/api")
@Produces("application/json")
public class Api {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(@PathParam("id") int messageId) throws SQLException, NamingException {
        // Connects to the database and gets the message. Returns "" if not found.
        DatabaseConnector databaseConnector = new DatabaseConnector();
        String message = databaseConnector.getMessage(messageId);

        // Checks if the supplied messageId has a valid entry
        if (message.equals("")) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {

            // Inits the JSONPayload object that will be sent back with the stored message in encryptedText
            Gson gson = new Gson();
            JSONPayload jsonPayload = new JSONPayload();
            jsonPayload.getMessage().setEncryptedText(databaseConnector.getMessage(messageId));
            jsonPayload.getMessage().setMessageId(messageId);
            return Response.status(Response.Status.OK).entity(gson.toJson(jsonPayload)).build();
        }
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response decryptionPost(@PathParam("id") int messageId, String payload) throws SQLException, NamingException {

        DatabaseConnector databaseConnector = new DatabaseConnector();
        String message = databaseConnector.getMessage(messageId);

        if (message.equals("")) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            Gson gson = new Gson();
            JSONPayload jsonPayload = gson.fromJson(payload, JSONPayload.class);

            // Decrypts the message
            Engine engine = new Engine(jsonPayload.getRotorsAndReflector(), jsonPayload.getCounterSettings());
            String plainText = engine.encode(jsonPayload.getMessage().getEncryptedText());

            // sets the plainText, encryptedText and messageID
            jsonPayload.getMessage().setPlainText(plainText);
            jsonPayload.getMessage().setEncryptedText(databaseConnector.getMessage(messageId));
            jsonPayload.getMessage().setMessageId(messageId);

            return Response.status(Response.Status.OK).entity(gson.toJson(jsonPayload)).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(String payload, @Context UriInfo uriInfo) throws SQLException, NamingException {
        Gson gson = new Gson();
        JSONPayload jsonPayload = gson.fromJson(payload, JSONPayload.class);

        // Encrypts the plainText message
        Engine engine = new Engine(jsonPayload.getRotorsAndReflector(), jsonPayload.getCounterSettings());
        String encryptedMessage = engine.encode(jsonPayload.getMessage().getPlainText());

        // Sends the encryptedMessage to the Database
        DatabaseConnector databaseConnector = new DatabaseConnector();
        int messageId = databaseConnector.insertMessage(encryptedMessage);

        // removes the plainText and sets encryptedText and messageId
        jsonPayload.getMessage().setPlainText("");
        jsonPayload.getMessage().setEncryptedText(encryptedMessage);
        jsonPayload.getMessage().setMessageId(messageId);

        // Builds and returns the URL of the message
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Integer.toString(messageId));
        return Response.created(uriBuilder.build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int messageId) throws SQLException, NamingException {
        DatabaseConnector databaseConnector = new DatabaseConnector();

        // Returns 204 if successful otherwise 404
        int returnCode = databaseConnector.deleteMessage(messageId);
        return Response.status(returnCode).build();
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readMessage(@PathParam("id") int messageId) throws SQLException, NamingException {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        int returnCode = databaseConnector.messageRead(messageId);
        return Response.status(returnCode).build();
    }
}