Feature: User Login

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters a valid username and password
    And the user clicks the login button
    Then the user is redirected to the dashboard
    And a welcome message is displayed

  Scenario: Unsuccessful login with invalid credentials
    Given the user is on the login page
    When the user enters an invalid username or password
    And the user clicks the login button
    Then an error message is displayed
    And the user remains on the login page

  Scenario: Attempt to login with empty credentials
    Given the user is on the login page
    When the user leaves the username and password fields empty
    And the user clicks the login button
    Then an error message is displayed indicating fields cannot be empty

  Scenario: Password recovery option
    Given the user is on the login page
    When the user clicks on the "Forgot Password" link
    Then the user is redirected to the password recovery page

  Scenario: Remember me option
    Given the user is on the login page
    When the user enters valid credentials
    And the user selects the "Remember Me" option
    And the user clicks the login button
    Then the user is redirected to the dashboard
    And the user's login information is remembered for future sessions