package com.gft.inditex_pricer.e2e

import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PricesE2EShould {

    @LocalServerPort
    private var port: Int = 0

    private val basePath = "/api/v1/prices"

    @Test
    fun shouldReturnPriceList1WhenDateIs2020_06_14_10_00() {
        given()
            .port(port)
            .queryParam("productId", "35455")
            .queryParam("brandId", "1")
            .queryParam("applicationDate", "2020-06-14T10:00:00Z")
            .`when`()
            .get(basePath)
            .then()
            .statusCode(200)
            .body("priceList", equalTo("1"))
            .body("price.amount", equalTo(35.50f))
    }

    @Test
    fun shouldReturnPriceList2WhenDateIs2020_06_14_16_00() {
        given()
            .port(port)
            .queryParam("productId", "35455")
            .queryParam("brandId", "1")
            .queryParam("applicationDate", "2020-06-14T16:00:00Z")
            .`when`()
            .get(basePath)
            .then()
            .statusCode(200)
            .body("priceList", equalTo("2"))
            .body("price.amount", equalTo(25.45f))
    }

    @Test
    fun shouldReturnPriceList1WhenDateIs2020_06_14_21_00() {
        given()
            .port(port)
            .queryParam("productId", "35455")
            .queryParam("brandId", "1")
            .queryParam("applicationDate", "2020-06-14T21:00:00Z")
            .`when`()
            .get(basePath)
            .then()
            .statusCode(200)
            .body("priceList", equalTo("1"))
            .body("price.amount", equalTo(35.50f))
    }

    @Test
    fun shouldReturnPriceList3WhenDateIs2020_06_15_10_00() {
        given()
            .port(port)
            .queryParam("productId", "35455")
            .queryParam("brandId", "1")
            .queryParam("applicationDate", "2020-06-15T10:00:00Z")
            .`when`()
            .get(basePath)
            .then()
            .statusCode(200)
            .body("priceList", equalTo("3"))
            .body("price.amount", equalTo(30.50f))
    }

    @Test
    fun shouldReturnPriceList4WhenDateIs2020_06_16_21_00() {
        given()
            .port(port)
            .queryParam("productId", "35455")
            .queryParam("brandId", "1")
            .queryParam("applicationDate", "2020-06-16T21:00:00Z")
            .`when`()
            .get(basePath)
            .then()
            .statusCode(200)
            .body("priceList", equalTo("4"))
            .body("price.amount", equalTo(38.95f))
    }
}
