@dropDown @HTML5
	
Feature: Validate that the dropdown selection displayed

Background:
  Given user goes to html page

  Scenario Outline: Validate the available dropdown selections
    When user selects "<option>" from dropdown with id "dropDownSelection"
    And user clicks button labeled "Submit"
    Then the text with id "dropDownResult" displays "<result>"

  Examples:
    |option|result|
    |tomato|red   |
    |onions|white |
    |mustard|yellow|

  Scenario: Validate default dropdown option
    And user clicks button labeled "Submit"
    Then the text with id "dropDownResult" displays "yellow"