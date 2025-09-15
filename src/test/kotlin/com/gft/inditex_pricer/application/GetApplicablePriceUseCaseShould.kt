package com.gft.inditex_pricer.application

import com.gft.inditex_pricer.domain.model.Money
import com.gft.inditex_pricer.domain.model.Price
import com.gft.inditex_pricer.domain.port.PriceQueryPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.ZonedDateTime
import java.util.*

class GetApplicablePriceUseCaseShould {

    private val eur = Currency.getInstance("EUR")

    private fun price(
        priceList: String,
        start: String,
        end: String,
        priority: Int,
        amount: Double
    ): Price {
        return Price(
            productId = "35455",
            brandId = "1",
            priceList = priceList,
            startDate = ZonedDateTime.parse(start),
            endDate = ZonedDateTime.parse(end),
            priority = priority,
            price = Money(amount, eur)
        )
    }

    @Test
    fun shouldReturnHighestPriorityPriceWhenMultiplePricesAvailable() {
        val at = ZonedDateTime.parse("2020-06-15T10:00:00Z")

        val lowPriority = price("1", "2020-06-14T00:00:00Z", "2020-12-31T23:59:59Z", 0, 35.50)
        val highPriority = price("3", "2020-06-15T00:00:00Z", "2020-06-15T11:00:00Z", 1, 30.50)

        val port: PriceQueryPort = mock()
        whenever(port.findApplicable("35455", "1", at))
            .thenReturn(listOf(lowPriority, highPriority))

        val useCase = GetApplicablePriceUseCase(port)

        val result = useCase.execute("35455", "1", at)

        assertThat(result).isNotNull
        assertThat(result!!.priceList).isEqualTo("3")
    }

    @Test
    fun shouldReturnNullWhenNoPriceIsAvailable() {
        val at = ZonedDateTime.parse("2020-06-13T10:00:00Z")

        val port: PriceQueryPort = mock()
        whenever(port.findApplicable("35455", "1", at)).thenReturn(emptyList())

        val useCase = GetApplicablePriceUseCase(port)

        val result = useCase.execute("35455", "1", at)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnSinglePriceWhenOnlyOneMatches() {
        val at = ZonedDateTime.parse("2020-06-14T10:00:00Z")

        val candidate = price("1", "2020-06-14T00:00:00Z", "2020-12-31T23:59:59Z", 0, 35.50)

        val port: PriceQueryPort = mock()
        whenever(port.findApplicable("35455", "1", at)).thenReturn(listOf(candidate))

        val useCase = GetApplicablePriceUseCase(port)

        val result = useCase.execute("35455", "1", at)

        assertThat(result).isNotNull
        assertThat(result!!.priceList).isEqualTo("1")
    }
}
