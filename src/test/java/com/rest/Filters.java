package com.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

public class Filters{

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() throws FileNotFoundException {
        PrintStream FileOutputStream1 = new PrintStream(new File("restassured1.log"));

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                addFilter(new RequestLoggingFilter(FileOutputStream1)).
                addFilter(new ResponseLoggingFilter(FileOutputStream1));
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecification = responseSpecBuilder.build();
    }
    @Test
    public void validateJsonSchema(){
        given().
                baseUri("https://postman-echo.com").
                filter(new RequestLoggingFilter(LogDetail.BODY)).
                filter(new ResponseLoggingFilter(LogDetail.STATUS)).
        when().
                get("/get").
        then().
                assertThat().statusCode(200);
    }

    @Test
    public void loggingFilter() throws FileNotFoundException {
        PrintStream FileOutputStream = new PrintStream(new File("restassured.log"));

        given().
                baseUri("https://postman-echo.com").
                filter(new RequestLoggingFilter(LogDetail.BODY, FileOutputStream)).
                filter(new ResponseLoggingFilter(LogDetail.STATUS, FileOutputStream)).
        when().
                get("/get").
        then().
                assertThat().statusCode(200);
    }

    @Test
    public void loggingFilter_global() {
        given(requestSpecification).
                baseUri("https://postman-echo.com").
        when().
                get("/get").
        then().spec(responseSpecification).
                assertThat().statusCode(200);
    }
}
