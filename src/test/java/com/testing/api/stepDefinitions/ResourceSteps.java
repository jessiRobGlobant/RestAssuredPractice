package com.testing.api.stepDefinitions;

import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

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

    @When("I retrieve the details of the latest resource")
    public void iRetrieveTheDetailsOfTheLatestResource() {
        // Get the last resource id
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        resource = resourceList.get(resourceList.size()-1);
        String lastId = resource.getId();

        // Get the last resource
        response = resourceRequest.getResource(lastId);
        logger.info("Retrieved the details of the latest resource");
        logger.info(response.jsonPath().prettify());
        logger.info("The status code is " + response.statusCode());
    }

    @When("I send a PUT request to update the latest resource")
    public void iSendAPUTRequestToUpdateTheLatestResource(String requestBody) {
        String lastId = resource.getId();
        resource = resourceRequest.getResourceEntity(requestBody);
        response = resourceRequest.updateResource(resource, lastId);
        logger.info("Updated last id: "+lastId);
    }

    // Then
    @Then("the resource response should have a status code of {int}")
    public void theResourceResponseShouldHaveAStatusCodeOf(int statusCode) {
        logger.info("the response should have a status code of " + statusCode);
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        resource = resourceRequest.getResourceEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps().get(0);

        Assert.assertEquals(expectedDataMap.get("name"), resource.getName());
        Assert.assertEquals(expectedDataMap.get("trademark"), resource.getTrademark());
        Assert.assertEquals(expectedDataMap.get("stock"),String.valueOf(resource.getStock()));
        Assert.assertEquals(expectedDataMap.get("price"), String.valueOf(resource.getPrice()));
        Assert.assertEquals(expectedDataMap.get("description"), resource.getDescription());
        Assert.assertEquals(expectedDataMap.get("tags"), resource.getTags());
        Assert.assertEquals(expectedDataMap.get("active"), String.valueOf(resource.getActive()));
        logger.info("the response should has the following details:" + expectedData);
    }

    @Then("validates the response with the resource list JSON schema")
    public void userValidatesResponseWithTheResourceListJSONSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully validated the response with client list JSON schema");
    }

    @Then("validates the response with the resource JSON schema")
    public void validatesTheResponseWithTheResourceJSONSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("successfully validated the response with resource JSON schema");
    }
}
