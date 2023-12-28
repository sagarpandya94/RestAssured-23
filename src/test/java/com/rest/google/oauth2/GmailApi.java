package com.rest.google.oauth2;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Base64;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class GmailApi {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String token = "ya29.a0AfB_byD1Nd3_o4OnDlpfZcmlpEIZZhleFmyBJcvXrVMzgZkhlvvqX44NGl-RHbzTMJXsUaCEDGsbT7kCXBsYdlBihNHaOpzy6u6pYhItvr3PA4CXn-2CIIq8NAL9XGdnPXR5BshsbFYMFwTvru-nZx6KiB17sy4T-mGlgwaCgYKAYcSARMSFQHGX2Mio5NHcMHF5lphUHhUA6jpjw0173";

    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://gmail.googleapis.com").
                addHeader("Authorization","Bearer " + token).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);

        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);

        responseSpecification= responseSpecBuilder.build();
    }

    @Test
    public void getProfile(){
        given(requestSpecification).
                basePath("/gmail/v1").
                pathParam("userid","sagarpandya094@gmail.com").
        when().
            get("users/{userid}/profile").
        then().spec(responseSpecification);
    }

    @Test
    public void sendEmail(){
        String msg = "From: sagarpandya094@gmail.com\n" +
                "To: sagarpandya94@gmail.com\n" +
                "Subject: Test Email from Restassured\n" +
                "\n" +
                "Sending from google api using Rest assured";

        String base64UrlEncodedMsg = Base64.getUrlEncoder().encodeToString(msg.getBytes());

        HashMap<String, String> payload = new HashMap<>();
        payload.put("raw",base64UrlEncodedMsg);

        given(requestSpecification).
                basePath("/gmail/v1").
                pathParam("userid","sagarpandya094@gmail.com").
                body(payload).
        when().
                post("users/{userid}/messages/send").
        then().spec(responseSpecification);
    }
}
