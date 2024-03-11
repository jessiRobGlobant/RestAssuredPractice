package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Client;
import com.testing.api.utils.Constants;
import com.testing.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientRequest extends BaseRequest {
    private String endpoint;

    /**
     * This function gets all the clients from the API
     * @return Response
     */
    public Response getClients() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * This function gets a client from the API by its id
     * @param clientId resource id
     * @return Response
     */
    public Response getClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * This function creates a new client in the API
     * @param client client object
     * @return response
     */
    public Response createClient(Client client) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestPost(endpoint, createBaseHeaders(), client);
    }

    /**
     * This function updates a client information in the API
     * @param client client object
     * @param clientId client id of the object to change
     * @return response
     */
    public Response updateClient(Client client, String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestPut(endpoint, createBaseHeaders(), client);
    }

    /**
     * This function deletes a client from the API by its id
     * @param clientId client id of the object to change
     * @return response
     */
    public Response deleteClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    /**
     * This function gets the response es a client Entity
     * @param response response
     * @return Client
     */
    public Client getClientEntity(@NotNull Response response) {
        return response.as(Client.class);
    }

    /**
     * This function gets a client list based on a response
     * of clients
     * @param response response
     * @return list of clients
     */
    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }

    /**
     * This function creates a default client on the API
     * based on a json file
     * @return response
     */
    public Response createDefaultClient() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createClient(jsonFile.getClientByJson(Constants.DEFAULT_CLIENT_FILE_PATH));
    }

    /**
     * This function creates a client entity based on a
     * Json
     * @param clientJson Client as a Json object
     * @return resource
     */
    public Client getClientEntity(String clientJson) {
        Gson gson = new Gson();
        return gson.fromJson(clientJson, Client.class);
    }

    /**
     * This function validates the schema of a response based
     * on a schema defined for clients in a Json file
     * @param response resource
     * @param schemaPath path of the schema in Json
     * @return boolean
     */
    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true; // Return true if the assertion passes
        } catch (AssertionError e) {
            // Assertion failed, return false
            return false;
        }
    }
}
