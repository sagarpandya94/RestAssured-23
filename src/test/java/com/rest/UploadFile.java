package com.rest;

import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
public class UploadFile {
    @Test
    public void upload_file(){
        String attributes = "{\"name\":\"temp.txt\"}";

        given().
                baseUri("https://postman-echo.com").
                multiPart("file", new File("src/main/resources/temp.txt")).
                multiPart("attributes",attributes,"application/json").
        when().
                post("/post").
        then().
                log().all().assertThat().statusCode(200);
    }
}
