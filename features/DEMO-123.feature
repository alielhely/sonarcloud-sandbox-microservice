```gherkin
Feature: User Login Feature

  As a user,
  I want to be able to log in to the application
  So that I can access my personalized dashboard.

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters a valid username and password
    And the user clicks the "Login" button
    Then the system should validate the credentials against the database
    And the user should be redirected to the dashboard
    And the login attempt should be logged for security

  Scenario: Failed login with invalid credentials
    Given the user is on the login page
    When the user enters an invalid username and password
    And the user clicks the "Login" button
    Then the system should validate the credentials against the database
    And the user should see an error message indicating invalid credentials
    And the login attempt should be logged for security

  Scenario: Login attempt with empty fields
    Given the user is on the login page
    When the user leaves the username and password fields empty
    And the user clicks the "Login" button
    Then the system should not validate the credentials
    And the user should see an error message indicating that fields cannot be empty
    And the login attempt should not be logged
```