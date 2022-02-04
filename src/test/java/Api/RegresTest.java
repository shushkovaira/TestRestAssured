package Api;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RegresTest {
    private final static String Url = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdUser() {
        Specification.installSpecification(Specification.requestSpec(Url), Specification.responceSpecOk200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        //проверка что email заканчивается на regres.in
        Assert.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void successfulRegTest() {
        Specification.installSpecification(Specification.requestSpec(Url), Specification.responceSpecOk200());
        int id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        SuccessfulRegister successfulReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessfulRegister.class);

        // проверка что респонс не NULL

        Assert.assertNotNull(successfulReg.getId());
        Assert.assertNotNull(successfulReg.getToken());

        // проверка что респонс соответствует переменным ид/токен

        Assert.assertEquals(id, successfulReg.getId());
        Assert.assertEquals(token, successfulReg.getToken());

    }
    @Test
    public void unsuccessfulRegTest(){
        Specification.installSpecification(Specification.requestSpec(Url), Specification.responceError400());
        Register user = new Register("sydney@fife", "");
        UnSuccessfulRegister unSuccessfulReg=given()
                .body(user)
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessfulRegister.class);

        Assert.assertEquals("Missing password", unSuccessfulReg.getError());
    }
}
