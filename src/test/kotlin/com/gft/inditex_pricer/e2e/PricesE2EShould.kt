package com.gft.inditex_pricer.e2e

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.ZonedDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PricesE2EShould {

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    @Test
    fun shouldReturnPriceList1WhenDateIs2020_06_14_10_00() {
        val response = given()
            .param("productId", "35455")
            .param("brandId", "1")
            .param("applicationDate", "2020-06-14T10:00:00Z")
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(200)
            .extract().response()

        assertThat(response.jsonPath().getString("priceList")).isEqualTo("1")
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("startDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-06-14T00:00:00Z").toInstant())
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("endDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-12-31T23:59:59Z").toInstant())
    }

    @Test
    fun shouldReturnPriceList2WhenDateIs2020_06_14_16_00() {
        val response = given()
            .param("productId", "35455")
            .param("brandId", "1")
            .param("applicationDate", "2020-06-14T16:00:00Z")
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(200)
            .extract().response()

        assertThat(response.jsonPath().getString("priceList")).isEqualTo("2")
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("startDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-06-14T15:00:00Z").toInstant())
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("endDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-06-14T18:30:00Z").toInstant())
    }

    @Test
    fun shouldReturnPriceList1WhenDateIs2020_06_14_21_00() {
        val response = given()
            .param("productId", "35455")
            .param("brandId", "1")
            .param("applicationDate", "2020-06-14T21:00:00Z")
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(200)
            .extract().response()

        assertThat(response.jsonPath().getString("priceList")).isEqualTo("1")
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("startDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-06-14T00:00:00Z").toInstant())
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("endDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-12-31T23:59:59Z").toInstant())
    }

    @Test
    fun shouldReturnPriceList3WhenDateIs2020_06_15_10_00() {
        val response = given()
            .param("productId", "35455")
            .param("brandId", "1")
            .param("applicationDate", "2020-06-15T10:00:00Z")
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(200)
            .extract().response()

        assertThat(response.jsonPath().getString("priceList")).isEqualTo("3")
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("startDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-06-15T00:00:00Z").toInstant())
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("endDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-06-15T11:00:00Z").toInstant())
    }

    @Test
    fun shouldReturnPriceList4WhenDateIs2020_06_16_21_00() {
        val response = given()
            .param("productId", "35455")
            .param("brandId", "1")
            .param("applicationDate", "2020-06-16T21:00:00Z")
            .`when`()
            .get("/api/v1/prices")
            .then()
            .statusCode(200)
            .extract().response()

        assertThat(response.jsonPath().getString("priceList")).isEqualTo("4")
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("startDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-06-15T16:00:00Z").toInstant())
        assertThat(ZonedDateTime.parse(response.jsonPath().getString("endDate")).toInstant())
            .isEqualTo(ZonedDateTime.parse("2020-12-31T23:59:59Z").toInstant())
    }
}
