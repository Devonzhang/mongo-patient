package com.example.MongoPractice.controller

import com.example.MongoPractice.dto.PatientRequest
import com.example.MongoPractice.entity.Patient
import com.example.MongoPractice.repository.PatientRepository
import com.example.MongoPractice.service.PatientAddDoctorService
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/patients")
class PatientController(
    private val patientRepository: PatientRepository,
    private val patientAddDoctorService: PatientAddDoctorService
) {
    @GetMapping
    fun getAllPatients(): ResponseEntity<List<Patient>> {
        val patients = patientRepository.findAll()
        return ResponseEntity.ok(patients)
    }

    @GetMapping("/{id}")
    fun getOnePatient(@PathVariable("id") id: String): ResponseEntity<Patient> {
        val patient = patientRepository.findOneById(ObjectId(id))
        return ResponseEntity.ok(patient)
    }

    @PostMapping
    fun createPatient(@RequestBody patient: PatientRequest): Patient {
        return patientRepository.save(patientAddDoctorService.addDoctor(patient))
    }
}