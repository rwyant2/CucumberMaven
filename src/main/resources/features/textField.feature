@textField @HTML5
	
Feature: Validate that the input of a text field displayed 

Background:
  Given user goes to html page

Scenario: Validate text field input
  When user enters "banana" in field where label contains "What's your text?"
  And user clicks button labeled "Submit"
  Then the text with id "textResult" displays "banana"

Scenario: Intentional fail to verify errors are getting reported
  When user enters "wheeee!" in field where label contains "What's your text?"
  And user clicks button labeled "Submit"
  Then the text with id "textResult" displays "banana"