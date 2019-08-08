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
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.xml.sax.InputSource;

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

public class RESTUtils2 {

    private static String absPath = new File("").getAbsolutePath();
    private static String jsonFilePath = absPath + "/src/main/resources/json/";
    private static String soapUiFilePath = absPath + "/src/main/resources/soapui/";
    private JSONParser parser = new JSONParser();

   public String sendRequest(String method, String url, List<Map<String, String>> map) {
       String responseString = null;
       URI uri = buildURI(map, url);
       switch(method.toLowerCase()) {
           case "post":
               JSONObject body = buildJSON(map,"");
               responseString = sendPost(uri, body);
               break;
           case "get":
               responseString = sendGet(uri);
               break;
       }
       return responseString;
   }

   public String sendJSONRequest(String method, String url, String jsonFileName, List<Map<String, String>> map) {
        String responseString = null;
        URI uri = buildURI(map, url);
        String jsonString = null;
        try {
            jsonString = new String (Files.readAllBytes(Paths.get(jsonFilePath + jsonFileName)));
        } catch (Exception e) {
            System.out.println("Problem reading JSON file " + jsonFileName);
        }
        JSONObject body = buildJSON(map, jsonString);
        switch(method.toLowerCase()) {
            case "post":
                responseString = sendPost(uri, body);
                break;
        }
        return responseString;
    }

   public String sendSoapUIRequest(String reqName, String soapUIFile, List<Map<String, String>> map) {
        String responseString = null;
        File file = null;
        Document soapUIDoc = null;
        String method = null;
        URI uri = null;
        JSONObject json = null;
        XPath xPath =  XPathFactory.newInstance().newXPath();

        try {
            String xmlFunTiems = new String (Files.readAllBytes(Paths.get(soapUiFilePath + soapUIFile)));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlFunTiems));
            soapUIDoc = dBuilder.parse(is);
            soapUIDoc.getDocumentElement().normalize();
        } catch (Exception e) {
            Assert.fail("Problem reading SoapUI project file " + soapUIFile);
        }

       try {
           uri = buildURI(map, (String) xPath.compile("*//request[@name=\"" + reqName + "\"]/originalUri/text()").evaluate(soapUIDoc, XPathConstants.STRING));
       } catch (Exception e) {
           Assert.fail("Problem finding url for request " + reqName + "in " + soapUIFile);
       }

       try {
           method = (String) xPath.compile("*//request[@name=\"" + reqName + "\"]//ancestor::method/@method").evaluate(soapUIDoc, XPathConstants.STRING);
       } catch (Exception e) {
           Assert.fail("Problem finding method for request " + reqName + "in " + soapUIFile);
       }

        try {
            json = buildJSON(map, (String) xPath.compile("*//request[@name=\"" + reqName + "\"]/request").evaluate(soapUIDoc, XPathConstants.STRING));
        } catch (Exception e) {
            System.out.println("Error when parsing JSON body from request " + reqName + " in SoapUI project " + soapUIFile);
        }

        switch(method.toLowerCase()) {
            case "post":
                responseString = sendPost(uri, json);
                break;
        }
        return responseString;
   }

   private URI buildURI(List<Map<String, String>> listMap, String url) {
       URI uri = null;
       boolean firstOne = true;

       for(Map<String,String> map:listMap) {
           if(map.get("type").equals("query")) {
               if(firstOne) {
                   url = url + "?" + map.get("name") + "=" + map.get("value");
                   firstOne = false;
               } else {
                   url = url + "&" + map.get("name") + "=" + map.get("value");
               }
           }
       }

       for(Map<String,String> map:listMap) {
           if(map.get("type").equals("segment")) {
               url = url.replace("{" + map.get("key") + "}",map.get("value"));
           }
       }

       try {
           uri = new URI(url.replace(" ","%20"));
       } catch (URISyntaxException e) {
           Assert.fail("Invalid endpoint: " + url);
       }

       return uri;
   }

   private JSONObject buildJSON(List<Map<String, String>> listMap, String jsonString) {
        JSONObject jsonObjFromMap = new JSONObject();
        JSONObject jsonObjFromString = new JSONObject();
        JSONObject jsonObjReturned = new JSONObject();

        // Meant to handle stuff coming from SoapUI
        if(jsonString.length() > 0) {
            try {
                jsonObjFromString = (JSONObject) parser.parse(jsonString);
            } catch (Exception e) {
                System.out.println("Problem parsing string to JSONObject: " + jsonString);
            }
        }

        if(listMap.size() > 0) {
           for(Map<String, String> entry: listMap) {
               if((entry.get("type").equals("body"))){
                   if(jsonObjFromMap.containsKey(entry.get("key"))) {
                       System.out.println("replacing key \"" + entry.get("key") + "\", value " + jsonObjFromMap.get(entry.get("key")) +
                               " with value \"" + entry.get("value") + "\"");
                   }
                   jsonObjFromMap.put(entry.get("key"),entry.get("value"));
               }
           }
       }

       try {
           for (Object key : jsonObjFromString.keySet()) {
               jsonObjReturned.put(key, jsonObjFromString.get(key));
           }
       } catch (Exception e) {
           Assert.fail("Problem building message body");
       }

       try {
           for (Object key : jsonObjFromMap.keySet()) {
               if(jsonObjReturned.containsKey(key)) {
                   System.out.println("replacing key \"" + key + "\", value " + jsonObjReturned.get(key) +
                           " with value \"" + jsonObjFromMap.get("key") + "\"");
               }
               jsonObjReturned.put(key, jsonObjFromMap.get(key));
           }
       } catch (Exception e) {
           Assert.fail("Problem building message body");
       }

       return jsonObjReturned;
    }

    // needed?
   //private JSONObject parseJSONFile(String jSONFile) {
//        JSONObject jSONObjectFromFile = null;
//        try {
//            jSONObjectFromFile = (JSONObject) parser.parse(new FileReader(jsonFilePath + jSONFile));
//        } catch (Exception e) {
//            Assert.fail("Problem parsing JSON from file " + jSONFile);
//        }
//        return jSONObjectFromFile;
//    }

//    getSoapUIBody
//            getSoapUIMethod

   private String sendPost(URI uri, JSONObject body) {
       String responseString = null;
       HttpPost httpPost = new HttpPost(uri);
       httpPost.setConfig(buildConfig());
       httpPost.setEntity(buildEntity(body));

       try {
           responseString = convertResponseToString(buildClient().execute(httpPost), uri);
       } catch (IOException e) {
           Assert.fail("Problem sending POST request to " + uri);
       }
       return responseString;
   }

    private String sendGet(URI uri) {
        String responseString = null;
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(buildConfig());

        try {
            responseString = convertResponseToString(buildClient().execute(httpGet), uri);
        } catch (IOException e) {
            Assert.fail("Problem sending POST request to " + uri);
        }
        return responseString;
    }

   private String convertResponseToString(HttpResponse response, URI uri) {
        String responseString = null;

        try {
            int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus != 200) {
                Assert.fail("Status code " + responseStatus + " when sending request to " + uri);
            } else {
                responseString = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            Assert.fail("Problem reading response from request to " + uri);
        }

        return responseString;
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

   public void validateJSONResponse(String response, String jSONFilename, Map<String, String> nameValueMap) {
        //todo: make this robust enough to handle JSONArrays and nested JSONs
        //todo maybe make parsing it's own method?
        JSONParser parser = this.parser;
        JSONObject responseJSON = new JSONObject();
        JSONObject fileJSON = new JSONObject();

        try {
            responseJSON = (JSONObject) parser.parse(response);
        } catch (Exception e) {
            Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        try {
           fileJSON = (JSONObject) parser.parse(new String (Files.readAllBytes(Paths.get(jsonFilePath + jSONFilename))));
        } catch (Exception e) {
           Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        for(Map.Entry<String, String> entry: nameValueMap.entrySet()) {
            fileJSON.put(entry.getKey(),entry.getValue());
        }

        Assert.assertEquals("number of JSON entries not same",fileJSON.size(), responseJSON.size());

        for (Object key: fileJSON.keySet()) {
            if(responseJSON.containsKey(key)) {
                Assert.assertEquals("Value is different for key " + key,responseJSON.get(key),fileJSON.get(key));
            } else {
                Assert.fail("Unexpected key:" + key);
            }
        }
    }

   public String getResponseValue(String name, String response) {
        JSONParser parser = this.parser;
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON = (JSONObject) parser.parse(response);
        } catch (Exception e) {
            Assert.fail("Problem parsing response into a JSONObject: " + response);
        }

        return (String) responseJSON.get(name);
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
}