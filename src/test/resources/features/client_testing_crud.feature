@active
Feature: Client testing CRUD

  @smoke
  Scenario: Read details of an existing client
    Given there are registered clients in the system
    When I retrieve the details of the client with ID "1"
    Then the response should have a status code of 200
    And the response should have the following details:
      | Name   | LastName | Gender | Country  | City   | Id |
      | Manuel | Munoz    | Man    | Colombia | Bogota | 1  |
    And validates the response with the client JSON schema

  @smoke @client
  Scenario: Get the list of clients
    Given there are registered clients in the system
    When I send a GET request to view all the clients
    Then the response should have a status code of 200
    And validates the response with the client list JSON schema

  @smoke @client
  Scenario: Create a new client
    Given I have a client with the following details:
      | Name | LastName | Country | City      | Email           | Phone     |
      | John | Doe      | USA     | New York  | dude@gmail.com  | 1234567   |
    When I send a POST request to create a client
    Then the response should have a status code of 201
    And the response should include the details of the created client
    And validates the response with the client JSON schema


  @smoke
  Scenario: Delete an existing client
    Given there are registered clients in the system
    When I send a DELETE request to delete the client with ID "1"
    Then the response should have a status code of 200
    And the response should have the following details:
      | Name  | LastName | Gender | Country | City      | Id |
      | Maria | Gomez    | Female | Spain   | Barcelona | 1  |
    And validates the response with the client JSON schema
