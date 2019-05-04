package stepdefs;

//import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import utils.RESTUtils;
import java.util.List;
import java.util.Map;


public class RESTStepDefs {

    private RESTUtils RESTUtils = new RESTUtils();
    private String response = null;

    // The below two support simple name-value pairs. This doesn't support arrays and nested entities... yet.
    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\" with the below values$")
    public void send_a_request_to_endpoint_resource_with_the_below_values(String reqMethod, String endpoint, DataTable nameValuePairs) {
        Map<String, String> nameValueMap = nameValuePairs.asMap(String.class, String.class);
        response = RESTUtils.sendRequest(reqMethod, endpoint, nameValueMap);
    }

    @Then("^the response comes back with the below values$")
    public void the_response_comes_back_with_the_below_values(DataTable nameValuePairs) {
        Map<String, String> nameValueMap = nameValuePairs.asMap(String.class, String.class);
        RESTUtils.validateResponse(response, nameValueMap);
    }

    //This should be able to support any JSON structure
    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\" with the JSON \"([^\"]*)\"$")
    public void send_a_request_to_endpoint_resource_with_the_JSON(String reqMethod, String endpoint, String jSONFile) {
        response = RESTUtils.sendRequest(reqMethod, endpoint, jSONFile);
    }

    @When("send the request \"([^\"]*)\" in SoapUI project \"([^\"]*)\"")
    public void send_the_request_in_SoapUI_project(String reqName, String soapUiProjectFile) {
        response = RESTUtils.sendRequest(reqName, soapUiProjectFile);
    }
}
