# Requirements

This is what **I believe** is required. Not sure though.
- Maven for building. Build package for .war
- Tomee for deployment
- MariaDB for database connection
    - User enigmaAPI
    - Password password123
    - Table named test
        - message_id (int autoincrement)
        - message (text)
        - message_read (boolean)
        - created_at (timestamp)

# Testing

Run mvn test in the folder.

# Deployement

## Server

1. Copy the enigmaAPI.war file from maven target to tomee webapp-folder.
2. Start tomee
3. Endpoint is {Host}/engima/v1/api

## Client

### Console client

Just run main.java in the client package.

### Other clients

#### Getting the template

A GET request to the APIs endpoint provides the template for the POST.

#### Encrypting a message

A POST request to the endpoint with a json-file based on the template e.g. "curl --json @message.json 127.0.0.1:8080/enigma/v1/api" posts the plaintextmessage encrypted according to the chosen settings. The request returns the location of the message within the header.

#### Decrypting a message

A POST request with a json-file to the location of the message returns the message decrypted according to the chosen settings.

#### Deleting a message

A DELETE request to the location of a message deletes the message.

#### Marking a message as read

A PATCH request to the location of a message marks the message as read.
