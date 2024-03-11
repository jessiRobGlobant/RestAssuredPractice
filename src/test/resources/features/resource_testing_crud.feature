@active
Feature: Resource testing CRUD

  @smoke @resource
  Scenario: Get the List of Resources
    Given there are registered resources in the system
    When I send a GET request to view all the resources
    Then the resource response should have a status code of 200
    And validates the response with the resource list JSON schema

  @smoke
  Scenario: Update the last resource
    Given there are registered resources in the system
    And I retrieve the details of the latest resource
    When I send a PUT request to update the latest resource
    """
    {
      "name": "NewName",
      "trademark": "NewTradeMark",
      "stock": 1000,
      "price": 99.99,
      "description": "New resource description",
      "tags": "NewTag",
      "active": true
    }
    """
    Then the resource response should have a status code of 200
    And the response should have the following details:
      | name    | trademark   | stock | price   | description               | tags    | active  |
      | NewName | NewTradeMark| 1000  | 99.99   | New resource description  | NewTag  | true    |
    And validates the response with the resource JSON schema
