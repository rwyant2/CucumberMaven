@textArea @HTML5
	
Feature: Validate that the input of a text area displayed

Background:
  Given user goes to html page

Scenario: Validate text area input for a long entry
  When user enters text "The bus ride still sucks. I take different routes now and carry personal protection gear. I'm sick of the wierdos begging for change and cigarettes, wanting me to explain public transit to them, wanting to use my cell phone, or just demanding attention when I am clearly not interested. Not everyone is an extrovert." in element with id "textArea"
  And user clicks button labeled "Submit"
  Then the text with id "textAreaResult" displays "The bus ride still sucks. I take different routes now and carry personal protection gear. I'm sick of the wierdos begging for change and cigarettes, wanting me to explain public transit to them, wanting to use my cell phone, or just demanding attention when I am clearly not interested. Not everyone is an extrovert."

Scenario: Validate text area input that contains return characters
  When user enters text "this is line one" ending with Return in element with id "textArea"
  And user enters text "this is line two" ending with Return in element with id "textArea"
  And user enters text "this is line three" ending with Return in element with id "textArea"
  And user clicks button labeled "Submit"
  Then the text with id "textAreaResult" displays "this is line one this is line two this is line three"