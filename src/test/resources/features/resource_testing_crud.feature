@active
Feature: Resource testing CRUD

  @smoke @resource
  Scenario: Get the List of Resources
    Given there are registered resources in the system
    When I send a GET request to view all the resources
    Then the resource response should have a status code of 200
    And validates the response with the resource list JSON schema

  @smoke
  Scenario: Update client details
    Given there are registered clients in the system
    And I retrieve the details of the client with ID "1"
    When I send a PUT request to update the client with ID "1"
    """
    {
      "name": "Maria",
      "lastName": "Gomez",
      "gender": "Female",
      "country": "Spain",
      "city": "Barcelona"
    }
    """
    Then the response should have a status code of 200
    And the response should have the following details:
      | Name  | LastName | Gender | Country | City      | Id |
      | Maria | Gomez    | Female | Spain   | Barcelona | 1  |
    And validates the response with client JSON schema
