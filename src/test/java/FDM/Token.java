package FDM;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Token {

    private final static String URL = "https://dev-api.allfdm.ru/";

    public static String getJWT() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());

        Map<String, String> user = new HashMap<>();
        user.put("phone", "79999999999");
        user.put("code", "1111");

        Response response = given()
                .body(user)
                .when()
                .post("user/login/check-code")
                .then()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getString("token");
    }
}