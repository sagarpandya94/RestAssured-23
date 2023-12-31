package com.rest;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FormAuthentication {
    @BeforeClass
    public void beforeClass(){
        RestAssured.requestSpecification = new RequestSpecBuilder().
                setRelaxedHTTPSValidation().
                setBaseUri("https://localhost:8443").build();
    }

    @Test
    public void form_authentication_using_csrf_token(){
        SessionFilter filter = new SessionFilter();

        given().
                auth().form("dan","dan123",new FormAuthConfig("/signin","txtUsername","txtPassword").withAdditionalField("_csrf")).
                filter(filter).
                log().all().
        when().
                get("/login").
        then().
                log().all().assertThat().
                statusCode(200);

        System.out.println("Session id is:" + filter.getSessionId());

        given().
                sessionId(filter.getSessionId()).
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("html.body.div.p",equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void form_authentication_using_csrf_token_cookie(){
        SessionFilter filter = new SessionFilter();

        given().
                auth().form("dan","dan123",new FormAuthConfig("/signin","txtUsername","txtPassword").withAdditionalField("_csrf")).
                filter(filter).
                log().all().
                when().
                get("/login").
                then().
                log().all().assertThat().
                statusCode(200);

        System.out.println("Session id is:" + filter.getSessionId());

        Cookie cookie = new Cookie.Builder("JSESSIONID", filter.getSessionId()).setHttpOnly(true).setComment("My cookie").build();
        Cookie cookie1 = new Cookie.Builder("Name","Sagar").build();

        Cookies cookies = new Cookies(cookie, cookie1);

        given().
            //cookie("JSESSIONID", filter.getSessionId()).
            //        cookie(cookie).
                    cookies(cookies).
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("html.body.div.p",equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void fetch_a_cookie(){
        Response response = given().
                log().all().
        when().
                get("/profile/index").
        then().
                extract().response();

        System.out.println(response.getCookie("JSESSIONID"));
    }

    @Test
    public void fetch_multiple_cookies(){
        Response response = given().
                log().all().
                when().
                get("/profile/index").
                then().
                extract().response();

        Map<String,String> cookies = response.getCookies();

        for(Map.Entry<String,String> entry: cookies.entrySet()){
            System.out.println("Cookie name" + entry.getKey());
            System.out.println("Cookie value" + entry.getValue());
        }

        Cookies cookies1 = response.getDetailedCookies();
        List<Cookie> cookieList = cookies1.asList();

        for (Cookie cookie:cookieList){
            System.out.println("Cookie:" + cookie.toString());
        }
    }
}
