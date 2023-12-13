package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AutomatePost {

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
    public void validate_post_request_bdd_style(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"Workspace from automation\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\": \"RestAssured is creating this workspace\"\n" +
                "    }\n" +
                "}";

        given().
                body(payload).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name",is(equalTo("Workspace from automation")),
                        "workspace.id",matchesPattern("^[a-z0-9-]{36}$"));

    }

    @Test
    public void validate_post_request_non_bdd_style(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"Workspace from automation non BDD style\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\": \"RestAssured is creating this workspace\"\n" +
                "    }\n" +
                "}";

        Response response = with().
                body(payload).
                post("/workspaces");
        assertThat(response.<String>path("workspace.name"),equalTo("Workspace from automation non BDD style"));
        assertThat(response.<String>path("workspace.id"),matchesPattern("^[a-z0-9-]{36}$"));
    }


    @Test
    public void validate_post_request_payload_from_file(){
        File file = new File("src/main/resources/CreateWorkspacePayload.json");

        given().
                body(file).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name",is(equalTo("Workspace from automation using a file")),
                        "workspace.id",matchesPattern("^[a-z0-9-]{36}$"));

    }


    @Test
    public void validate_post_request_payload_using_map(){
        HashMap<String, Object> mainObj = new HashMap<String,Object>();

        HashMap<String, String> nestedObj = new HashMap<String, String>();
        nestedObj.put("name","Workspace created from map");
        nestedObj.put("type","personal");
        nestedObj.put("description","Hello");

        mainObj.put("workspace",nestedObj);

        given().
                body(mainObj).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name",is(equalTo("Workspace created from map")),
                        "workspace.id",matchesPattern("^[a-z0-9-]{36}$"));

    }

}
