package com.gft.inditex_pricer.application


import com.gft.inditex_pricer.domain.model.Price
import com.gft.inditex_pricer.domain.model.stubs.PriceStub
import com.gft.inditex_pricer.domain.port.PriceQueryPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.ZonedDateTime

class GetApplicablePriceUseCaseTest {

    private val port: PriceQueryPort = mock()
    private val useCase = GetApplicablePriceUseCase(port)

    @Test
    fun shouldReturnHighestPriorityPriceWhenMultiplePricesAvailable() {
        val at = ZonedDateTime.parse("2020-06-15T10:00:00Z")

        val lowPriority: Price = PriceStub.invoke(priceList = "1", priority = 0,
            startDate = "2020-06-14T00:00:00Z", endDate = "2020-12-31T23:59:59Z", amount = 35.50)
        val highPriority: Price = PriceStub.invoke(priceList = "3", priority = 1,
            startDate = "2020-06-15T00:00:00Z", endDate = "2020-06-15T11:00:00Z", amount = 30.50)

        whenever(port.findApplicable("35455", "1", at)).thenReturn(listOf(lowPriority, highPriority))

        val result = useCase.execute("35455", "1", at)

        assertThat(result).isNotNull
        assertThat(result!!.priceList).isEqualTo("3")
    }

    @Test
    fun shouldReturnNullWhenNoPriceIsAvailable() {
        val at = ZonedDateTime.parse("2020-06-13T10:00:00Z")

        whenever(port.findApplicable("35455", "1", at)).thenReturn(emptyList())

        val result = useCase.execute("35455", "1", at)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnSinglePriceWhenOnlyOneMatches() {
        val at = ZonedDateTime.parse("2020-06-14T10:00:00Z")

        val candidate: Price = PriceStub.invoke(priceList = "1", amount = 35.50)

        whenever(port.findApplicable("35455", "1", at)).thenReturn(listOf(candidate))

        val result = useCase.execute("35455", "1", at)

        assertThat(result).isNotNull
        assertThat(result!!.priceList).isEqualTo("1")
    }
}
