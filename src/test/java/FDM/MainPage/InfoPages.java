package FDM.MainPage;

import FDM.Specifications;
import FDM.Token;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Инфо-страницы")
public class InfoPages {


    private final static String URL = "https://dev-api.allfdm.ru/";


    @Test
    @Order(1)
    @DisplayName("Возврат")
    public void refund() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("info/refund")
                .then()
                .body("title", notNullValue())
                .body("text", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        String title = jsonPath.get("title");

        // Проверка на корректность заголовка

        Assert.assertEquals("Некорректный заголовок", "Возврат", title);
    }


    @Test
    @Order(2)
    @DisplayName("Оплата")
    public void payment() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("info/payment")
                .then()
                .body("title", notNullValue())
                .body("text", notNullValue())
                .body("methods", notNullValue())
                .body("legal_entity_info", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        String title = jsonPath.get("title");
        List<String> icons = jsonPath.get("methods.icon");
        List<String> names = jsonPath.get("methods.name");

        List<AssertionError> errors = new ArrayList<>();

        // Проверка на корректность заголовка
        try {
            Assert.assertEquals("Некорректный заголовок", "Оплата", title);
        } catch (AssertionError a) {
            errors.add(a);
        }

        // Проверка наличия изображений в способах оплаты
        try {
            icons.forEach(x -> Assert.assertTrue("Отсутствуют изображения в способах оплаты", x.endsWith(".svg")));
        } catch (AssertionError a) {
            errors.add(a);
        }

        // Проверка наименований способов оплаты
        try {
            names.forEach(x -> Assert.assertTrue("Отсутствует описание способов оплаты", x.length() > 0));
        } catch (AssertionError a) {
            errors.add(a);
        }

        if (errors.size() > 0) {
            for (AssertionError error : errors) {
                error.printStackTrace();
            }
            throw new AssertionError();
        }
    }
}



