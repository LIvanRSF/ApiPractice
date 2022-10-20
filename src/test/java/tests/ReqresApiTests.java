package tests;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specification.baseRequest;
import static specs.Specification.baseResponse;
import static specs.Specification.createUserResponse;
import static specs.Specification.errorUserResponse;

import models.lombok.register.RegisterUserResponseLombokModel;
import models.lombok.singleuser.SingleUserResponseLombokModel;
import models.pojo.CreateUserPojoModel;
import models.pojo.RegisterUserPojoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ReqresApiTests {

    @DisplayName("Проверяем ссылку поддержки из ответа")
    @Tag("api")
    @Test
    void getSingleUserSupportURL() {
        String expectedSupportUrl = "https://reqres.in/#support-heading";

        SingleUserResponseLombokModel response = given()
            .spec(baseRequest)
            .when()
            .get("/users/2")
            .then()
            .spec(baseResponse)
            .extract().as(SingleUserResponseLombokModel.class);

        assertThat(response.getSupport().getUrl()).isEqualTo(expectedSupportUrl);
    }

    @DisplayName("Проверяем ссылку поддержки из ответа, используя extract\\path")
    @Tag("api")
    @Test
    void getWithPathSingleUser() {
        String expectedSupportUrl = "https://reqres.in/#support-heading";

        String actualResponseUrl = given()
            .spec(baseRequest)
            .when()
            .get("/users/2")
            .then()
            .spec(baseResponse)
            .extract()
            .path("support.url");

        assertEquals(expectedSupportUrl, actualResponseUrl);
    }

    @DisplayName("Создали юзера, проверили имя и работу")
    @Tag("api")
    @Test
    void postCreateUser() {
        CreateUserPojoModel createUser = new CreateUserPojoModel();
        createUser.setJob("rebel");
        createUser.setName("Neo");

        CreateUserPojoModel user = given()
            .spec(baseRequest)
            .body(createUser)
            .when()
            .post("/users")
            .then()
            .spec(createUserResponse)
            .extract().as(CreateUserPojoModel.class);

        assertThat(user.getName()).isEqualTo("Neo");
        assertThat(user.getJob()).isEqualTo("rebel");
    }

    @DisplayName("Зарегистрировались, получили id и token")
    @Tag("api")
    @Test
    void postSuccessRegister() {
        RegisterUserPojoModel user = new RegisterUserPojoModel();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        RegisterUserResponseLombokModel register = given()
            .spec(baseRequest)
            .body(user)
            .when()
            .post("/register")
            .then()
            .spec(baseResponse)
            .extract().as(RegisterUserResponseLombokModel.class);

        assertThat(register.getId()).isEqualTo("4");
        assertThat(register.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @DisplayName("Ошибка регистрации. Пользователь не определен.")
    @Tag("api")
    @Test
    void postUnsuccessfulRegistration() {
        RegisterUserPojoModel user = new RegisterUserPojoModel();
        user.setEmail("bad@email.in");
        user.setPassword("pistol");

        RegisterUserResponseLombokModel register = given()
            .spec(baseRequest)
            .body(user)
            .when()
            .post("/register")
            .then()
            .spec(errorUserResponse)
            .extract().as(RegisterUserResponseLombokModel.class);

        assertThat(register.getError()).isEqualTo("Note: Only defined users succeed registration");
    }
}
