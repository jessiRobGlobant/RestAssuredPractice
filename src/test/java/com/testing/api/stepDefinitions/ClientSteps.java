package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ClientSteps{
    private final ClientRequest clientRequest = new ClientRequest();
    private Client client;

    // Given
    @Given("there are registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem() {
        CommonSteps.setResponse(clientRequest.getClients());
        CommonSteps.logInfo(CommonSteps.getResponse().jsonPath().prettify());
        Assert.assertEquals(CommonSteps.getResponse().statusCode(), 200);

        List<Client> clientList = clientRequest.getClientsEntity(CommonSteps.getResponse());
        // If not registered at least 3 users, create them
        for (int i = clientList.size();i<3; i++){
            CommonSteps.setResponse(clientRequest.createDefaultClient());
            CommonSteps.logInfo("Added client with status code "+ CommonSteps.getResponse().statusCode());
            CommonSteps.logInfo(CommonSteps.getResponse().jsonPath().prettify());
            Assert.assertEquals(CommonSteps.getResponse().statusCode(), 201);
        }
        CommonSteps.logInfo("There are registered clients in the system");
    }

    @Given("I have a client with the following details:")
    public void iHaveAClientWithTheFollowingDetails(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps().get(0);
        client = Client.builder()
                .name(clientDataMap.get("Name"))
                .lastName(clientDataMap.get("LastName"))
                .country(clientDataMap.get("Country"))
                .city(clientDataMap.get("City"))
                .email(clientDataMap.get("Email"))
                .phone(clientDataMap.get("Phone"))
                .build();

        CommonSteps.logInfo("Client wrapped:" + client);
    }

    // When
    @When("I send a GET request to view all the clients")
    public void iSendAGETRequestToViewAllTheClient() {
        CommonSteps.setResponse(clientRequest.getClients());
        CommonSteps.logInfo("Sent a GET request to view all the clients");
    }

    @When("I send a POST request to create a client")
    public void iSendAPOSTRequestToCreateAClient() {
        CommonSteps.setResponse(clientRequest.createClient(client));
        CommonSteps.logInfo("Sent a POST request to create a client");
    }

    @When("I send a DELETE request to delete the client with ID {string}")
    public void iSendADELETERequestToDeleteTheClientWithID(String clientId) {
        CommonSteps.logInfo("Sent a DELETE request to delete the client with ID " + clientId);
    }

    // Then
    @Then("the response should include the details of the created client")
    public void theResponseShouldIncludeTheDetailsOfTheCreatedClient() {
        Client new_client = clientRequest.getClientEntity(CommonSteps.getResponse());
        new_client.setId(null);
        Assert.assertEquals(client, new_client);
        CommonSteps.logInfo("The response includes the details of the created client");
    }

    @Then("validates the response with the client JSON schema")
    public void userValidatesResponseWithTheClientJSONSchema() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(CommonSteps.getResponse(), path));
        CommonSteps.logInfo("Successfully validated the response with client JSON schema");
    }

    @Then("validates the response with the client list JSON schema")
    public void userValidatesResponseWithTheClientListJSONSchema() {
        String path = "schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(CommonSteps.getResponse(), path));
        CommonSteps.logInfo("Successfully validated the response with client list JSON schema");
    }

}
