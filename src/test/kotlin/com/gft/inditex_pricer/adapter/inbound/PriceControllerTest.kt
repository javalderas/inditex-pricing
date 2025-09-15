package com.gft.inditex_pricer.adapter.inbound

import com.gft.inditex_pricer.adapter.inbound.dto.PriceNotFoundException
import com.gft.inditex_pricer.adapter.inbound.dto.PriceResponseDTO
import com.gft.inditex_pricer.application.GetApplicablePriceUseCase
import com.gft.inditex_pricer.domain.model.Money
import com.gft.inditex_pricer.domain.model.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

class PriceControllerTest {

    private val useCase: GetApplicablePriceUseCase = mock()
    private val controller = PriceController(useCase)

    @Test
    fun shouldReturn200AndResponseDTOWhenPriceIsFound() {
        val domainPrice = Price(
            productId = "35455",
            brandId = "1",
            priceList = "1",
            startDate = ZonedDateTime.of(2020, 6, 14, 0, 0, 0, 0, ZoneOffset.UTC),
            endDate = ZonedDateTime.of(2020, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC),
            priority = 0,
            price = Money(35.50, Currency.getInstance("EUR"))
        )
        whenever(useCase.retrieveBy("35455", "1", ZonedDateTime.parse("2020-06-14T10:00:00Z")))
            .thenReturn(domainPrice)

        val response = controller.getApplicablePrice(
            productId = "35455",
            brandId = "1",
            applicationDate = "2020-06-14T10:00:00Z"
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val dto = response.body as PriceResponseDTO
        assertThat(dto.productId).isEqualTo("35455")
        assertThat(dto.brandId).isEqualTo("1")
        assertThat(dto.priceList).isEqualTo("1")
        assertThat(dto.price.amount).isEqualTo(35.50)
        assertThat(dto.price.currency).isEqualTo("EUR")
    }

    @Test
    fun shouldThrowPriceNotFoundExceptionWhenUseCaseReturnsNull() {
        // Given
        whenever(useCase.retrieveBy("35455", "1", ZonedDateTime.parse("2020-06-14T10:00:00Z")))
            .thenReturn(null)

        // When + Then
        assertThrows<PriceNotFoundException> {
            controller.getApplicablePrice(
                productId = "35455",
                brandId = "1",
                applicationDate = "2020-06-14T10:00:00Z"
            )
        }
    }

    @Test
    fun shouldThrowDateTimeParseExceptionWhenApplicationDateIsInvalid() {
        // When + Then
        assertThrows<java.time.format.DateTimeParseException> {
            controller.getApplicablePrice(
                productId = "35455",
                brandId = "1",
                applicationDate = "14-06-2020 10:00"
            )
        }
    }
}
