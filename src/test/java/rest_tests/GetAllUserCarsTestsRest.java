package rest_tests;

import rest_api.CarController;
import dto.CarsDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetAllUserCarsTestsRest extends CarController {
    SoftAssert softAssert = new SoftAssert();
    //CarsDto cars;

    @Test
    public void getAllUserCarsPositiveTest(){
        Response response = getAllUserCars();
        CarsDto cars = response.getBody().as(CarsDto.class);
        System.out.println(cars);
        softAssert.assertEquals(response.getStatusCode(),
                200, "validate status code");
        softAssert.assertTrue(cars.getCars()[0].getCity().contains("Haifa"),
                "validate city");
        softAssert.assertAll();
    }


}
