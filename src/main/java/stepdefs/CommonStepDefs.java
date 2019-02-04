package stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.*;

public class CommonStepDefs {
    Html5Page htmlPage = new Html5Page();
    //indexPage indexPage = new indexPage();

    @Given("^user goes to html page$")
    public void user_goes_to_html5_page() {
        htmlPage.goToHTML5Page();
    }

	@When("^user enters \"([^\"]*)\" in field where label contains \"([^\"]*)\"$")
	public void user_enters_in_field_where_label_contains(String text, String label) {
	    htmlPage.enterTextInField(text,label);
	}

	@And("^user clicks button labeled \"([^\"]*)\"$")
    public void user_clicks_button_labeled(String label) {
        htmlPage.click(label);
    }

    @Then("^the text with id \"([^\"]*)\" displays \"([^\"]*)\"$")
    public void the_text_with_id_displays(String id, String value) {
        htmlPage.verifyTextField(id, value);
    }
}
