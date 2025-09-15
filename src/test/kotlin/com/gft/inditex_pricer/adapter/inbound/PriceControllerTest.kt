package com.gft.inditex_pricer.adapter.inbound


import com.gft.inditex_pricer.adapter.inbound.dto.ApiErrorDTO
import com.gft.inditex_pricer.adapter.inbound.dto.PriceResponseDTO
import com.gft.inditex_pricer.application.GetApplicablePriceUseCase
import com.gft.inditex_pricer.domain.model.Price
import com.gft.inditex_pricer.domain.model.stubs.PriceStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

class PriceControllerShould {

    private val useCase: GetApplicablePriceUseCase = mock()
    private val controller = PriceController(useCase)

    @Test
    fun shouldReturn200AndDtoWhenPriceIsFound() {
        // Given
        val domainPrice: Price = PriceStub.invoke(
            priceList = "2",
            startDate = "2020-06-14T15:00:00Z",
            endDate = "2020-06-14T18:30:00Z",
            priority = 1,
            amount = 25.45
        )
        whenever(useCase.execute(eq("35455"), eq("1"), any())).thenReturn(domainPrice)

        // When
        val response = controller.getApplicablePrice(
            productId = "35455",
            brandId = "1",
            applicationDate = "2020-06-14T16:00:00Z"
        )

        // Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body as PriceResponseDTO
        assertThat(body.productId).isEqualTo("35455")
        assertThat(body.brandId).isEqualTo("1")
        assertThat(body.priceList).isEqualTo("2")
        assertThat(body.priority).isEqualTo(1)
        assertThat(ZonedDateTime.parse(body.startDate))
            .isEqualTo(ZonedDateTime.parse("2020-06-14T15:00:00Z"))
        assertThat(ZonedDateTime.parse(body.endDate))
            .isEqualTo(ZonedDateTime.parse("2020-06-14T18:30:00Z"))
        assertThat(body.price.amount).isEqualTo(25.45)
        assertThat(body.price.currency).isEqualTo("EUR")
    }

    @Test
    fun shouldReturn400WhenParametersAreMissing() {
        val response = controller.getApplicablePrice(
            productId = null,
            brandId = "1",
            applicationDate = "2020-06-14T16:00:00Z"
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val error = response.body as ApiErrorDTO
        assertThat(error.code).isEqualTo("MISSING_PARAMETER")
    }

    @Test
    fun shouldReturn400WhenApplicationDateIsInvalid() {
        val response = controller.getApplicablePrice(
            productId = "35455",
            brandId = "1",
            applicationDate = "invalid-date"
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val error = response.body as ApiErrorDTO
        assertThat(error.code).isEqualTo("INVALID_DATE")
    }

    @Test
    fun shouldReturn404WhenNoPriceIsFound() {
        whenever(useCase.execute(eq("35455"), eq("1"), any())).thenReturn(null)

        val response = controller.getApplicablePrice(
            productId = "35455",
            brandId = "1",
            applicationDate = "2020-06-14T16:00:00Z"
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val error = response.body as ApiErrorDTO
        assertThat(error.code).isEqualTo("PRICE_NOT_FOUND")
    }
}
