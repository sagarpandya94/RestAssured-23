package com.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JacksonAPI_JSONObject {

    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.postman.com").
                addHeader("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }
    @Test
    public void validate_post_request_payload_using_map_jackson() throws JsonProcessingException {
        HashMap<String, Object> mainObj = new HashMap<String,Object>();

        HashMap<String, String> nestedObj = new HashMap<String, String>();
        nestedObj.put("name","Workspace created from map in jackson");
        nestedObj.put("type","personal");
        nestedObj.put("description","Hello");

        mainObj.put("workspace",nestedObj);

        ObjectMapper objectMapper = new ObjectMapper();
        String mainObjectStr = objectMapper.writeValueAsString(mainObj);

        given().
                body(mainObjectStr).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name",is(equalTo("Workspace created from map in jackson")),
                        "workspace.id",matchesPattern("^[a-z0-9-]{36}$"));

    }

    @Test
    public void validate_post_request_payload_using_jackson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode nestedObjNode = objectMapper.createObjectNode();
        nestedObjNode.put("name","Workspace created from map in jackson1");
        nestedObjNode.put("type","personal");
        nestedObjNode.put("description","Hello");

        ObjectNode mainObjNode = objectMapper.createObjectNode();
        mainObjNode.set("workspace",nestedObjNode);

        given().
                body(mainObjNode).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name",is(equalTo("Workspace created from map in jackson1")),
                        "workspace.id",matchesPattern("^[a-z0-9-]{36}$"));

    }
}
