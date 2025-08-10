package com.babymonitor.baby

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("babies")
data class Baby(
    @Id val id: Long? = null,
    val nombre: String,
    @Column("fecha_nacimiento")
    val fechaNacimiento: LocalDate,
    val genero: String? = null
)
