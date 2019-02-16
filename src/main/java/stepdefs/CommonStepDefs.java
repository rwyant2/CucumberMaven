package stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import pages.*;

public class CommonStepDefs {
    IndexPage indexPage = new IndexPage();
    ResultsPage resultsPage = new ResultsPage();

    @Given("^user goes to html page$")
    public void user_goes_to_html5_page() {
        switch(indexPage.getPageTitle()) {
            case "index": indexPage.goToHTML5Page(); break;
            case "results": resultsPage.goToHTML5Page(); break;
            default: Assert.fail("Cannot navigate to HTML5 page");
        }
    }

	@When("^user enters \"([^\"]*)\" in field where label contains \"([^\"]*)\"$")
	public void user_enters_in_field_where_label_contains(String text, String label) { indexPage.enterTextInField(text,label); }

	@When("^user enters text \"([^\"]*)\" in element with id \"([^\"]*)\"$")
    public void user_enters_text_in_element_with_id(String text, String id) { indexPage.enterTextInElement(text, id); }

    @When("^user enters text \"([^\"]*)\" ending with Return in element with id \"([^\"]*)\"$")
    public void user_enters_text_ending_with_Return_in_element_with_id(String text, String id) { indexPage.enterTextEndingWithReturnInElement(text, id); }

    @And("^user clicks button labeled \"([^\"]*)\"$")
    public void user_clicks_button_labeled(String label) {
        indexPage.clickButton(label);
    }

    @Then("^the text with id \"([^\"]*)\" displays \"([^\"]*)\"$")
    public void the_text_with_id_displays(String id, String value) {
        indexPage.verifyTextField(id, value);
    }

    @Then("^the text with id \"([^\"]*)\" is blank$")
    public void the_text_with_id_is_blank(String id) {
        indexPage.verifyTextFieldIsBlank(id);
    }

    @When("^user clicks webelement with id \"([^\"]*)\"$")
    public void user_clicks_webelement_with_id(String id) { indexPage.clickWebElementWithId(id); }

    @When("^user selects \"([^\"]*)\" from dropdown with id \"([^\"]*)\"$")
    public void user_selects_from_dropdown_with_id(String option, String id) { indexPage.selectDropDownOption(option, id); }

}
