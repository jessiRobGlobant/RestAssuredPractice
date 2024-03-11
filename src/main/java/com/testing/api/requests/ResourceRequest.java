package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Resource;
import com.testing.api.utils.Constants;
import com.testing.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourceRequest extends BaseRequest {
    private String endpoint;

    /**
     * This function gets all the resources from the API
     * @return Response
     */
    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * This function gets a resource from the API by its id
     * @param resourceId resource id
     * @return Response
     */
    public Response getResource(String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * This function creates a new resource in the API
     * @param resource resource object
     * @return response
     */
    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestPost(endpoint, createBaseHeaders(), resource);
    }

    /**
     * This function updates a resource information in the API
     * @param resource resource object
     * @param resourceId resource id of the object to change
     * @return response
     */
    public Response updateResource(Resource resource, String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPut(endpoint, createBaseHeaders(), resource);
    }

    /**
     * This function deletes a resource from the API by its id
     * @param resourceId resource id of the object to change
     * @return response
     */
    public Response deleteResource(String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);;
        return requestDelete(endpoint, createBaseHeaders());
    }

    /**
     * This function gets the response es a resource Entity
     * @param response response
     * @return Resource
     */
    public Resource getResourceEntity(@NotNull Response response) {
        return response.as(Resource.class);
    }

    /**
     * This function gets a resource list based on a response
     * of resources
     * @param response response
     * @return list of resources
     */
    public List<Resource> getResourcesEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    /**
     * This function creates a default resource on the API
     * based on a json file
     * @return response
     */
    public Response createDefaultResource() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createResource(jsonFile.getResourceByJson(Constants.DEFAULT_RESOURCE_FILE_PATH));
    }

    /**
     * This function creates a resource entity based on a
     * Json
     * @param resourceJson Resource as a Json object
     * @return resource
     */
    public Resource getResourceEntity(String resourceJson) {
        Gson gson = new Gson();
        return gson.fromJson(resourceJson, Resource.class);
    }

    /**
     * This function validates the schema of a response based
     * on a schema defined for resources in a Json file
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
