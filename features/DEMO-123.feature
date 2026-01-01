```gherkin
Feature: User Login Feature

  As a user,
  I want to be able to log in to the application
  So that I can access my personalized dashboard.

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters a valid username and password
    And the user clicks the login button
    Then the system should validate the credentials against the database
    And the user should be redirected to the dashboard
    And the login attempt should be logged for security

  Scenario: Failed login with invalid credentials
    Given the user is on the login page
    When the user enters an invalid username and password
    And the user clicks the login button
    Then the system should validate the credentials against the database
    And the user should see an error message
    And the login attempt should be logged for security
```