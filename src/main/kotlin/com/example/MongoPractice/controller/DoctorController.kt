package com.example.MongoPractice.controller

import com.example.MongoPractice.dto.DoctorRequest
import com.example.MongoPractice.entity.Doctor
import com.example.MongoPractice.repository.DoctorRepository
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/doctors")
class DoctorController(
    private val doctorRepository: DoctorRepository
) {
    @GetMapping
    fun getAllDoctors(): ResponseEntity<List<Doctor>> {
        val doctors = doctorRepository.findAll()
        return ResponseEntity.ok(doctors)
    }

    @GetMapping("/{id}")
    fun getOneDoctor(@PathVariable("id") id: String): ResponseEntity<Doctor> {
        val doctor = doctorRepository.findOneById(ObjectId(id))
        return ResponseEntity.ok(doctor)
    }

    @PostMapping
    fun createDoctor(@RequestBody doctor: DoctorRequest): Doctor {
        return doctorRepository.save(Doctor(doctor.name, doctor.description))
    }
}