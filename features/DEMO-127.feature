Feature: User Login Feature

  As a user, I want to be able to log in to the application so that I can access my personalized dashboard.

  Scenario: Successful login
    Given the user is on the login page
    When the user enters a valid username and password
    And the user clicks the login button
    Then the system validates the credentials against the database
    And the user is redirected to the dashboard
    And the login attempt is logged for security

  Scenario: Failed login due to incorrect credentials
    Given the user is on the login page
    When the user enters an invalid username or password
    And the user clicks the login button
    Then the system validates the credentials against the database
    And an error message is displayed
    And the login attempt is logged for security

  Scenario: Failed login due to missing credentials
    Given the user is on the login page
    When the user does not enter a username or password
    And the user clicks the login button
    Then an error message is displayed indicating missing credentials
    And the login attempt is logged for security