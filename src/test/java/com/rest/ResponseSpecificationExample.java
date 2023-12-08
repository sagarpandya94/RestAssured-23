package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ResponseSpecificationExample {

    ResponseSpecification responseSpecification;
    @BeforeClass
    public void beforeClass(){
        /* requestSpecification = with().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b"); */

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://api.postman.com");
        requestSpecBuilder.addHeader("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b");
        requestSpecBuilder.log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        responseSpecification = RestAssured.expect().statusCode(200).contentType(ContentType.JSON);
    }

    @org.testng.annotations.Test
    public void validate_get_status_code(){
        get("/workspaces").
                then().spec(responseSpecification).
                    log().all();
    }

    @Test
    public void validate_response_body(){
        Response response = get("/workspaces").
                then().spec(responseSpecification).
                log().all().
                extract().response();
        assertThat(response.<String>path("workspaces[0].name"),is(equalTo("My Workspace")));
    }

}
