Feature: The User can connect to a role and disconnect

  Scenario: The User connects itself as a Administrator
    Given The User is not already connected
    When The User connects as an Admin
    Then The User is connected as an Admin

  Scenario: The User changes role from Admin to Employee
    Given The User is already connected as a Admin
    When The User connects as an Employee
    Then The User is connected as an Employee

  Scenario: The User is not connected and disconnects
    Given The User is not already connected
    When The User disconnects
    Then The User is not connected

  Scenario: The User is connected as a Client and disconnects
    Given The User is already connected as a Client
    When The User disconnects
    Then The User is not connected
