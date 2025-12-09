package rest_tests;

import dto.ErrorMessageDto;
import dto.RegistrationBodyDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import rest_api.AuthenticationController;

import java.util.Random;

public class RegistrationTestsRest extends AuthenticationController {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void registrationPositiveTest() {
        int i = new Random().nextInt(1000);
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("mary_polina" + i + "@gmail.com")
                .password("MMaa123!")
                .firstName("Mary")
                .lastName("Polina")
                .build();
        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL)
                .getStatusCode(), 200);
    }

    //poly_polina"+i+"gmail.com; @gmail.com; poly_polina@w;
    //poly_polina@@gmail.com; poly_polina@w.; poly_polina@.r;
    //poly_polina@.r; poly_polina"+11111111111+"gmail.com;
    //poly polina@gmail.com; полина"+i+"polina@gmail.com;
    //poly_polina"+i+"@gmail..com;
    @Test
    public void registrationNegativeTest_WrongEmail() {
        int i = new Random().nextInt(1000);
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("mary_polina" + i + "gmail.com")
                .password("MMaa123!")
                .firstName("Mary")
                .lastName("Polina")
                .build();
        Response response = registrationLogin(user, REGISTRATION_URL);
        System.out.println(response.getBody().print());
        softAssert.assertEquals(response.getStatusCode(), 400,
                "validate statusCode");
        ErrorMessageDto errorMessageDto = response.getBody()
                .as(ErrorMessageDto.class);
        softAssert.assertEquals(errorMessageDto.getError(),
                "Bad Request", "validate ErrorName");
        softAssert.assertTrue(errorMessageDto.getMessage().toString()
                .contains("a well-formed email address"), "validate Message");
        softAssert.assertAll();
    }
    //AAaaaa$; aesrw123#; ASWER123&; AAa 123!; АКВПРН123!; "       "; Aaa123!; >15 char;
    // AAaa123%;
    @Test
    public void registrationNegativeTest_WrongPassword() {
        int i = new Random().nextInt(1000);
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("mary_polina" + i + "@gmail.com")
                .password("MMaa1234")
                .firstName("Mary")
                .lastName("Polina")
                .build();
        Response response = registrationLogin(user, REGISTRATION_URL);
        System.out.println(response.getBody().print());
        softAssert.assertEquals(response.getStatusCode(), 400,
                "validate statusCode");
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        softAssert.assertEquals(errorMessageDto.getError(),
                "Bad Pequest", "validate ErrorNme");
        softAssert.assertTrue(errorMessageDto.getMessage().toString()
                .contains("Must contain at least 1 uppercase letter, 1 lowercase letter,"),
                "validate error message");
        softAssert.assertAll();
    }

    @Test
    public void registrationNegativeTest_EmptyField(){
        int i = new Random().nextInt(1000);
        RegistrationBodyDto user = RegistrationBodyDto.builder()
                .username("mary_polina" + i + "@gmail.com")
                .password("MMaa123!")
                .firstName("")
                .lastName("Polina")
                .build();
        Response response = registrationLogin(user, REGISTRATION_URL);
        softAssert.assertEquals(response.getStatusCode(), 400,
                "validate status code");
        ErrorMessageDto errorMessageDto = response.getBody()
                .as(ErrorMessageDto.class);
        softAssert.assertEquals(errorMessageDto.getError(),
                "Bad Request", "validate error");
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getMessage().toString()
                .contains("must not be blank"), "validate message");
        softAssert.assertAll();
    }
}
