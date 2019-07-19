package stepdefs;

//import cucumber.api.DataTable;
import cucumber.api.java.en.But;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import utils.RESTUtils;

import java.util.*;


public class RESTStepDefs {

    private RESTUtils RESTUtils = new RESTUtils();
    private String response = null;
    private static Map<String, String> savedValues = new HashMap<String, String>();
    private Map<String, String> addThis = new HashMap<String, String>();

    @When("^a \"([^\"]*)\" request is sent to \"([^\"]*)\" with the values listed below$")
    public void a_request_is_sent_to_with_the_values_listed_below(String method, String endpoint, DataTable dt) {
        List<Map<String, String>> mapIn = dt.asMaps(String.class, String.class);
        List<Map<String, String>> mapOut = new ArrayList<Map<String, String>>();

        Iterator<Map<String, String>> i = mapIn.iterator();

        // if I try to update mapIn directly, I get UnsupportedOperationException
        // datatable stuff goes into a query (i.e. endpoint.com/?param=value
        while (i.hasNext()) {
            Map<String, String> entryIn = i.next();
            String value = entryIn.get("value");
            Map<String, String> entryOut = new HashMap<String, String>();
            entryOut.put("type",entryIn.get("type"));
            entryOut.put("name",entryIn.get("name"));
            if(value.startsWith("((") && value.endsWith("))")){
                String savedValueKey = value.substring(2,value.length()-2);
                System.out.println(savedValues.get(savedValueKey));
                entryOut.put("value",savedValues.get(savedValueKey));
            } else {
                entryOut.put("value",entryIn.get("value"));
            }
            mapOut.add(entryOut);
        }

        //if a saved value is in the url itself, parse it here
        //i.e. endpoint.com/value
        //todo: test with multiple params
        while(endpoint.indexOf("((") > 0) {
            int start = endpoint.indexOf("((") + 2;
            int end = endpoint.indexOf("))");
            String param = endpoint.substring(start, end);
            endpoint = endpoint.replace("((" + param + "))",savedValues.get(param));
        }

        response = RESTUtils.sendRequest(method, endpoint, mapOut);
    }

    // The below two support simple name-value pairs. This doesn't support arrays and nested entities... yet.
//    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\" with the below values$")
//    public void send_a_request_to_endpoint_resource_with_the_below_values(String reqMethod, String endpoint, DataTable nameValuePairs) {
//        Map<String, String> nameValueMap = nameValuePairs.asMap(String.class, String.class);
//        response = RESTUtils.sendRequest(reqMethod, endpoint, nameValueMap);
//    }
//
    @Then("^the response comes back with the below values$")
    public void the_response_comes_back_with_the_below_values(DataTable nameValuePairs) {
        Map<String, String> nameValueMap = nameValuePairs.asMap(String.class, String.class);
        Map<String, String> mapOut = new HashMap<String, String>();
        for(Map.Entry<String, String> entry : nameValueMap.entrySet()) {
            if((entry.getValue()).indexOf("((") > -1) {
                String param = entry.getValue();
                param  = param.substring(param.indexOf("((") + 2,param.indexOf("))"));
                mapOut.put(param,savedValues.get(param));
            } else {
                mapOut.put(entry.getKey(),entry.getValue());
            }
        }
        RESTUtils.validateResponse(response, mapOut);
    }
//
//    //This should be able to support any JSON structure
//    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\" with the JSON \"([^\"]*)\"$")
//    public void send_a_request_to_endpoint_resource_with_the_JSON(String reqMethod, String endpoint, String jSONFile) {
//        response = RESTUtils.sendRequest(reqMethod, endpoint, jSONFile);
//
//    }
//
//    @When("send the request \"([^\"]*)\" in SoapUI project \"([^\"]*)\"")
//    public void send_the_request_in_SoapUI_project(String reqName, String soapUiProjectFile) {
//        response = RESTUtils.sendRequest(reqName, soapUiProjectFile);
//    }
//
//    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\"$")
//    public void send_a_request_to_endpoint_resource(String reqName, String endpoint) {
//        response = RESTUtils.sendRequestNoValues(reqName, endpoint);
//    }
//
    @Then("^the response comes back with the below keys$")
    public void the_response_comes_back_with_the_below_keys(DataTable nameTable) {
        Map<String, String> nameMap = nameTable.asMap(String.class, String.class);
        RESTUtils.validateResponseNoValues(response, nameMap);
    }
//
//    @Then("^the response matches the JSON in \"([^\"]*)\"$")
//    public void the_response_matches_the_json_in (String filename) {
//        RESTUtils.validateResponseJSON(response, filename);
//    }
//
    @Then("^save the value of \"([^\"]*)\" from the response$")
    public void save_value_of_in_the_response (String name) {
        //todo: handle replacing existing value
        savedValues.put(name,RESTUtils.getResponseValue(name, response));
        System.out.println("saved value: " + name + " = " + savedValues.get(name));
    }
//
//    @When("^send a \"([^\"]*)\" request to endpoint-resource \"([^\"]*)\" with the previously saved id$")
//    public void send_a_request_to_endpoint_resource_with_the_previously_saved_id(String reqType, String endpoint) {
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("id",savedId);
//        response = RESTUtils.sendRequest(reqType, endpoint, map);
//    }

}
