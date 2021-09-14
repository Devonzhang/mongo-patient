package com.example.MongoPractice.repository

import com.example.MongoPractice.entity.Doctor
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface DoctorRepository : MongoRepository<Doctor, String> {
    fun findOneById(id: ObjectId): Doctor
    fun save(doctor: Doctor): Doctor
    override fun deleteAll()
}