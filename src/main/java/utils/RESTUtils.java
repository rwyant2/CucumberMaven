package utils;


import gherkin.deps.com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.junit.Assert;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import java.net.URISyntaxException;
import java.util.Map;
import java.net.URI;

public class RESTUtils {

    public String sendRequest(String reqMethod, String endpoint, Map<String, String> nameValueMap) {
        String response = null;
        switch(reqMethod.toLowerCase()) {
            case "post": response = sendPost(endpoint, nameValueMap); break;
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
        JSONObject responseJSON = new JSONObject(response);
        Map<String, Object> responseMap = responseJSON.toMap();
        Assert.assertTrue("Expected " + nameValueMap.size() + " elements in response but got " + responseMap.size(), responseMap.size() == nameValueMap.size());

        for(Map.Entry<String, String> entry : nameValueMap.entrySet()) {
            String actualValue = (String) responseMap.get(entry.getKey());

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

        HttpResponse response = null;
        try {
            response = buildClient().execute(httpPost);
        } catch (IOException e) {
            Assert.fail("Problem sending POST request to " + endpoint);
        }

        try {
            int responseStatus = response.getStatusLine().getStatusCode();
            if(responseStatus != 200) {
                Assert.fail("Status code " + responseStatus + " when sending POST request to " + endpoint);
            } else {
                responseString = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            Assert.fail("Problem reading response from POST request to to " + endpoint);
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
