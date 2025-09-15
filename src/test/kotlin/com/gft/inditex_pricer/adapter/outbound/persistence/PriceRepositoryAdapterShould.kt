package com.gft.inditex_pricer.adapter.outbound.persistence


import com.gft.inditex_pricer.adapter.outbound.persistence.stubs.JpaPriceEntityStub
import com.gft.inditex_pricer.domain.model.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.ZonedDateTime

class PriceRepositoryAdapterShould {

    private val repository: SpringDataPriceRepository = mock()
    private val adapter = PriceRepositoryAdapter(repository)

    @Test
    fun shouldMapJpaEntityToDomainPrice() {
        // Given
        val entity = JpaPriceEntityStub.invoke()
        whenever(repository.findAll()).thenReturn(listOf(entity))

        // When
        val result: List<Price> = adapter.findApplicable("35455", "1", ZonedDateTime.parse("2020-06-14T10:00:00Z"))

        // Then
        assertThat(result).hasSize(1)
        assertThat(result.first().productId).isEqualTo("35455")
        assertThat(result.first().price.amount).isEqualTo(35.50)
        assertThat(result.first().price.currency.currencyCode).isEqualTo("EUR")
    }

    @Test
    fun shouldReturnEmptyListWhenNoEntityMatchesProductOrBrand() {
        // Given
        val entity = JpaPriceEntityStub.invoke(productId = "99999")
        whenever(repository.findAll()).thenReturn(listOf(entity))

        // When
        val result: List<Price> = adapter.findApplicable("35455", "1", ZonedDateTime.parse("2020-06-14T10:00:00Z"))

        // Then
        assertThat(result).isEmpty()
    }
}
