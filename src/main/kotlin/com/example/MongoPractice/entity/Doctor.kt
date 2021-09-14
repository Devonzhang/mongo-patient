package com.example.MongoPractice.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Doctor(
    val name: String,
    val description: String,
    var patientNumber: Int = 0,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    @Id
    val id: ObjectId = ObjectId.get()
)