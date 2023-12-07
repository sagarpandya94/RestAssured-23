package com.rest;

import io.restassured.config.LogConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomateGet {

    String apikey = "PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b";
    @Test
    public void validate_get_status_code(){
        given().
                baseUri("https://api.postman.com").
                header("x-api-key",apikey).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void validate_response_body(){
        given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                body("workspaces.name",hasItems("My Workspace", "Alexis"),
                        "workspaces.type", hasItem("personal"),
                        "workspaces.name[0]",is(equalTo("My Workspace")),
                        "workspaces.size()",is(equalTo(2)));
    }

    @Test
    public void extract_response(){
        Response res = given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                extract().response();

        System.out.println("Response is" + res.asString());
    }

    @Test
    public void extract_single_value_from_response(){
        Response res = given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                extract().response();

        JsonPath jsonPath = new JsonPath(res.asString());
        System.out.println("First workspace name:"+ jsonPath.getString("workspaces[0].name"));
        // System.out.println("First workspace name:" + res.path("workspaces[0].name"));
    }

    @Test
    public void extract_single_value_from_response_1(){
        String res = given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
                when().
                get("/workspaces").
                then().
                assertThat().statusCode(200).
                extract().response().path("workspaces[0].name");

        Assert.assertEquals(res, "My Workspace");
        assertThat(res, equalTo("My Workspace"));
    }

    @Test
    public void validate_hemcrestmethods(){
        given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                body("workspaces.name",containsInAnyOrder("Alexis","My Workspace"),
                        "workspaces.name",is(not(empty())),
                        "workspaces.name",hasSize(2),
       //                 "workspaces.name",everyItem(startsWith("My"))
                        "workspaces[0]",hasKey("id"),
                        "workspaces[1]",hasValue("ba928b1d-b640-40a9-a4e7-c18e88eccca5"),
                        "workspaces[1]",hasEntry("id","ba928b1d-b640-40a9-a4e7-c18e88eccca5"),
                        "workspaces[0]",not(equalTo(Collections.EMPTY_MAP)),
                        "workspaces[0].name", allOf(startsWith("My"), containsString("Work"))
                );
    }

    @Test
    public void request_response_logging(){
        given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
                log().all().
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void request_response_conditional_logging(){
        Set<String> headers = new HashSet<>();
        headers.add("x-api-key");
        headers.add("Accept");
        given().
                baseUri("https://api.postman.com").
                header("x-api-key",apikey).
                //config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
                config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))).
                log().all().
        when().
                get("/workspaces").
        then().
                log().ifError().
                //log().ifValidationFails()
                assertThat().statusCode(200);
    }

}
