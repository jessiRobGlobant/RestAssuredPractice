package com.testing.api.stepDefinitions;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

@Data
public class CommonSteps {
    protected static final Logger logger = LogManager.getLogger(CommonSteps.class);
    protected static Response response;

    // Helper
    protected static void logInfo(Object msg) {
        logger.info(msg);
    }

    protected static Response getResponse(){
        return response;
    }

    protected static void setResponse(Response response){
        CommonSteps.response = response;
    }

    // Then
    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        logInfo("The response has a status code of " + statusCode);
        Assert.assertEquals(statusCode, response.getStatusCode());
    }


}
