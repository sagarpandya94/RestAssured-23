package com.rest;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.*;

public class AutomateHeaders {
    String mockUrl = "https://4adabd8e-438a-4e65-b301-5081d0844c09.mock.pstmn.io";


    @Test
    public void multiple_headers(){

        Header header1 = new Header("headerName", "value2");
        Header matchHeader = new Header("x-mock-match-request-headers","headerName");

        given().
                baseUri(mockUrl).
                header(header1).
            //    header("x-mock-match-request-headers", "headerName").
                header(matchHeader).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void multiple_headers_using_Headers(){
        Header header1 = new Header("headerName", "value2");
        Header matchHeader = new Header("x-mock-match-request-headers","headerName");

        Headers headers = new Headers(header1, matchHeader);
        given().
                baseUri(mockUrl).
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void multiple_headers_using_map(){

        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value2");
        headers.put("x-mock-match-request-headers","headerName");

        given().
                baseUri(mockUrl).
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void multi_value_headers_using_headers(){
        Header header1 = new Header("multivalueheader", "dummyvalue1");
        Header header2 = new Header("multivalueheader", "dummyvalue2");
        Headers headers = new Headers(header1, header2);

        given().
                baseUri(mockUrl).
                headers(headers).
                log().headers().
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void assert_response_headers(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value1");
        headers.put("x-mock-match-request-headers","headerName");

        given().
                baseUri(mockUrl).
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200).headers("responseHeader","resValue1","X-RateLimit-Limit","120");
    }

    @Test
    public void extract_response_headers(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value1");
        headers.put("x-mock-match-request-headers","headerName");

        Headers extractedHeaders = given().
                baseUri(mockUrl).
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().statusCode(200).extract().headers();

        System.out.println("Extracted headers" + extractedHeaders);

        for(Header header: extractedHeaders){
            System.out.print("header name = " + header.getName() + ", " );
            System.out.println("header value = " + header.getValue());
        }
    }

    @Test
    public void extract_multi_response_headers(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value1");
        headers.put("x-mock-match-request-headers","headerName");

        Headers extractedHeaders = given().
                baseUri(mockUrl).
                headers(headers).
                when().
                get("/get").
                then().
                assertThat().statusCode(200).extract().headers();

        System.out.println("Extracted headers" + extractedHeaders);

        List<String> values = extractedHeaders.getValues("multiValueHeader");
        for(String value: values){
            System.out.println(value);
        }
    }
}
