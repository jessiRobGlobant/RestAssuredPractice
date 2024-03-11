## Project description
This project was created by Jessica Robles as a project in the API Test Automation class. This projects testes an API features with help of RestAssured.

## Project structure
* `src/main`:
  * `models`: Contains the classes that define the structure of the models
  * `request`: Contains the classes with the logic to send request to the API, one for clients, one for resources and another one that defines a base request to the API.
  * `utils`:
    * `Constants`: Defines constants to use in the program
    * `JsonFileReader`: Helper class to read Json and turn it into objects
* `src/test`:
  * `TestRunner`: Contains the parameters and configurations for the tests
  * `stepDefinitions`: Contains all the steps defined in gherkin language in the test scenarios
  * `features`: Contains the test scenarios definitions in gherkin language
  * `schemas`: Contains the expected structure schemas for the json responses from the API
 
## Run scenarios
1. Install Maven
2. Run `mvn clean install` on the terminal
3. Open the feature file with the scenario to run and execute it
