package com.example.carsharing.supplier;

import com.example.carsharing.dto.car.CarDto;
import com.example.carsharing.dto.car.CarRegistrationRequestDto;
import com.example.carsharing.model.Car;
import com.example.carsharing.model.CarType;

import java.math.BigDecimal;
import java.util.UUID;

public class CarSupplier {
    public static CarRegistrationRequestDto getCreateCarRequestDto() {
        return new CarRegistrationRequestDto(
                "BYD",
                "Song L",
                "green",
                CarType.AUTO,
                5,
                BigDecimal.valueOf(700)
        );
    }

    public static CarDto getCarDto() {
        return new CarDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb463d4"),
                "BYD",
                "Song L",
                "green",
                CarType.AUTO,
                5,
                BigDecimal.valueOf(700),
                true
        );
    }

    public static CarRegistrationRequestDto getInvalidCreateCarRequestDto() {
        return new CarRegistrationRequestDto(
                "",
                "",
                "",
                null,
                -1,
                BigDecimal.valueOf(-700)
        );
    }

    public static CarDto getCarWithId2() {
        return new CarDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb463d4"),
                "Zeekr",
                "001",
                "green",
                CarType.AUTO,
                5,
                BigDecimal.valueOf(750),
                true
        );
    }

    public static CarRegistrationRequestDto getUpdatedCreateCarRequestDto() {
        return new CarRegistrationRequestDto(
                "Zeekr",
                "001",
                "green",
                CarType.AUTO,
                7,
                BigDecimal.valueOf(1200)
        );
    }

    public static CarDto getUpdatedCarDto() {
        return new CarDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb466d4"),
                "Zeekr",
                "001",
                "green",
                CarType.AUTO,
                7,
                BigDecimal.valueOf(1200),
                true
        );
    }

    public static Car getCar() {
        Car car = new Car();
        car.setId( UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb466d4"));
        car.setBrand("BYD");
        car.setYear(5);
        car.setPrice(BigDecimal.valueOf(700));
        car.setCarType(CarType.AUTO);
        return car;
    }


}
