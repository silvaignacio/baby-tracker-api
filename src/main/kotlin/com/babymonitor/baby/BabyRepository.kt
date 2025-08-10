package com.babymonitor.baby

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface BabyRepository : R2dbcRepository<Baby, Long>
