package com.example.MongoPractice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document
data class Patient(
    val name: String,
    val description: String,
    var doctor: Doctor,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    @Id
    val id: UUID = UUID.randomUUID()
)