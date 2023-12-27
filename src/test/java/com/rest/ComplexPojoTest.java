package com.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.pojo.collection.*;
import com.rest.pojo.workspace.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ComplexPojoTest {
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.getpostman.com").
                addHeader("x-api-key","PMAK-656434cc4607b765340353a5-22c3a511abf383b861745b2f678009575b").
                addQueryParam("workspaceId","ba928b1d-b640-40a9-a4e7-c18e88eccca5").
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
    public void complex_pojo_create_collection() throws JsonProcessingException, JSONException {
        Header header = new Header("Content-Type","application/json");
        List<Header> headerList = new ArrayList<Header>();
        headerList.add(header);

        Body body = new Body("raw","{\\\"data\\\": \\\"123\\\"}");

        Request request = new Request("https://postman-echo.com/post","POST",headerList,body,"This is a sample post request");

        RequestRoot requestRoot = new RequestRoot("Sample POST request",request);
        List<RequestRoot> requestRootList = new ArrayList<RequestRoot>();
        requestRootList.add(requestRoot);

        Folder folder = new Folder("This is a folder",requestRootList);
        List<Folder> folderList = new ArrayList<Folder>();
        folderList.add(folder);

        Info info = new Info("Sample Collection11","just a sample collection","https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        Collection collection = new Collection(info, folderList);

        CollectionRoot collectionRoot = new CollectionRoot(collection);

        String collectionUid = given().
                body(collectionRoot).
        when().
                post("/collections").
        then().spec(responseSpecification).extract().path("collection.uid");

        CollectionRoot deserializedCollectionRoot = given().
                pathParam("collectionUid",collectionUid).
        when().
                get("collections/{collectionUid}").
        then().spec(responseSpecification).
                extract().response().as(CollectionRoot.class);

        ObjectMapper objectMapper= new ObjectMapper();
        String collectionRootStr = objectMapper.writeValueAsString(collectionRoot);
        String deserializedRootStr = objectMapper.writeValueAsString(deserializedCollectionRoot);

        JSONAssert.assertEquals(collectionRootStr,deserializedRootStr,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("collection.item[*].item[*].request.url", new ValueMatcher<Object>() {
                            @Override
                            public boolean equal(Object o, Object t1) {
                                return true;
                            }
                        })));
    }
}
