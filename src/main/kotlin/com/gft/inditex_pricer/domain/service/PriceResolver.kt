package com.gft.inditex_pricer.domain.service

import com.gft.inditex_pricer.domain.model.Price
import java.time.ZonedDateTime

object PriceResolver {
    /**
     * Business rule:
     * - Select all prices valid for the given datetime
     * - Return the one with the highest priority
     */
    fun resolve(candidates: List<Price>, at: ZonedDateTime): Price? =
        candidates
            .filter { at >= it.startDate && at < it.endDate }
            .maxByOrNull { it.priority }
}