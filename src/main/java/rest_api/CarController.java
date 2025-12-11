package rest_api;

import dto.CarDTO;
import dto.RegistrationBodyDto;
import dto.TokenDto;
import interfaces.Base_Api;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;

import java.net.URL;

import static io.restassured.RestAssured.given;

public class CarController implements Base_Api {
    public TokenDto tokenDto;

    @BeforeSuite
    public void login() {
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("sima_simonova370@gmail.com")
                .password("BSas124!")
                .build();
        tokenDto = given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + LOGIN_URL)
                .thenReturn()
                .getBody()
                .as(TokenDto.class);
        System.out.println(tokenDto.getAccessToken());
    }

    public Response addNewCar(CarDTO car) {
        return given()
                .body(car)
                .contentType(ContentType.JSON)
                .header("Authorization", tokenDto.getAccessToken())
                .when()
                .post(BASE_URL + ADD_NEW_CAR_URL)
                .thenReturn();
    }

    public Response addNewCar_WrongToken(CarDTO car, String token) {
        return given()
                .body(car)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post(BASE_URL + ADD_NEW_CAR_URL)
                .thenReturn();
    }

    public Response addNewCar_WOToken(CarDTO car) {
        return given()
                .body(car)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + ADD_NEW_CAR_URL)
                .thenReturn();
    }

    public Response getAllUserCars() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", tokenDto.getAccessToken())
                .when()
                .get(BASE_URL + GET_ALL_USER_CARS_URL)
                .thenReturn();
    }

    public Response getAllUserCars_WrongUrl(String url) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", tokenDto.getAccessToken())
                .when()
                .get(BASE_URL + url)
                .thenReturn();
    }

    public Response deleteCarBySerialNumber(String serialNumber){
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", tokenDto.getAccessToken())
                .delete(BASE_URL+DELETE_CAR_URL+serialNumber)
                .thenReturn();
    }
}
