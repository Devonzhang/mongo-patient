package com.example.patient.repository

import com.example.patient.entity.Doctor
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DoctorRepository : MongoRepository<Doctor, String> {
    fun findOneById(id: UUID): Doctor?
    fun save(doctor: Doctor): Doctor
    override fun deleteAll()
}