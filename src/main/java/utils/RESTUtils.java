package utils;


//import jdk.nashorn.internal.parser.JSONParser;

import com.google.common.io.CharStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.junit.Assert;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

//import org.json.JSONObject; // has toMap, FileReader doesn't work
import org.json.simple.JSONObject; // has no toMap, FileReader doesn't work

//import com.google.gson.JSONObject;
//import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class RESTUtils {

    private String endpoint;
    private static String absPath = new File("").getAbsolutePath();
    private static String filePath = absPath + "/src/main/resources/json/";
    private JSONParser parser = new JSONParser();

    public String sendRequest(String reqMethod, String endpoint, Map<String, String> nameValueMap) {
        String responseString = null;
        this.endpoint = endpoint;
        switch(reqMethod.toLowerCase()) {
            case "post": responseString = sendPost(endpoint, nameValueMap); break;
//            case "put": sendPut; break;
//            case "get": sendGet; break;
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
        switch(reqMethod.toLowerCase()) {
            case "post": response = sendPost(endpoint, jSONFile); break;
//            case "put": sendPut; break;
//            case "get": sendGet; break;
//            case "delete": sendDelete; break;
            default:
                if (reqMethod.isEmpty()) {
                    Assert.fail("There's no request method. You are silly.");
                } else {
                    Assert.fail(reqMethod + " is currently unsupported as a request method");
                }
        }

        return response;
    }

    public void validateResponse(String response, Map<String, String> nameValueMap) {
//        JSONParser parser = this.parser;
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON = (JSONObject) parser.parse(response);
        } catch (Exception e) {
            Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        Assert.assertTrue("Expected " + nameValueMap.size() + " elements in response but got " + responseJSON.size(), responseJSON.size() == nameValueMap.size());

        for(Map.Entry<String, String> entry : nameValueMap.entrySet()) {
            String actualValue = (String) responseJSON.get(entry.getKey());

            if (entry.getValue().equals("*")) {
                Assert.assertFalse("Element " + entry.getKey() + " has no content" + actualValue, actualValue.isEmpty());
            } else {
                Assert.assertTrue("Expected " + entry.getKey() + " = " + entry.getValue() + " but got " + actualValue, entry.getValue().equals(actualValue));
            }
        }
    }

    private String sendPost(String endpoint, Map<String, String> nameValueMap) {
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

    private String sendPost(String endpoint, String jSONFile) {
        String responseString = null;
        HttpPost httpPost = new HttpPost(validateEndpoint(endpoint));
        httpPost.setConfig(buildConfig());
        JSONObject jSONObjectFromFile = parseJSONFile (jSONFile);
        httpPost.setEntity(buildEntity((JSONObject) jSONObjectFromFile));

        try {
            responseString =  convertResponseToString(buildClient().execute(httpPost));
        } catch (IOException e) {
            Assert.fail("Problem sending POST request to " + endpoint);
        }
        return responseString;
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
            jSONObjectFromFile = (JSONObject) parser.parse(new FileReader(filePath + jSONFile));
        } catch (Exception e) {
            Assert.fail("Problem parsing JSON from file " + jSONFile);
        }
        return jSONObjectFromFile;
    }

//    HttpClient httpClient = HttpClientBuilder.create().build();
//    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).build();
//    HttpPost httpPost = new HttpPost("http://frengly.com/frengly/data/translateREST");
//		httpPost.setConfig(requestConfig);
//
//    StringEntity entity = new StringEntity(object.toJSONString(), "UTF-8");
//    BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
//		entity.setContentType(basicHeader);
//		httpPost.setEntity(entity);
//
//    HttpResponse response = httpClient.execute(httpPost);
//    InputStream is = response.getEntity().getContent();
//    String strResponse = IOUtils.toString(is, "UTF-8");
//
//		System.out.println(strResponse);
}
