package rest_tests;

import dto.CarDTO;
import dto.ErrorMessageDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import rest_api.CarController;

import java.util.Random;

public class AddNewCarTestsRest extends CarController {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void addNewCarPositiveTest(){
        int i = new Random().nextInt(1000)+1000;
        CarDTO car = CarDTO.builder()
                .serialNumber("876-"+i)
                .manufacture("Honda")
                .model("CRV")
                .year("2024")
                .fuel("Electric")
                .seats(4)
                .carClass("A")
                .pricePerDay(23.5)
                .city("Haifa")
                .build();
        System.out.println(car);
        Response response = addNewCar(car);
        System.out.println(response.getBody().print());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void addNewCarNegativeTest_DuplicateSerialNumber(){
        CarDTO car = CarDTO.builder()
                .serialNumber("876-1175")
                .manufacture("Honda")
                .model("CRV")
                .year("2024")
                .fuel("Electric")
                .seats(4)
                .carClass("A")
                .pricePerDay(23.5)
                .city("Haifa")
                .build();
        System.out.println(car);
        Response response = addNewCar(car);
        System.out.println(response.getBody().print());
        softAssert.assertEquals(response.getStatusCode(),
                400, "validate status code");
        ErrorMessageDto errorMessageDto = response.getBody()
                .as(ErrorMessageDto.class);
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("already exists"),
                "validate error message");
        softAssert.assertAll();
    }

    @Test
    public void addNewCarNegativeTest_EmptyField(){
        CarDTO car = CarDTO.builder()
                //.serialNumber("")
                .manufacture("Honda")
                .model("CRV")
                .year("2024")
                .fuel("Electric")
                .seats(4)
                .carClass("A")
                .pricePerDay(23.5)
                .city("Haifa")
                .build();
        System.out.println(car);
        Response response = addNewCar(car);
        System.out.println(response.getBody().print());
        softAssert.assertEquals(response.getStatusCode(),
                400, "validate status code");
        ErrorMessageDto errorMessageDto = response.getBody()
                .as(ErrorMessageDto.class);
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("must not be blank"),
                "validate error message");
        softAssert.assertAll();
    }

    @Test
    public void addNewCarNegativeTest_WrongField(){
        int i = new Random().nextInt(1000)+1000;
        CarDTO car = CarDTO.builder()
                .serialNumber("876-"+i)
                .manufacture("Honda") //max length
                .model("CRV")         //max length
                .year("2024")         //2026; -1; 20.25; yyyy; 20 25; BUG!!!!!!!!
                .fuel("Electric")     //citric;
                .seats(4)             // 1, 21, -1,
                .carClass("A")        //max length
                .pricePerDay(23.5)    // -0.0001; 1000.00001;
                .city("Haif")
                .build();
        System.out.println(car);
        Response response = addNewCar(car);
        System.out.println(response.getBody().print());
        softAssert.assertEquals(response.getStatusCode(),
                400, "validate status code");
        ErrorMessageDto errorMessageDto = response.getBody()
                .as(ErrorMessageDto.class);
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("is not supported"),
                "validate error message");
        softAssert.assertAll();
    }



@Test
    public void addNewCarNegativeTest_WrongAuthorization(){
        int i = new Random().nextInt(1000)+1000;
        CarDTO car = CarDTO.builder()
                .serialNumber("876-"+i)
                .manufacture("Honda")
                .model("CRV")
                .year("2024")
                .fuel("Electric")
                .seats(4)
                .carClass("A")
                .pricePerDay(23.5)
                .city("Haifa")
                .build();
        Response response = addNewCar_WrongToken(car, "5fshkjgfd");
        System.out.println(response.getBody().print());
        softAssert.assertEquals(response.getStatusCode(), 401, "validate status code");
        softAssert.assertTrue(response.getBody().print().contains(
                "strings must contain exactly 2 period characters."),
                "validate error message");
        softAssert.assertAll();
    }
    @Test
    public void addNewCarNegativeTest_WOAuthorization(){
        int i = new Random().nextInt(1000)+1000;
        CarDTO car = CarDTO.builder()
                .serialNumber("876-"+i)
                .manufacture("Honda")
                .model("CRV")
                .year("2024")
                .fuel("Electric")
                .seats(4)
                .carClass("A")
                .pricePerDay(23.5)
                .city("Haifa")
                .build();
        Response response = addNewCar_WOToken(car);
        System.out.println(response.getBody().print());
        softAssert.assertEquals(response.getStatusCode(), 403,
                "validate status code");
        softAssert.assertAll();
    }

}
