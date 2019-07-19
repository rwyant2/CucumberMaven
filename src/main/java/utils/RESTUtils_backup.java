package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

//attempt with dom4j
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.Node;
//import org.dom4j.io.SAXReader;
//attempt with jdom2
//import org.jdom2.input.SAXBuilder;
//attempt with XPath

public class RESTUtils_backup {

    private String endpoint;
    private static String absPath = new File("").getAbsolutePath();
    private static String jsonFilePath = absPath + "/src/main/resources/json/";
    private static String soapUiFilePath = absPath + "/src/main/resources/soapui/";
    private JSONParser parser = new JSONParser();

    public String sendRequest(String reqMethod, String endpoint, Map<String, String> nameValueMap) {
        String responseString = null;
        this.endpoint = endpoint;
        switch(reqMethod.toLowerCase()) {
            case "post": responseString = sendPostMap(endpoint, nameValueMap); break;
            case "put": responseString = sendPutMap(endpoint, nameValueMap); break;
            case "get": responseString = sendGetMap(endpoint, nameValueMap); break;
//            case "delete": sendDelete; break;
            default:
                if (reqMethod.isEmpty()) {
                    Assert.fail("There's no request method. You are silly.");
                } else {
                    Assert.fail(reqMethod + " is currently unsupported as a request method");
                }
        }

        return responseString;
    }

    public String sendRequest(String reqMethod, String endpoint, String jSONFile) {
        String response = null;
        JSONObject json = parseJSONFile(jSONFile);
        switch(reqMethod.toLowerCase()) {
            case "post": response = sendPostJSON(endpoint, json); break;
            case "put": response = sendPutJSON(endpoint, json); break;
//            case "get": sendGet; break;
//            case "delete": sendDelete; break;
            default:
                if (reqMethod.isEmpty()) {
                    Assert.fail("There's no request method. You are silly.");
                } else {
                    Assert.fail(reqMethod + " is not currently unsupported as a request method");
                }
        }

        return response;
    }

    public String sendRequest(String reqName, String soapUiProjectFile) {
        String response = null;
        File file = null;
        Document doc = null;
        //String endpoint = null;
        String method = null;
        JSONObject json = null;
        XPath xPath =  XPathFactory.newInstance().newXPath();

        try {
            file = new File(soapUiFilePath + soapUiProjectFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            Assert.fail("Problem reading SoapUI project file " + soapUiProjectFile);
        }

        try {
            this.endpoint = (String) xPath.compile("*//request[@name=\"" + reqName + "\"]/originalUri/text()").evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            Assert.fail("Problem finding endpoint for request " + reqName + "in " + soapUiProjectFile);
        }

        try {
            method = (String) xPath.compile("*//request[@name=\"" + reqName + "\"]//ancestor::method/@method").evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            Assert.fail("Problem finding method for request " + reqName + "in " + soapUiProjectFile);
        }

        switch(method.toLowerCase()) {
            case "post":
                try {
                    json = (JSONObject) parser.parse((String) xPath.compile("*//request[@name=\"" + reqName + "\"]/request").evaluate(doc, XPathConstants.STRING));
                } catch (Exception e) {
                    Assert.fail("Problem finding message body for request " + reqName + "in " + soapUiProjectFile);
                }

                response = sendPostJSON(endpoint, json);
                break;

            case "put":
                try {
                    json = (JSONObject) parser.parse((String) xPath.compile("*//request[@name=\"" + reqName + "\"]/request").evaluate(doc, XPathConstants.STRING));
                    // This is another place I'm shoehorning in id to avoid writing new code. I'm lazy.
                    NodeList params = (NodeList) xPath.evaluate("*//request[@name=\"" + reqName + "\"]/parameters/entry", doc, XPathConstants.NODESET);
                    for(int i = 0;i < params.getLength();i++) {
                        endpoint = endpoint + params.item(i).getNodeValue() + "/";
//                        if(i > 0) endpoint = endpoint + "/";
                    }
                } catch (Exception e) {
                    Assert.fail("Problem finding message body for request " + reqName + "in " + soapUiProjectFile);
                }

                response = sendPutJSON(endpoint, json);
                break;

                case "get":

                    try {
                        response = sendGetSoapUI(endpoint, (NodeList) xPath.evaluate("*//request[@name=\"" + reqName + "\"]/parameters/entry", doc, XPathConstants.NODESET));
                    } catch (Exception e) {
                        Assert.fail("Problem finding parameters for request " + reqName + "in " + soapUiProjectFile);
                    }

                break;

//            case "delete": sendDelete; break;

            default: Assert.fail(method + " is not currently unsupported as a request method");
        }

        return response;
    }

    public String sendRequestNoValues(String reqMethod, String endpoint) {
        String response = null;
        switch(reqMethod.toLowerCase()) {
//            case "post": response = sendPost(endpoint); break;
//            case "put": sendPut; break;
            case "get": response = sendGet(endpoint); break;
//            case "delete": sendDelete; break;
            default:
                if (reqMethod.isEmpty()) {
                    Assert.fail("There's no request method. You are silly.");
                } else {
                    Assert.fail(reqMethod + " is not currently unsupported as a request method");
                }
        }
        return response;
    }

    public void validateResponse(String response, Map<String, String> nameValueMap) {
//todo: make this robust enough to handle JSONArrays and nested JSONs
        JSONParser parser = this.parser;
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON = (JSONObject) parser.parse(response);
        } catch (Exception e) {
            Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        Assert.assertEquals("Difference amount of elements than expected: " + response, nameValueMap.size(), responseJSON.size());

        for(Map.Entry<String, String> entry : nameValueMap.entrySet()) {
            String actualValue = (String) responseJSON.get(entry.getKey());

            if (entry.getValue().equals("*")) {
                Assert.assertFalse("Element " + entry.getKey() + " has no content" + actualValue, actualValue.isEmpty());
            } else {
                Assert.assertTrue("Expected " + entry.getKey() + " = " + entry.getValue() + " but got " + actualValue, entry.getValue().equals(actualValue));
            }
        }
    }

    public void validateResponseNoValues(String response, Map<String, String> nameMap) {
        JSONParser parser = this.parser;
//todo: logic to handle either a JSONObject or JSONArray
        JSONObject responseJSON = null;
        try {
            JSONArray responseJSONArray = (JSONArray) parser.parse(response);
// in this particular situation, I only care about getting one result
            responseJSON = (JSONObject) responseJSONArray.get(1);
        } catch (Exception e) {
            Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        Assert.assertEquals("Difference amount of elements than expected: ", nameMap.size(), responseJSON.size());

        for(Map.Entry<String, String> entry : nameMap.entrySet()) {
            String actualKey = (String) responseJSON.get(entry.getKey());
            if(actualKey == null){
                Assert.fail("Expected element \"" + entry.getKey() + "\" not found.");
            }
        }

        for(Object key : responseJSON.keySet()) {
            if(!nameMap.containsKey(key)) {
                Assert.fail("Unexpected element \"" + key + "\" found in response.");
            }
        }
    }

    public void validateResponseJSON(String response, String jSONFilename) {
        //todo: make this robust enough to handle JSONArrays and nested JSONs
        //todo maybe make parsing it's own method?
        JSONParser parser = this.parser;
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON = (JSONObject) parser.parse(response);
        } catch (Exception e) {
            Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        JSONObject expectedJSON = parseJSONFile(jSONFilename);

        Assert.assertEquals("Expected message body different than response.", expectedJSON, responseJSON);

    }

    private String sendPostMap(String endpoint, Map<String, String> nameValueMap) {
        String responseString = null;
        HttpPost httpPost = new HttpPost(validateEndpoint(endpoint));
        httpPost.setConfig(buildConfig());
        httpPost.setEntity(buildEntity(buildJSON(nameValueMap)));

        try {
            responseString = convertResponseToString(buildClient().execute(httpPost));
        } catch (IOException e) {
            Assert.fail("Problem sending POST request to " + endpoint);
        }
        return responseString;
    }

    private String sendPostJSON(String endpoint, JSONObject json) {
        String responseString = null;
        HttpPost httpPost = new HttpPost(validateEndpoint(endpoint));
        httpPost.setConfig(buildConfig());
        httpPost.setEntity(buildEntity(json));

        try {
            responseString = convertResponseToString(buildClient().execute(httpPost));
        } catch (IOException e) {
            Assert.fail("Problem sending POST request to " + endpoint);
        }
        return responseString;
    }

    private String sendGet(String endpoint) {
        String responseString = null;
        HttpGet httpGet = new HttpGet(validateEndpoint(endpoint));
        httpGet.setConfig(buildConfig());

        try {
            responseString = convertResponseToString(buildClient().execute(httpGet));
        } catch (IOException e) {
            Assert.fail("Problem sending GET request to " + endpoint);
        }
        return responseString;
    }

    private String sendGetMap(String endpoint, Map<String, String> nameValueMap) {

//todo: have this support queries and more than one value per key
//todo: make this more generic and flexible to support both queries and direct links
        for(Map.Entry<String,String> entry : nameValueMap.entrySet()) {
            //endpoint = endpoint + "?" + entry.getKey() + "=" + entry.getValue(); //format not used on the dummy
            endpoint = endpoint + "/" + entry.getValue();
        }

        return sendGet(endpoint);
    }

    private String sendGetSoapUI(String endpoint, NodeList reqParams) {

//todo: have this support queries and more than one value per key
//todo: support for query format ?id-123,name=whatever,etc
//todo: make this more generic and flexible to support both queries and direct links

//        for(int i=0; i<reqParams.getLength(); i++) {
        Node n = reqParams.item(0);
        NamedNodeMap m = n.getAttributes();
        String s = m.getNamedItem("value").toString();
        String sub = s.substring(s.indexOf("\"") + 1,s.lastIndexOf("\""));
        endpoint = endpoint + sub;

        return sendGet(endpoint);
    }

    private String sendPutMap(String endpoint, Map<String, String> nameValueMap) {
        String responseString = null;
        HttpPut httpPut = new HttpPut(validateEndpoint(endpoint) + nameValueMap.get("id"));
        // The message in this particular API has all the elements except id. That goes in the URL above.
        // nameValueMap.remove() gives me UnsupportedOperationException ಠ_ಠ
        // So I'm making a new Map with everything but "id" so I can re-use the buildJSON method.
        Map<String,String> mapWithoutId = new HashMap<String, String>();
        for(Map.Entry<String, String> entry: nameValueMap.entrySet()) {
            if(!(entry.getKey().equals("id"))) {
                mapWithoutId.put(entry.getKey(),entry.getValue());
            }
        }

        httpPut.setConfig(buildConfig());
        httpPut.setEntity(buildEntity(buildJSON(mapWithoutId)));

        try {
            responseString = convertResponseToString(buildClient().execute(httpPut));
        } catch (IOException e) {
            Assert.fail("Problem sending PUT request to " + endpoint);
        }
        return responseString;
    }

    private String sendPutJSON(String endpoint, JSONObject json) {
        // Not sure if this is the right way to do this. In order to re-use existing methods
        // I'm adding "id" to the JSON file even though the actual message won't have that
        // element in there.
        String responseString = null;
        HttpPut httpPut = new HttpPut(validateEndpoint(endpoint) + json.get("id").toString());
        httpPut.setConfig(buildConfig());
        json.remove("id");
        httpPut.setEntity(buildEntity(json));

        try {
            responseString = convertResponseToString(buildClient().execute(httpPut));
        } catch (IOException e) {
            Assert.fail("Problem sending POST request to " + endpoint);
        }
        return responseString;
    }

    private String sendPutSoapUI(String endpoint, NodeList reqParams) {

//todo: have this support queries and more than one value per key
//todo: support for query format ?id-123,name=whatever,etc
//todo: make this more generic and flexible to support both queries and direct links

//        for(int i=0; i<reqParams.getLength(); i++) {
        Node n = reqParams.item(0);
        NamedNodeMap m = n.getAttributes();
        String s = m.getNamedItem("value").toString();
        String sub = s.substring(s.indexOf("\"") + 1,s.lastIndexOf("\""));
        endpoint = endpoint + sub;

        return sendGet(endpoint);
    }

    private String convertResponseToString(HttpResponse response) {
        String responseString = null;

        try {
            int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus != 200) {
                Assert.fail("Status code " + responseStatus + " when sending request to " + endpoint);
            } else {
                responseString = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            Assert.fail("Problem reading response from request to " + endpoint);
        }

        return responseString;
    }

    private URI validateEndpoint(String endpoint) {
        URI uri = null;
        try {
            uri = new URI(endpoint);
        } catch (URISyntaxException e) {
            Assert.fail("Invalid endpoint: " + endpoint);
        }
        return uri;
    }

    private JSONObject buildJSON(Map<String, String> map) {
        JSONObject object = new JSONObject();
        for(Map.Entry<String, String> entry : map.entrySet()) {
            object.put(entry.getKey(), entry.getValue());
        }
        return object;
    }

    private HttpClient buildClient() {
        return HttpClientBuilder.create().build();
    }

    private RequestConfig buildConfig() {
        return RequestConfig.custom().setConnectTimeout(10000).build();
    }

    private StringEntity buildEntity(JSONObject object) {
        StringEntity entity = new StringEntity(object.toString(), "UTF-8");
        BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		entity.setContentType(basicHeader);
		return entity;
    }

    private JSONObject parseJSONFile(String jSONFile) {
        JSONObject jSONObjectFromFile = null;
        try {
            jSONObjectFromFile = (JSONObject) parser.parse(new FileReader(jsonFilePath + jSONFile));
        } catch (Exception e) {
            Assert.fail("Problem parsing JSON from file " + jSONFile);
        }
        return jSONObjectFromFile;
    }

    public String getId(String response) {
        JSONParser parser = this.parser;
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON = (JSONObject) parser.parse(response);
        } catch (Exception e) {
            Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        return (String) responseJSON.get("id");
    }

}
