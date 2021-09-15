package com.example.patient.controller

import com.example.patient.dto.PatientRequestDTO
import com.example.patient.entity.Patient
import com.example.patient.exception.GlobalErrorResponse
import com.example.patient.exception.IdNotFoundException
import com.example.patient.exception.InvalidNameException
import com.example.patient.repository.PatientRepository
import com.example.patient.service.PatientAddDoctorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/patients")
class PatientController(
    private val patientRepository: PatientRepository,
    private val patientAddDoctorService: PatientAddDoctorService
) {
    @ExceptionHandler(IdNotFoundException::class)
    fun wrongPatientId(idNotFoundException: IdNotFoundException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity(
            GlobalErrorResponse(
                HttpStatus.BAD_REQUEST,
                idNotFoundException.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(InvalidNameException::class)
    fun wrongPatientName(invalidNameException: InvalidNameException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity(
            GlobalErrorResponse(
                HttpStatus.BAD_REQUEST,
                invalidNameException.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @GetMapping
    fun getAllPatients(): ResponseEntity<List<Patient>> {
        val patients = patientRepository.findAll()
        return ResponseEntity.ok(patients)
    }

    @GetMapping("/{id}")
    fun getOnePatient(@PathVariable("id") id: UUID): ResponseEntity<Patient> {
        val patient = patientRepository.findOneById(id)
        try {
            patient!!
        } catch (nullPointerException: NullPointerException) {
            throw IdNotFoundException("No patient with id $id")
        }
        return ResponseEntity.ok(patient)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPatient(@RequestBody patient: PatientRequestDTO): Patient {
        if (patient.name.isBlank()) {
            throw InvalidNameException("Name can not be blank!")
        }
        return patientRepository.save(patientAddDoctorService.addDoctor(patient))
    }
}