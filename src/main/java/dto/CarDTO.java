package dto;
//import enums.Fuel;
import lombok.*;

import java.util.Objects;
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private String serialNumber;
    private String manufacture;
    private String model;
    private String year;
    private String fuel;
    private int seats;
    private String carClass;
    private double pricePerDay;
    private String about;
    private String city;
    private double lat;
    private double lng;
    private String image;
    private String owner;
    BookedDto[] bookedPeriods;
}
