package com.gft.inditex_pricer.adapter.outbound.persistence

import com.gft.inditex_pricer.adapter.outbound.persistence.entity.JpaPriceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataPriceRepository : JpaRepository<JpaPriceEntity, Long>