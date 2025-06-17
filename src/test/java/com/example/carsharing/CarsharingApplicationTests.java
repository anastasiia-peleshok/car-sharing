package com.example.carsharing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.url=jdbc:mysql://localhost:3306/car_sharing_db",
        "spring.datasource.username=root",
        "spring.datasource.password=password",
        "jwt.secret=d85ed6ed9059d8c6ace909e4daae72e899b2096bd5b748ce56f741bdf1879a2aa632b6645ea2d21f26e4824df5f36db1ef496bcf7f1b114f5244f17847b426e9e32006d4b57aed31594f99d0e6229e10080249cb28e0102df4f967a64764047b7b4da235faf033cb4751c2ec6cd39"
})
class CarsharingApplicationTests {

    @Test
    void contextLoads() {
    }

}
