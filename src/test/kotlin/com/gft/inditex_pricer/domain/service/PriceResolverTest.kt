package com.gft.inditex_pricer.domain.service

import com.gft.inditex_pricer.domain.model.Price
import com.gft.inditex_pricer.domain.model.stubs.PriceStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class PriceResolverShould {

    @Test
    fun shouldReturnPriceWithHigherPriorityWhenTwoPricesOverlap() {
        val at = ZonedDateTime.parse("2020-06-15T10:00:00Z")

        val lowPriority: Price = PriceStub.invoke(
            priceList = "1",
            priority = 0,
            startDate = "2020-06-14T00:00:00Z",
            endDate = "2020-12-31T23:59:59Z",
            amount = 35.50
        )

        val highPriority: Price = PriceStub.invoke(
            priceList = "3",
            priority = 1,
            startDate = "2020-06-15T00:00:00Z",
            endDate = "2020-06-15T11:00:00Z",
            amount = 30.50
        )

        val result = PriceResolver.resolve(listOf(lowPriority, highPriority), at)

        assertThat(result).isNotNull
        assertThat(result!!.priceList).isEqualTo("3")
    }

    @Test
    fun shouldReturnNullWhenNoPriceIsValidAtGivenDate() {
        val at = ZonedDateTime.parse("2020-06-13T10:00:00Z")

        val candidate: Price = PriceStub.invoke(
            priceList = "1",
            startDate = "2020-06-14T00:00:00Z",
            endDate = "2020-06-14T23:59:59Z",
            amount = 35.50
        )

        val result = PriceResolver.resolve(listOf(candidate), at)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnSinglePriceWhenOnlyOneMatches() {
        val at = ZonedDateTime.parse("2020-06-14T10:00:00Z")

        val candidate: Price = PriceStub.invoke(
            priceList = "1",
            startDate = "2020-06-14T00:00:00Z",
            endDate = "2020-12-31T23:59:59Z",
            amount = 35.50
        )

        val result = PriceResolver.resolve(listOf(candidate), at)

        assertThat(result).isNotNull
        assertThat(result!!.priceList).isEqualTo("1")
    }

    @Test
    fun shouldHandleEmptyListAndReturnNull() {
        val at = ZonedDateTime.parse("2020-06-14T10:00:00Z")

        val result = PriceResolver.resolve(emptyList(), at)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnPriceThatStartsExactlyAtGivenDate() {
        val at = ZonedDateTime.parse("2020-06-14T15:00:00Z")

        val candidate: Price = PriceStub.invoke(
            priceList = "2",
            startDate = "2020-06-14T15:00:00Z",
            endDate = "2020-06-14T18:30:00Z",
            priority = 1,
            amount = 25.45
        )

        val result = PriceResolver.resolve(listOf(candidate), at)

        assertThat(result).isNotNull
        assertThat(result!!.priceList).isEqualTo("2")
    }

    @Test
    fun shouldNotReturnPriceWhenDateEqualsEndDate() {
        val at = ZonedDateTime.parse("2020-06-14T18:30:00Z")

        val candidate: Price = PriceStub.invoke(
            priceList = "2",
            startDate = "2020-06-14T15:00:00Z",
            endDate = "2020-06-14T18:30:00Z",
            priority = 1,
            amount = 25.45
        )

        val result = PriceResolver.resolve(listOf(candidate), at)

        assertThat(result).isNull()
    }
}
