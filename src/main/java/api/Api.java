package api;

import database.DatabaseConnector;
import enigma.Engine;
import jakarta.ws.rs.*;
import com.google.gson.Gson;
import jakarta.ws.rs.core.*;

import javax.naming.NamingException;
import java.net.MalformedURLException;
import java.sql.*;

@Path("/v1/api")
@Produces("application/json")
public class Api{
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(@PathParam("id") int messageId) throws SQLException, NamingException {
        Gson gson = new Gson();
        JSONPayload jsonPayload = new JSONPayload();
        DatabaseConnector databaseConnector = new DatabaseConnector();
        String message = databaseConnector.getMessage(messageId);
        jsonPayload.getMessage().setEncryptedText(databaseConnector.getMessage(messageId));
        jsonPayload.getMessage().setMessageId(messageId);
        if(message.equals("")) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            jsonPayload.getMessage().setEncryptedText(databaseConnector.getMessage(messageId));
            jsonPayload.getMessage().setMessageId(messageId);
           return Response.status(Response.Status.OK).entity(gson.toJson(jsonPayload)).build();
        }
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response decryptionPost(@PathParam("id") int messageId, String payload) throws SQLException, MalformedURLException, NamingException {
        Gson gson = new Gson();
        JSONPayload jsonPayload = gson.fromJson(payload, JSONPayload.class);
        DatabaseConnector databaseConnector = new DatabaseConnector();
        jsonPayload.getMessage().setEncryptedText(databaseConnector.getMessage(messageId));
        jsonPayload.getMessage().setMessageId(messageId);

        Engine engine = new Engine(jsonPayload.getRotorsAndReflector(), jsonPayload.getCounterSettings());
        String plainText = engine.encode(jsonPayload.getMessage().getEncryptedText());
        jsonPayload.getMessage().setPlainText(plainText);

        return Response.status(Response.Status.OK).entity(gson.toJson(jsonPayload)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(String payload, @Context UriInfo uriInfo) throws SQLException, MalformedURLException, NamingException {
        Gson gson = new Gson();
        JSONPayload jsonPayload = gson.fromJson(payload, JSONPayload.class);

        Engine engine = new Engine(jsonPayload.getRotorsAndReflector(), jsonPayload.getCounterSettings());
        String encryptedMessage = engine.encode(jsonPayload.getMessage().getPlainText());

        jsonPayload.getMessage().setPlainText("");
        DatabaseConnector databaseConnector = new DatabaseConnector();
        int messageId = databaseConnector.insertMessage(encryptedMessage);

        jsonPayload.getMessage().setEncryptedText(encryptedMessage);
        jsonPayload.getMessage().setMessageId(messageId);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Integer.toString(messageId));
        return Response.created(uriBuilder.build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id")int messageId) throws SQLException, NamingException {
        DatabaseConnector databaseConnector = new DatabaseConnector();

        // Returns 204 if successful otherwise 404
        int returnCode = databaseConnector.deleteMessage(messageId);
        return Response.status(returnCode).build();
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void readMessage(@PathParam("id") int messageId) throws SQLException {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.messageRead(messageId);
    }
}