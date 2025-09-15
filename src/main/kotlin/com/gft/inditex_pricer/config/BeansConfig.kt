package com.gft.inditex_pricer.config

import com.gft.inditex_pricer.application.GetApplicablePriceUseCase
import com.gft.inditex_pricer.domain.port.PriceQueryPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeansConfig {
    @Bean
    fun getApplicablePriceUseCase(priceQueryPort: PriceQueryPort) =
        GetApplicablePriceUseCase(priceQueryPort)
}