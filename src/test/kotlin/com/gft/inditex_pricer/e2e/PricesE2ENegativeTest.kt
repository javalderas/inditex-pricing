package com.gft.inditex_pricer.e2e

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PricesE2ENegativeTest {

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    @Test
    fun shouldReturn404WhenPriceNotFound() {
        given()
            .param("productId", "99999")
            .param("brandId", "1")
            .param("applicationDate", "2020-06-14T10:00:00Z")
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(404)
            .body("code", equalTo("PRICE_NOT_FOUND"))
            .body("message", equalTo("Price not found"))
            .body("status", equalTo(404))
            .body("timestamp", notNullValue())
            .body("path", equalTo("/api/v1/prices"))
    }

    @Test
    fun shouldReturn400WhenMissingParameters() {
        given()
            .param("productId", "35455") // brandId missing
            .param("applicationDate", "2020-06-14T10:00:00Z")
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(400)
            .body("code", equalTo("MISSING_PARAMETER"))
            .body("status", equalTo(400))
            .body("timestamp", notNullValue())
            .body("path", equalTo("/api/v1/prices"))
    }

    @Test
    fun shouldReturn400WhenInvalidDateFormat() {
        given()
            .param("productId", "35455")
            .param("brandId", "1")
            .param("applicationDate", "14-06-2020 10:00") // invalid format
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(400)
            .body("code", equalTo("INVALID_DATE"))
            .body("status", equalTo(400))
            .body("timestamp", notNullValue())
            .body("path", equalTo("/api/v1/prices"))
    }
}
