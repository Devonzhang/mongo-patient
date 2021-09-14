package com.example.MongoPractice.entity

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Patient(
    val name: String,
    val description: String,
    val doctor: Doctor,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now(),
    @Id
    @JsonSerialize
    val id: ObjectId = ObjectId.get()
)