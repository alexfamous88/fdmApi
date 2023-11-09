package FDM;

import io.netty.channel.unix.Errors;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Каталог")
public class Catalog {

    private final static String URL = "https://dev-api.allfdm.ru/";

    private static String getCategoryTypesOf(int id) {
        String request = "catalog/category/" + id + "/category-types";
        return request;
    }

    @Test
    @Order(1)
    @DisplayName("Список типов фасада (2-й уровень категорий)")
    public void facadesTypesList() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(getCategoryTypesOf(FactoriesID.kedr))
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        List<String> groupNames = jsonPath.get("group_name");
        List<String> items = jsonPath.get("items");
        List<Integer> idsArray = jsonPath.get("items.id");
        List<String> namesArray = jsonPath.get("items.name");
        List<String> isPlasticsArray = jsonPath.get("items.isPlastic");

        ArrayList<AssertionError> errors = new ArrayList<>();

        // Проверка на пустые типы фасадов
        try {
            groupNames.forEach(x -> Assert.assertTrue("Типы фасадов отсутствуют", x != null));
        } catch (AssertionError a) {
            errors.add(a);
        }

        // Проверка на пустой тип фасадов
        try {
            Assert.assertTrue("Типы фасадов не заполнены", !items.isEmpty());
        } catch (AssertionError a) {
            errors.add(a);
        }

        // Проверка на пустые id
        try {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < idsArray.size(); i++) {
                ids.addAll(jsonPath.get("items[" + i + "].id"));
            }
            ids.forEach(x -> Assert.assertTrue("Присутствуют пустые id", x != null && x > 0));
        } catch (AssertionError a) {
            System.err.println();
            errors.add(a);
        }

        // Проверка на пустые наименования
        try {
            List<String> names = new ArrayList<>();
            for (int i = 0; i < namesArray.size(); i++) {
                names.addAll(jsonPath.get("items[" + i + "].name"));
            }
            names.forEach(x -> Assert.assertTrue("Присутствуют пустые наименования", x != null && !x.isEmpty()));
        } catch (AssertionError a) {
            errors.add(a);
        }

        // Проверка на корректную идентификацию пластика
        try {
            List<Boolean> isPlastics = new ArrayList<>();
            for (int i = 0; i < isPlasticsArray.size(); i++) {
                isPlastics.addAll(jsonPath.get("items[" + i + "].isPlastic"));
            }
            isPlastics.forEach(x -> Assert.assertTrue("Идентификация пластика имеет нулевые значения", x != null));
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


    @Test
    @Order(2)
    @DisplayName("Список фрезеровок")
    public void decorsList() {

        Integer category_type = 22; // ID категории типа фасадов
        Integer decor = 251; // ID декора
        Integer plate = 1; // ID плиты

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        String token = Token.getJWT();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("catalog/millings?category_type=" + category_type + "&decor=" + decor + "&plate=" + plate + "&search&limit&page")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        List<Integer> ids = jsonPath.get("items.id");
        List<String> images = jsonPath.get("items.image");
        List<String> names = jsonPath.get("items.name");
        List<Boolean> shagreens = jsonPath.get("items.labels.shagreenPossible");
        List<Boolean> complexes = jsonPath.get("items.labels.complex");
        List<Boolean> news = jsonPath.get("items.labels.new");
        List<Boolean> promos = jsonPath.get("items.labels.promo");
        List<Boolean> integrates = jsonPath.get("items.labels.integrated");
        List<Boolean> threeDs = jsonPath.get("items.labels.threeD");
        List<Integer> cutterEdgeIdsArray = jsonPath.get("items.options.cutterEdge.id");
        List<Integer> cutterEdgeImagesArray = jsonPath.get("items.options.cutterEdge.image");
        List<Integer> cutterEdgeNamesArray = jsonPath.get("items.options.cutterEdge.name");

        ArrayList<AssertionError> errors = new ArrayList<>();

        try {
            ids.forEach(x -> Assert.assertTrue("Некорректные id декоров", x > 0));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            images.forEach(x -> Assert.assertTrue("Изображение декора отсутствует", x.endsWith(".png") || x.endsWith(".jpg")));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            names.forEach(x -> Assert.assertTrue("Некорректное имя декора", x.length() > 0));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            shagreens.forEach(x -> Assert.assertTrue("Присутствуют пустые значения шагрени", x != null));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            complexes.forEach(x -> Assert.assertTrue("Присутствуют пустые значения комплекса", x != null));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            news.forEach(x -> Assert.assertTrue("Присутствуют пустые значения новых", x != null));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            promos.forEach(x -> Assert.assertTrue("Присутствуют пустые значения промо", x != null));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            integrates.forEach(x -> Assert.assertTrue("Присутствуют пустые значения интегрированный", x != null));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            threeDs.forEach(x -> Assert.assertTrue("Присутствуют пустые значения 3D", x != null));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            List<Integer> cutterEdgeIds = new ArrayList<>();
            for (int i = 0; i < cutterEdgeIdsArray.size(); i++) {
                cutterEdgeIds.addAll(jsonPath.get("items.options.cutterEdge[" + i + "].id"));
            }
            cutterEdgeIds.forEach(x -> Assert.assertTrue("Некорректные значения id cutterEdge", x > 0));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            List<String> cutterEdgeImages = new ArrayList<>();
            for (int i = 0; i < cutterEdgeImagesArray.size(); i++) {
                cutterEdgeImages.addAll(jsonPath.get("items.options.cutterEdge[" + i + "].image"));
            }
            cutterEdgeImages.forEach(x -> Assert.assertTrue("Некорректный формат изображения cutterEdge", x.endsWith(".png") || x.endsWith(".jpg")));
        } catch (AssertionError a) {
            errors.add(a);
        }

        try {
            List<String> cutterEdgeNames = new ArrayList<>();
            for (int i = 0; i < cutterEdgeNamesArray.size(); i++) {
                cutterEdgeNames.addAll(jsonPath.get("items.options.cutterEdge[" + i + "].name"));
            }
            cutterEdgeNames.forEach(x -> Assert.assertTrue("Наименование cutterEdge отсутствует", x.length() > 0));
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