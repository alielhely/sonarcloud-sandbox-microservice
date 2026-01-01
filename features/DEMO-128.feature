Feature: User Login

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters valid username and password
    And the user clicks on the login button
    Then the user should be redirected to the dashboard
    And a welcome message should be displayed

  Scenario: Login attempt with invalid credentials
    Given the user is on the login page
    When the user enters an invalid username or password
    And the user clicks on the login button
    Then an error message should be displayed
    And the user should remain on the login page

  Scenario: Login attempt with empty fields
    Given the user is on the login page
    When the user leaves the username and password fields empty
    And the user clicks on the login button
    Then an error message should be displayed
    And the user should remain on the login page

  Scenario: Password recovery option
    Given the user is on the login page
    When the user clicks on the "Forgot Password" link
    Then the user should be redirected to the password recovery page

  Scenario: Remember me option
    Given the user is on the login page
    When the user enters valid credentials
    And the user selects the "Remember Me" option
    And the user clicks on the login button
    Then the user should be redirected to the dashboard
    And the user's login information should be remembered for future sessions

  Scenario: Logout from the dashboard
    Given the user is logged in and on the dashboard
    When the user clicks on the logout button
    Then the user should be redirected to the login page
    And the session should be terminated