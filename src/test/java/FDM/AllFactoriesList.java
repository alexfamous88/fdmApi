package FDM;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.*;


import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Список всех фабрик")
public class AllFactoriesList {

    private final static String URL = "https://dev-api.allfdm.ru/";
    private final List<String> FACTORIES_LIST = List.of("ORWOOD", "ЛОТОС-ЮГ", "ДЕСКОР", "Мебель Холдинг",
            "ХАМЕЛЕОН", "ФАСАДЕЛЬ", "FABRICHE", "Древиз", "ADELKREIS", "DEMFA", "СТАНДАРТ", "Фасады Черноземья",
            "Томские мебельные фасады", "Кедр", "ЛаминатРус", "Палаццо");
    private final Set<String> FACTORIES = new TreeSet<>(FACTORIES_LIST);

    @Test
    @Order(1)
    @DisplayName("Проверка списка фабрик без информации о фабрике")
    public void factoriesListNotExtended() {

        String token = Token.getJWT();

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("catalog/factories")
                .then()
                .body("id", notNullValue())
                .body("name", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        List<String> factories = jsonPath.get("name");
        Collections.sort(factories);
        Assert.assertTrue("Количество фабрик не одинаково", factories.size() == FACTORIES.size());
        Assert.assertTrue("Фабрики не соответствуют", FACTORIES.stream().toList().equals(factories));

        List<String> infos = jsonPath.get("info");
        infos.forEach(x -> Assert.assertTrue("Отображается поле info", x == null));
    }

    @Test
    @Order(2)
    @DisplayName("Проверка списка фабрик с информацией о фабрике")
    public void factoriesListExtended() {

        String token = Token.getJWT();

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("extented", "1")
                .when()
                .get("catalog/factories?extented=1")
                .then()
                .body("id", notNullValue())
                .body("name", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        List<String> factories = jsonPath.get("name");
        Collections.sort(factories);
        Assert.assertTrue("Количество фабрик не одинаково", factories.size() == FACTORIES.size());
        Assert.assertTrue("Фабрики не соответствуют", FACTORIES.stream().toList().equals(factories));

        List<String> infos = jsonPath.get("info");
        Assert.assertTrue("Не отображается поле info", infos.size() == factories.size());
    }
}