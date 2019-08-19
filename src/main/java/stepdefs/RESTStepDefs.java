package stepdefs;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import utils.RESTUtils;

import java.util.*;

public class RESTStepDefs {

    private RESTUtils restUtils = new RESTUtils();
    private String response = null;
    private static Map<String, String> savedValues = new HashMap<String, String>();

    static {
        savedValues.put("null",null);
    }

    @When("^a \"([^\"]*)\" request is sent to \"([^\"]*)\" with the below data$")
    public void a_request_is_sent_to_with_the_below_data(String method, String url, DataTable dt) {
        List<Map<String, String>> mapIn = dt.asMaps(String.class, String.class);
        List<Map<String, String>> mapOut = new ArrayList<Map<String, String>>();

        // trying to update mapIn directly gets UnsupportedOperationException
        //todo: make this a method
        Iterator<Map<String, String>> i = mapIn.iterator();
        while (i.hasNext()) {
            Map<String, String> entryIn = i.next();
            String value = entryIn.get("value");
            Map<String, String> entryOut = new HashMap<String, String>();
            entryOut.put("type",entryIn.get("type"));
            entryOut.put("key",entryIn.get("key"));
            if(value.startsWith("((") && value.endsWith("))")){
                String savedValueKey = value.substring(2,value.length()-2);
                System.out.println(savedValues.get(savedValueKey));
                entryOut.put("value",savedValues.get(savedValueKey));
            } else {
                entryOut.put("value",entryIn.get("value"));
            }
            mapOut.add(entryOut);
        }

        response = restUtils.sendRequest(method, url, mapOut);

    }

    @When("^a \"([^\"]*)\" request is sent to \"([^\"]*)\" using the JSON file \"([^\"]*)\" and any below data$")
    public void a_request_is_sent_to_using_the_JSON_file_and_any_below_data(String method, String url, String jsonFileName, DataTable dt) {
        List<Map<String, String>> mapIn = dt.asMaps(String.class, String.class);
        List<Map<String, String>> mapOut = new ArrayList<Map<String, String>>();

        // trying to update mapIn directly gets UnsupportedOperationException
        Iterator<Map<String, String>> i = mapIn.iterator();
        while (i.hasNext()) {
            Map<String, String> entryIn = i.next();
            String value = entryIn.get("value");
            Map<String, String> entryOut = new HashMap<String, String>();
            entryOut.put("type",entryIn.get("type"));
            entryOut.put("key",entryIn.get("key"));
            if(value.startsWith("((") && value.endsWith("))")){
                String savedValueKey = value.substring(2,value.length()-2);
                System.out.println(savedValues.get(savedValueKey));
                entryOut.put("value",savedValues.get(savedValueKey));
            } else {
                entryOut.put("value",entryIn.get("value"));
            }
            mapOut.add(entryOut);
        }

        response = restUtils.sendJSONRequest(method, url, jsonFileName, mapOut);

    }

    @When("^the request \"([^\"]*)\" in SoapUI project \"([^\"]*)\" is sent with any below data$")
    public void the_request_in_SoapUI_project_is_sent_with_any_below_data(String reqName, String soapUIFile, DataTable dt) {
        List<Map<String, String>> mapIn = dt.asMaps(String.class, String.class);
        List<Map<String, String>> mapOut = new ArrayList<Map<String, String>>();

        // trying to update mapIn directly gets UnsupportedOperationException
        Iterator<Map<String, String>> i = mapIn.iterator();
        while (i.hasNext()) {
            Map<String, String> entryIn = i.next();
            String value = entryIn.get("value");
            Map<String, String> entryOut = new HashMap<String, String>();
            entryOut.put("type",entryIn.get("type"));
            entryOut.put("key",entryIn.get("key"));
            if(value.startsWith("((") && value.endsWith("))")){
                String savedValueKey = value.substring(2,value.length()-2);
                System.out.println(savedValues.get(savedValueKey));
                entryOut.put("value",savedValues.get(savedValueKey));
            } else {
                entryOut.put("value",entryIn.get("value"));
            }
            mapOut.add(entryOut);
        }

        response = restUtils.sendSoapUIRequest(reqName, soapUIFile, mapOut);

    }

    @Then("^the response comes back with the below values$")
    public void the_response_comes_back_with_the_below_values(DataTable nameValuePairs) {
        Map<String, String> nameValueMap = nameValuePairs.asMap(String.class, String.class);
        Map<String, String> mapOut = new HashMap<String, String>();
        for(Map.Entry<String, String> entry : nameValueMap.entrySet()) {
            if((entry.getValue()).contains("((")) {
                String param = entry.getValue();
                param  = param.substring(param.indexOf("((") + 2,param.indexOf("))"));
                mapOut.put(entry.getKey(),savedValues.get(param));
            } else {
                mapOut.put(entry.getKey(),entry.getValue());
            }
        }
        restUtils.validateResponse(response, mapOut);
    }

    @Then("^the response comes back with the below keys$")
    public void the_response_comes_back_with_the_below_keys(DataTable nameTable) {
        Map<String, String> nameMap = nameTable.asMap(String.class, String.class);
        restUtils.validateResponseNoValues(response, nameMap);
    }

    @Then("^save the value of \"([^\"]*)\" from the response$")
    public void save_value_of_in_the_response (String name) {
        //todo: handle replacing existing value
        savedValues.put(name, restUtils.getResponseValue(name, response));
        System.out.println("saved value: " + name + " = " + savedValues.get(name));
    }

    @Then("^the response matches the JSON in \"([^\"]*)\" and any below data$")
    public void the_response_matches_the_json_in_and_any_below_values(String jsonFileName, DataTable dt) {
        Map<String, String> mapIn = dt.asMap(String.class, String.class);
        Map<String, String> mapOut = new HashMap<String, String>();

        // trying to update mapIn directly gets UnsupportedOperationException
        for(Map.Entry<String, String> entry: mapIn.entrySet()) {
            if (entry.getValue().startsWith("((") && entry.getValue().endsWith("))")) {
                String savedValueKey = entry.getValue().substring(2, entry.getValue().length() - 2);
                System.out.println(savedValues.get(savedValueKey));
                mapOut.put(entry.getKey(), savedValues.get(savedValueKey));
            } else {
                mapOut.put(entry.getKey(), entry.getValue());
            }
        }

        restUtils.validateJSONResponse(response, jsonFileName, mapOut);
    }

}
