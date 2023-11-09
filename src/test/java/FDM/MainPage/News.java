package FDM.MainPage;

import FDM.Specifications;
import FDM.Token;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Новости и акции")
public class News {

    private final static String URL = "https://dev-api.allfdm.ru/";


    @Test
    @Order(1)
    @DisplayName("Список всех новостей")
    public void getAllNews() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("news/")
                .then()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        Integer totalPages = jsonPath.get("totalPages");
        List<String> items = jsonPath.get("items");

        // Проверка наличия новостей, если страницы присутствуют
        if (totalPages != null) {
            Assert.assertTrue("Новости отсутствуют", !items.isEmpty());
        }
    }
}
