package com.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class RequestSpecificationExample {
    RequestSpecification requestSpecification;
    @BeforeClass
    public void beforeClass(){
        requestSpecification = with().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b");

        /* RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://api.postman.com");
        requestSpecBuilder.addHeader("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b");
        requestSpecBuilder.log(LogDetail.ALL);

        requestSpecification = requestSpecBuilder.build();

        Need to use given method in the cases if we use RequestSpecBuilder - can't do
        */
    }

    @org.testng.annotations.Test
    public void validate_get_status_code(){
        Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));

        /* given().spec(requestSpecification).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().statusCode(200); */
    }

    @Test
    public void validate_response_body(){
        Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(),is(equalTo(200)));
        assertThat(response.<String>path("workspaces[0].name"),is(equalTo("My Workspace")));

        /* given().spec(requestSpecification).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                body("workspaces.name",hasItems("My Workspace", "Alexis"),
                        "workspaces.type", hasItem("personal"),
                        "workspaces.name[0]",is(equalTo("My Workspace")),
                        "workspaces.size()",is(equalTo(2))); */
    }
}
