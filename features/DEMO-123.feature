```gherkin
Feature: User Login Feature

  As a user,
  I want to be able to log in to the application
  So that I can access my personalized dashboard.

  Background:
    Given the user is on the login page

  Scenario: Successful login
    When the user enters a valid username and password
    And clicks the login button
    Then the system validates the credentials against the database
    And the user is redirected to the dashboard
    And the login attempt is logged for security

  Scenario: Failed login due to invalid credentials
    When the user enters an invalid username and password
    And clicks the login button
    Then the system validates the credentials against the database
    And an error message is displayed
    And the login attempt is logged for security

  Scenario: Failed login due to missing username
    When the user leaves the username field empty
    And enters a password
    And clicks the login button
    Then an error message is displayed
    And the login attempt is logged for security

  Scenario: Failed login due to missing password
    When the user enters a username
    And leaves the password field empty
    And clicks the login button
    Then an error message is displayed
    And the login attempt is logged for security
```