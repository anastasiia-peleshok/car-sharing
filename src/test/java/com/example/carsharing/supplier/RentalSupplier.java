package com.example.carsharing.supplier;

import com.example.carsharing.dto.rental.RentalCreationRequestDto;
import com.example.carsharing.dto.rental.RentalDto;
import com.example.carsharing.dto.rental.RentalWithDetailedCarInfoDto;
import com.example.carsharing.model.Rental;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public class RentalSupplier {
    public static RentalCreationRequestDto getCreateRentalRequestDto() {
        return new RentalCreationRequestDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb466d4"),
                LocalDate.of(2025, 3, 15)
        );
    }

    public static RentalWithDetailedCarInfoDto getRentalWithDetailedCarInfoDto() {
        return new RentalWithDetailedCarInfoDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb466d4"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb466d4"),
                CarSupplier.getCarWithId2(),
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                null
        );
    }

    public static RentalWithDetailedCarInfoDto getRentalWithDetailedCarInfoDtoWithId4() {
        return new RentalWithDetailedCarInfoDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb466d4"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb466d4"),
                CarSupplier.getCarDto(),
                LocalDate.now(),
                LocalDate.of(2025, 3, 15),
                LocalDate.now().plusDays(2)
        );
    }

    public static RentalCreationRequestDto getInvalidCreateRentalRequestDto() {
        return new RentalCreationRequestDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb63d4"),
                LocalDate.of(2024, 2, 1)
        );
    }

    public static List<RentalDto> getAllRentalsByUserWithId1() {
        return List.of(
                getRentalDtoWithId2(),

                new RentalDto(
                        UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758b4753d4"),
                        UUID.fromString("abc2b2a9-53dc-4bc8-824f-6575293763d4"),
                        UUID.fromString("abc2b2a9-53dc-4bc8-824f-6575102933d4"),
                        LocalDate.of(2024, 3, 7),
                        LocalDate.of(2025, 3, 13),
                        LocalDate.of(2024, 3, 10)
                )
        );
    }

    public static RentalWithDetailedCarInfoDto getCarWithId2() {
        return new RentalWithDetailedCarInfoDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758b7763d4"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb41234"),
                CarSupplier.getCarWithId2(),
                LocalDate.of(2024, 3, 9),
                LocalDate.of(2024, 3, 15),
                null
        );
    }

    public static List<RentalDto> getAllActiveRentalsByUserWithId1() {
        return List.of(getRentalDtoWithId2());
    }

    public static List<RentalDto> getAllActiveRentals() {
        return List.of(
                getRentalDtoWithId2(),

                new RentalDto(
                        UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb464d4"),
                        UUID.fromString("abc2b2a9-53dc-4bc8-82gf-65758bb463d4"),
                        UUID.fromString("abc2b2a9-53dc-4bc8-824f-657587b463d4"),
                        LocalDate.of(2024, 3, 8),
                        LocalDate.of(2025, 3, 14),
                        null
                )
        );
    }

    private static RentalDto getRentalDtoWithId2() {
        return new RentalDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb463d4"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-657584b463d4"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb46324"),
                LocalDate.of(2024, 3, 9),
                LocalDate.of(2025, 3, 15),
                null
        );
    }

    public static Rental getRental() {
        Rental rental = new Rental();
        rental.setCar(CarSupplier.getCar());
        rental.setUser(UserSupplier.getUser());
        rental.setRentalStart(LocalDate.of(2024, 3, 10));
        rental.setRentalEnd(LocalDate.of(2025, 3, 15));
        return rental;
    }

    public static Rental getNotActiveRental() {
        Rental rental = new Rental();
        rental.setId(UUID.fromString("abc2b2a9-53dc-4bc8-824f-64448bb463d4"));
        rental.setCar(CarSupplier.getCar());
        rental.setUser(UserSupplier.getUser());
        rental.setRentalStart(LocalDate.now());
        rental.setRentalEnd(LocalDate.of(2025, 3, 15));
        rental.setActualRentalEnd(LocalDate.now().plusDays(2));
        return rental;
    }

    public static RentalDto getRentalDto() {
        return new RentalDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758b9993d4"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-657678b463d4"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-6575892833d4"),
                LocalDate.of(2024, 3, 10),
                LocalDate.of(2025, 3, 15),
                null
        );
    }

    public static RentalWithDetailedCarInfoDto getOverdueRentalWithDetailedCarInfoDto() {
        return new RentalWithDetailedCarInfoDto(
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb463d2"),
                UUID.fromString("abc2b2a9-53dc-4bc8-824f-65758bb46y34"),
                CarSupplier.getCarWithId2(),
                LocalDate.of(2024, 3, 9),
                LocalDate.of(2024, 3, 11),
                null
        );
    }
}
