package com.testing.api.stepDefinitions;

import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ResourceSteps {

    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Resource resource;

    // Given
    @Given("there are registered resources in the system")
    public void thereAreRegisteredResourcesInTheSystem() {
        CommonSteps.setResponse(resourceRequest.getResources());
        CommonSteps.logInfo(CommonSteps.getResponse().jsonPath().prettify());
        Assert.assertEquals(CommonSteps.getResponse().statusCode(), 200);

        List<Resource> resourceList = resourceRequest.getResourcesEntity(CommonSteps.getResponse());
        // If not registered at least 5 resources, create them
        for (int i = resourceList.size();i<5; i++){
            CommonSteps.setResponse(resourceRequest.createDefaultResource());
            CommonSteps.logInfo("Added resource with status code "+ CommonSteps.getResponse().statusCode());
            CommonSteps.logInfo(CommonSteps.getResponse().jsonPath().prettify());
            Assert.assertEquals(CommonSteps.getResponse().statusCode(), 201);
        }
        CommonSteps.logInfo("There are registered resources in the system");
    }

    // When
    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        CommonSteps.setResponse(resourceRequest.getResources());
        CommonSteps.logInfo("Sent a GET request to view all the resources");
    }

    @When("I retrieve the details of the latest resource")
    public void iRetrieveTheDetailsOfTheLatestResource() {
        // Get the last resource id
        CommonSteps.setResponse(resourceRequest.getResources());
        List<Resource> resourceList = resourceRequest.getResourcesEntity(CommonSteps.getResponse());
        resource = resourceList.get(resourceList.size()-1);
        String lastId = resource.getId();

        // Get the last resource
        CommonSteps.setResponse(resourceRequest.getResource(lastId));
        CommonSteps.logInfo("Retrieved the details of the latest resource");
        CommonSteps.logInfo(CommonSteps.getResponse().jsonPath().prettify());
        CommonSteps.logInfo("The status code is " + CommonSteps.getResponse().statusCode());
    }

    @When("I send a PUT request to update the latest resource")
    public void iSendAPUTRequestToUpdateTheLatestResource(String requestBody) {
        String lastId = resource.getId();
        resource = resourceRequest.getResourceEntity(requestBody);
        CommonSteps.setResponse(resourceRequest.updateResource(resource, lastId));
        CommonSteps.logInfo("Updated last id: "+lastId);
    }

    // Then
    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        resource = resourceRequest.getResourceEntity(CommonSteps.getResponse());
        Map<String, String> expectedDataMap = expectedData.asMaps().get(0);

        Assert.assertEquals(expectedDataMap.get("name"), resource.getName());
        Assert.assertEquals(expectedDataMap.get("trademark"), resource.getTrademark());
        Assert.assertEquals(expectedDataMap.get("stock"),String.valueOf(resource.getStock()));
        Assert.assertEquals(expectedDataMap.get("price"), String.valueOf(resource.getPrice()));
        Assert.assertEquals(expectedDataMap.get("description"), resource.getDescription());
        Assert.assertEquals(expectedDataMap.get("tags"), resource.getTags());
        Assert.assertEquals(expectedDataMap.get("active"), String.valueOf(resource.getActive()));
        CommonSteps.logInfo("The response has the following details:" + expectedData);
    }

    @Then("validates the response with the resource list JSON schema")
    public void userValidatesResponseWithTheResourceListJSONSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(CommonSteps.getResponse(), path));
        CommonSteps.logInfo("Successfully validated the response with client list JSON schema");
    }

    @Then("validates the response with the resource JSON schema")
    public void validatesTheResponseWithTheResourceJSONSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(CommonSteps.getResponse(), path));
        CommonSteps.logInfo("Successfully validated the response with resource JSON schema");
    }
}
