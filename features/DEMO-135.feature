```gherkin
Feature: User Login Feature

  As a user,
  I want to be able to log in to the application
  So that I can access my personalized dashboard.

  Scenario: Successful login
    Given the user is on the login page
    When the user enters a valid username and password
    And clicks the login button
    Then the system should validate the credentials against the database
    And the user should be redirected to the dashboard
    And the login attempt should be logged for security

  Scenario: Failed login due to invalid credentials
    Given the user is on the login page
    When the user enters an invalid username or password
    And clicks the login button
    Then the system should display an error message
    And the login attempt should be logged for security

  Scenario: Failed login due to empty fields
    Given the user is on the login page
    When the user leaves the username and password fields empty
    And clicks the login button
    Then the system should display an error message
    And the login attempt should be logged for security
```