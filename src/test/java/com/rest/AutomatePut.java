package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AutomatePut {
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
    public void validate_put_request_bdd_Style(){
        String workspaceId = "11970b95-ebef-4ee2-b56b-5696a06a85c9";

        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"new Workspace from automation non BDD style\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\": \"Description 1\"\n" +
                "    }\n" +
                "}";

        given().
                body(payload).
                pathParam("workspaceId",workspaceId).
        when().
                put("/workspaces/{workspaceId}").
        then().
            log().all().
                assertThat().
                body("workspace.name",is(equalTo("new Workspace from automation non BDD style")),
                        "workspace.id",matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id",equalTo(workspaceId));
    }
}
