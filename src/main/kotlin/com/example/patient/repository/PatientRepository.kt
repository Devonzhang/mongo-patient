package com.example.patient.repository

import com.example.patient.entity.Patient
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface PatientRepository : MongoRepository<Patient, String> {
    fun findOneById(id: UUID): Patient?
    override fun deleteAll()
}