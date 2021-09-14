package com.example.MongoPractice.repository

import com.example.MongoPractice.entity.Patient
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface PatientRepository : MongoRepository<Patient, String> {
    fun findOneById(id: ObjectId): Patient
    override fun deleteAll()
}