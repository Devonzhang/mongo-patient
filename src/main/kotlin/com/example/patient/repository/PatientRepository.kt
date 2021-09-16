package com.example.patient.repository

import com.example.patient.entity.Patient
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PatientRepository : MongoRepository<Patient, String> {
    fun findOneById(id: UUID): Patient?
    override fun deleteAll()
}