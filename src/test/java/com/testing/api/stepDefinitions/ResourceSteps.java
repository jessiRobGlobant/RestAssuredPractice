package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.models.Resource;
import com.testing.api.requests.ClientRequest;
import com.testing.api.requests.ResourceRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Response response;
    private Resource resource;

    // Given
    @Given("there are registered resources in the system")
    public void thereAreRegisteredResourcesInTheSystem() {
        response = resourceRequest.getResources();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(response.statusCode(), 200);

        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        // If not registered at least 3 users, create them
        for (int i = resourceList.size();i<3; i++){
            response = resourceRequest.createDefaultResource();
            logger.info(response.statusCode());
            Assert.assertEquals(response.statusCode(), 201);
        }
        logger.info("there are registered resources in the system");
    }

    // When
    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
        logger.info("I send a GET request to view all the resources");
    }

    // Then
    @Then("the resource response should have a status code of {int}")
    public void theResourceResponseShouldHaveAStatusCodeOf(int statusCode) {
        logger.info("the response should have a status code of " + statusCode);
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("validates the response with the resource list JSON schema")
    public void userValidatesResponseWithTheResourceListJSONSchema() {
        logger.info("validates the response with client list JSON schema");
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully validated the response with client list JSON schema");
    }

}
