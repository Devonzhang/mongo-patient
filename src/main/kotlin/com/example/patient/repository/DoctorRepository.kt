package com.example.patient.repository

import com.example.patient.entity.Doctor
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface DoctorRepository : MongoRepository<Doctor, String> {
    fun findOneById(id: UUID): Doctor?
    fun save(doctor: Doctor): Doctor
    override fun deleteAll()
}