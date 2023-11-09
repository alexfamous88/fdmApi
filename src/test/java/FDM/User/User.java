package FDM.User;

import FDM.Specifications;
import FDM.Token;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Пользователь")
public class User {

    private final static String URL = "https://dev-api.allfdm.ru/";


    @Test
    @Order(1)
    @DisplayName("Информация о пользователе")
    public void userInfo() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("user/profile/")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

    }

    @Test
    @Order(2)
    @DisplayName("Изменение профиля пользователя")
    public void editUserInfo() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("user/profile/")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

    }

    @Test
    @Order(3)
    @DisplayName("Изменение города пользователя")
    public void editUserCity() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("user/profile/")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

    }

}
