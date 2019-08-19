package backup_stepdefs;

//import cucumber.api.DataTable;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import utils.RESTUtils;

import java.util.HashMap;
import java.util.Map;


public class RESTStepDefs_backup {

    private RESTUtils RESTUtils = new RESTUtils();
    private String response = null;
    private static String savedId = null;

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

    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\"$")
    public void send_a_request_to_endpoint_resource(String reqName, String endpoint) {
        response = RESTUtils.sendRequestNoValues(reqName, endpoint);
    }

    @Then("^the response comes back with the below keys$")
    public void the_response_comes_back_with_the_below_keys(DataTable nameTable) {
        Map<String, String> nameMap = nameTable.asMap(String.class, String.class);
        RESTUtils.validateResponseNoValues(response, nameMap);
    }

    @Then("^the response matches the JSON in \"([^\"]*)\"$")
    public void the_response_matches_the_json_in (String filename) {
        RESTUtils.validateResponseJSON(response, filename);
    }

    @Then("^save the id in the response$")
    public void print_out_id_for_employee () {
        savedId = RESTUtils.getId(response);
        System.out.println("savedId = " + savedId);
    }

    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\" with the previously saved id$")
    public void send_a_request_to_endpoint_resource_with_the_previously_saved_id(String reqType, String endpoint) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id",savedId);
        response = RESTUtils.sendRequest(reqType, endpoint, map);
    }

}
