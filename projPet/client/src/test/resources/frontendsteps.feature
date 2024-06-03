Feature: Pet Management and Appointment Scheduling

  Scenario: User logs in and adds a pet
    Given the user is on the login page
    When the user enters the email "test@example.com"
    And the user enters the password "password123123"
    And the user clicks on the login button
    Then the user should be logged in successfully
    And the user navigates to add a pet
    And the user enters pet name "hope"
    And the user enters pet type "dog"
    And the user enters pet breed "husky"
    And the user enters pet age "3"
    And the user enters pet color "black"
    And the user clicks on the add pet button
    Then the pet should be added successfully

  Scenario: User schedules an appointment for a pet
    Given the user is logged in
    And the user navigates to appointments
    When the user selects the pet "hope" for the appointment
    And the user selects the date "2024-05-30"
    And the user selects the time slot
    And the user enters observations "sick"
    And the user clicks on the add appointment button
    Then the appointment should be added successfully

  Scenario: User updates pet details
    Given the user is logged in
    And the user navigates to the profile
    When the user views the pet details
    And the user clicks on the update pet button
    And the user updates the pet color to "black and white"
    And the user clicks on the save button
    Then the pet details should be updated successfully

  Scenario: User logs out
    Given the user is logged in
    When the user clicks on the logout button
    Then the user should be logged out successfully
