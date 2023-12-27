package com.rest;

import com.beust.ah.A;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class JacksonAPI_JSONArray {

    ResponseSpecification customResponseSpecification;
    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                addHeader("x-mock-match-request-body","true").
                setBaseUri("https://4adabd8e-438a-4e65-b301-5081d0844c09.mock.pstmn.io").
                addHeader("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
                setConfig(config.encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        customResponseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validate_post_request_payload_as_json_array_jackson() throws JsonProcessingException {
        HashMap<String,String> obj1 = new HashMap<String,String>();
        obj1.put("name","sagar");
        obj1.put("id","1");

        HashMap<String,String> obj2 = new HashMap<String,String>();
        obj2.put("name","John");
        obj2.put("id","2");

        List<HashMap<String,String>> jsonList = new ArrayList<HashMap<String,String>>();
        jsonList.add(obj1);
        jsonList.add(obj2);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonListStr = objectMapper.writeValueAsString(jsonList);

        given().
                body(jsonListStr).
                when().
                post("/post").
                then().spec(customResponseSpecification).
                assertThat().
                body("msg",equalTo("Success"));
    }

    @Test
    public void serialize_json_array_using_jackson() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNodeList = objectMapper.createArrayNode();

        ObjectNode obj1Node = objectMapper.createObjectNode();
        obj1Node.put("name","sagar");
        obj1Node.put("id","1");

        ObjectNode obj2Node = objectMapper.createObjectNode();
        obj2Node.put("name","John");
        obj2Node.put("id","2");

        arrayNodeList.add(obj1Node);
        arrayNodeList.add(obj2Node);

        given().
                body(arrayNodeList).
        when().
                post("/post").
        then().spec(customResponseSpecification).
                assertThat().
                body("msg",equalTo("Success"));
    }
}
