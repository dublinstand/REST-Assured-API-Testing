package com.specification;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ResponseSpecificationExample {

    //we need to set up the parameters, headers, cookies for each test, so instead of setting it up every time
    //we can use request specification requestBuilder in our @BeforeClass
    private static RequestSpecBuilder requestBuilder;
    private static RequestSpecification requestSpecification;

    //This is our response request requestBuilder
    //It is used if we want to set the same set of conditions for every single response
    //for example log().all().statusCode(200)
    //or you can check some stuff in the headers is there
    //or in our case that the count is always 5
    private static ResponseSpecBuilder responseBuilder;
    private static ResponseSpecification responseSpecification;

    //Map for headers
    static Map<String, Object> responseHeaders = new HashMap<String, Object>();

    @BeforeClass
    public static void init() {
        RestAssured.baseURI = "https://query.yahooapis.com";
        RestAssured.basePath = "/v1/public";

        requestBuilder = new RequestSpecBuilder();

        //adding all params
        requestBuilder.addParam("q", "SELECT * FROM yahoo.finance.xchange WHERE pair in (\"USDINR\", \"USDCAD\",\"USDAUD\",\"USDEUR\",\"USDBRL\")");
        requestBuilder.addParam("format", "json");
        requestBuilder.addParam("diagnostics", "true");
        requestBuilder.addParam("env", "store://datatables.org/alltableswithkeys");

        //using the requestBuilder to build and assign it to our request specification
        requestSpecification = requestBuilder.build();


        //BUILDING RESPONSE
        //building the response headers
        responseHeaders.put("Content-Type","application/json;charset=utf-8");
        responseHeaders.put("Server", "ATS");

        responseBuilder = new ResponseSpecBuilder();
        //we expect the count returned always to be 5
        responseBuilder.expectBody("query.count",equalTo(5));
        //we expect the status code returned is always 200
        responseBuilder.expectStatusCode(200);
        //expect the headers that we build in our responseHeaders using expectHeaders
        responseBuilder.expectHeaders(responseHeaders);

        //we add our responseBuilder to our responseSpec
        responseSpecification = responseBuilder.build();
    }

    //1 Assert status code using request specification
    @Test
    public void assertCountValue() {
        given()
                .spec(requestSpecification)
                .log()
                .all()
                .get("/yql")
                .then()
                .spec(responseSpecification);
    }
}
