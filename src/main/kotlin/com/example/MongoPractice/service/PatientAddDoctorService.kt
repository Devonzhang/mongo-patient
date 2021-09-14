package com.example.MongoPractice.service

import com.example.MongoPractice.dto.PatientRequest
import com.example.MongoPractice.entity.Doctor
import com.example.MongoPractice.entity.Patient
import com.example.MongoPractice.repository.DoctorRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PatientAddDoctorService(
    private val doctorRepository: DoctorRepository,
) {
    fun addDoctor(patient: PatientRequest): Patient {
        val doctor = selectDoctor()
        return Patient(patient.name, patient.description, doctor)
    }

    fun selectDoctor(): Doctor {
        var doctor = doctorRepository.findAll().minByOrNull { it.patientNumber }!!
        doctor.patientNumber++
        doctor.modifiedDate = LocalDateTime.now()
        doctorRepository.save(doctor)
        return doctor
    }
}