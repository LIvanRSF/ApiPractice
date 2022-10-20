package specs;

import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specification {

    public static RequestSpecification baseRequest = with()
        .baseUri("https://reqres.in/api")
        .log().uri()
        .log().body()
        .contentType(JSON);

    public static ResponseSpecification baseResponse = new ResponseSpecBuilder()
        .log(LogDetail.BODY)
        .log(LogDetail.STATUS)
        .expectStatusCode(200)
        .build();

    public static ResponseSpecification createUserResponse = new ResponseSpecBuilder()
        .log(LogDetail.BODY)
        .log(LogDetail.STATUS)
        .expectStatusCode(201)
        .build();

    public static ResponseSpecification errorUserResponse = new ResponseSpecBuilder()
        .log(LogDetail.BODY)
        .log(LogDetail.STATUS)
        .expectStatusCode(400)
        .build();
}
