package stepdefs;

import cucumber.api.java.en.Given;
import pages.*;

public class CommonStepDefs {
    Html5Page htmlPage = new Html5Page();
    //indexPage indexPage = new indexPage();

    @Given("^user goes to html page$")
    public void user_goes_to_html5_page() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
        System.out.println("place to put breakpoint");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
        htmlPage.goToHTML5Page();
    }


    //
//	@When("^user enters \"([^\"]*)\" in field where label contains \"([^\"]*)\"$")
//	//@When("^user enters {string} in field where label contains {string}$")
//	public void user_enters_in_field_where_label_contains(String text, String label) {
//	    htmlPage.enterTextInField(text,label);
//	    throw new cucumber.api.PendingException();
//	}

}
