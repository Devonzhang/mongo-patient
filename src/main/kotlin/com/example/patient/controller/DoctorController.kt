package com.example.patient.controller

import com.example.patient.dto.DoctorRequestDTO
import com.example.patient.entity.Doctor
import com.example.patient.exception.GlobalErrorResponse
import com.example.patient.exception.PersonNotFoundException
import com.example.patient.exception.InvalidNameException
import com.example.patient.repository.DoctorRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/doctors")
class DoctorController(
    private val doctorRepository: DoctorRepository
) {
    @ExceptionHandler(PersonNotFoundException::class)
    fun wrongDoctorId(personNotFoundException: PersonNotFoundException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity(
            GlobalErrorResponse(
                HttpStatus.BAD_REQUEST,
                personNotFoundException.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(InvalidNameException::class)
    fun wrongDoctorName(invalidNameException: InvalidNameException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity(
            GlobalErrorResponse(
                HttpStatus.BAD_REQUEST,
                invalidNameException.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }


    @GetMapping
    fun getAllDoctors(): ResponseEntity<List<Doctor>> {
        val doctors = doctorRepository.findAll()
        return ResponseEntity.ok(doctors)
    }

    @GetMapping("/{id}")
    fun getOneDoctor(@PathVariable("id") id: UUID): ResponseEntity<Doctor> {
        val doctor = doctorRepository.findOneById(id)
        try {
            doctor!!
        } catch (nullPointerException: NullPointerException) {
            throw PersonNotFoundException("No patient with id $id")
        }
        return ResponseEntity.ok(doctor)
    }

    @PostMapping
    fun createDoctor(@RequestBody doctor: DoctorRequestDTO): Doctor {
        if (doctor.name.isBlank()) {
            throw InvalidNameException("Invalid Name!")
        }
        return doctorRepository.save(Doctor(doctor.name, doctor.description))
    }
}