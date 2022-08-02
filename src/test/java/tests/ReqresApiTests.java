package tests;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ReqresApiTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @DisplayName("Проверяем ссылку поддержки из ответа")
    @Tag("api")
    @Test
    void getSingleUserSupportURL() {
        given()
            .log().uri()
            .log().body()
            .when()
            .get("/users/2")
            .then()
            .log().status()
            .log().body()
            .body("support.url", is("https://reqres.in/#support-heading"))
            .statusCode(200);
    }

    @DisplayName("Проверяем ссылку поддержки из ответа, используя extract\\path")
    @Tag("api")
    @Test
    void getWithPathSingleUser() {
        String expectedSupportUrl = "https://reqres.in/#support-heading";

        String actualResponseUrl = given()
            .log().uri()
            .log().body()
            .contentType(JSON)
            .when()
            .get("/users/2")
            .then()
            .log().status()
            .log().body()
            .extract()
            .path("support.url");

        assertEquals(expectedSupportUrl, actualResponseUrl);
    }

    @DisplayName("Создали юзера, проверили имя и работу")
    @Tag("api")
    @Test
    void postCreateUser() {
        String name = "Neo";
        String job = "rebel";
        String body = "{ \"name\": \"" + name + "\",\"job\": \"" + job + "\" }";

        given()
            .log().uri()
            .log().body()
            .contentType(JSON)
            .body(body)
            .when()
            .post("/users")
            .then()
            .log().status()
            .log().body()
            .body("name", is(name))
            .body("job", is(job))
            .statusCode(201);
    }

    @DisplayName("Зарегистрировались, получили id и token")
    @Tag("api")
    @Test
    void postSuccessRegister() {
        String body = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";

        given()
            .log().uri()
            .log().body()
            .contentType(JSON)
            .body(body)
            .when()
            .post("/register")
            .then()
            .log().status()
            .log().body()
            .body("id", is(4))
            .body("token", is("QpwL5tke4Pnpja7X4"))
            .statusCode(200);
    }

    @DisplayName("Ошибка регистрации. Пользователь не определен.")
    @Tag("api")
    @Test
    void postUnsuccessfulRegistration() {
        String body = "{\"email\": \"bad@email.in\",\"password\": \"pistol\"}";

        given()
            .log().uri()
            .log().body()
            .contentType(JSON)
            .body(body)
            .when()
            .post("/register")
            .then()
            .log().status()
            .log().body()
            .body("error", is("Note: Only defined users succeed registration"))
            .statusCode(400);
    }
}
