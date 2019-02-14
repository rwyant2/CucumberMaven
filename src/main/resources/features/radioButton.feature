@textField @HTML5
	
Feature: Validate that the radio button selection is being displayed

Background:
  Given user goes to html page

Scenario: Validate first choice
  When user clicks webelement with id "radioButtonSelection1"
  And user clicks button labeled "Submit"
  Then the text with id "radioResult" displays "apple"

Scenario: Validate second choice
  When user clicks webelement with id "radioButtonSelection2"
  And user clicks button labeled "Submit"
  Then the text with id "radioResult" displays "banana"

Scenario: Validate third choice
  When user clicks webelement with id "radioButtonSelection3"
  And user clicks button labeled "Submit"
  Then the text with id "radioResult" displays "cherry"

Scenario: Validate default choice
  When user clicks button labeled "Submit"
  Then the text with id "radioResult" is blank