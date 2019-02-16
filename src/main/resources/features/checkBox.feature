@checkBox @HTML5
	
Feature: Validate that the input of a check box displayed

Background:
  Given user goes to html page

  Scenario: Validate check box selections 1 and 2
    When user clicks webelement with id "checkBoxSelection1"
    And user clicks webelement with id "checkBoxSelection2"
    And user clicks button labeled "Submit"
    Then the text with id "checkBoxResult" displays "one,two"

  Scenario: Validate check box selections 2 and 3
    When user clicks webelement with id "checkBoxSelection2"
    And user clicks webelement with id "checkBoxSelection3"
    And user clicks button labeled "Submit"
    Then the text with id "checkBoxResult" displays "two,three"

  Scenario: Validate check box selections 1 and 3
    When user clicks webelement with id "checkBoxSelection1"
    And user clicks webelement with id "checkBoxSelection3"
    And user clicks button labeled "Submit"
    Then the text with id "checkBoxResult" displays "one,three"

  Scenario: Validate check box tests fail correctly
    When user clicks webelement with id "checkBoxSelection1"
    And user clicks webelement with id "checkBoxSelection3"
    And user clicks button labeled "Submit"
    Then the text with id "checkBoxResult" displays "Intentional Fail #2"

  Scenario: Validate check box selections 1, 2, and 3
    When user clicks webelement with id "checkBoxSelection1"
    And user clicks webelement with id "checkBoxSelection2"
    And user clicks webelement with id "checkBoxSelection3"
    And user clicks button labeled "Submit"
    Then the text with id "checkBoxResult" displays "one,two,three"

  Scenario: Validate check box selections when nothing is selected
    When user clicks button labeled "Submit"
    Then the text with id "checkBoxResult" is blank