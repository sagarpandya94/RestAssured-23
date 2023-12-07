package com.rest;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class Test {

    @org.testng.annotations.Test
    public void first_test_case(){
        given().
                baseUri("https://api.postman.com").
                header("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
            when().
                get("/workspaces").
            then().
                statusCode(200);
    }
}
