package com.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.pojo.simple.SimplePojo;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SimplePojoTest {
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://4adabd8e-438a-4e65-b301-5081d0844c09.mock.pstmn.io").
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void simple_pojo_example(){
        SimplePojo simplePojo = new SimplePojo("value1","value2");
        given().
                body(simplePojo).
        when().
                post("/postSimpleJson").
        then().spec(responseSpecification).
                assertThat().body("key1",equalTo(simplePojo.getKey1()), "key2",equalTo(simplePojo.getKey2()));
    }

    @Test
    public void simple_pojo_deserialize() throws JsonProcessingException {
        SimplePojo simplePojo = new SimplePojo("value1","value2");
        SimplePojo deserializedPojo = given().
                body(simplePojo).
        when().
                post("/postSimpleJson").
        then().spec(responseSpecification).
                extract().response().as(SimplePojo.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String deserializedPojoStr = objectMapper.writeValueAsString(deserializedPojo);
        String simplePojoStr = objectMapper.writeValueAsString(simplePojo);
        assertThat(objectMapper.readTree(deserializedPojoStr),equalTo(objectMapper.readTree(simplePojoStr)));
    }
}
