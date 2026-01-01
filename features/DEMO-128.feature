Feature: User Login

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters a valid username and password
    And the user clicks the login button
    Then the user should be redirected to the dashboard
    And a welcome message should be displayed

  Scenario: Unsuccessful login with invalid credentials
    Given the user is on the login page
    When the user enters an invalid username or password
    And the user clicks the login button
    Then an error message should be displayed
    And the user should remain on the login page

  Scenario: Attempt to login with empty credentials
    Given the user is on the login page
    When the user leaves the username and password fields empty
    And the user clicks the login button
    Then an error message should be displayed
    And the user should remain on the login page

  Scenario: Password recovery option
    Given the user is on the login page
    When the user clicks on the "Forgot Password" link
    Then the user should be redirected to the password recovery page

  Scenario: Remember me option
    Given the user is on the login page
    When the user enters valid credentials
    And the user checks the "Remember Me" option
    And the user clicks the login button
    Then the user should be redirected to the dashboard
    And the user should remain logged in after closing and reopening the browser

  Scenario: Logout from the dashboard
    Given the user is logged in and on the dashboard
    When the user clicks the logout button
    Then the user should be redirected to the login page
    And the session should be terminated