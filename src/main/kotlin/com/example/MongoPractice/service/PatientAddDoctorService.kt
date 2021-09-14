package com.example.MongoPractice.service

import com.example.MongoPractice.dto.PatientRequest
import com.example.MongoPractice.entity.Doctor
import com.example.MongoPractice.entity.Patient
import com.example.MongoPractice.repository.DoctorRepository
import com.example.MongoPractice.repository.PatientRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PatientAddDoctorService(
    private val doctorRepository: DoctorRepository,
    private val patientRepository: PatientRepository
) {
    fun addDoctor(patient: PatientRequest): Patient {
        val doctor = selectDoctor()
        updatePatients(doctor)
        return Patient(patient.name, patient.description, doctor)
    }

    fun selectDoctor(): Doctor {
        val doctor = doctorRepository.findAll().minByOrNull { it.patientNumber }!!
        doctor.patientNumber++
        doctor.modifiedDate = LocalDateTime.now()
        doctorRepository.save(doctor)
        return doctor
    }

    fun updatePatients(doctor: Doctor) {
        val patientsNeedUpdate = patientRepository.findAll().filter { it.doctor.id == doctor.id }
        patientsNeedUpdate.forEach {
            it.doctor = doctor
            it.modifiedDate = LocalDateTime.now()
        }
        patientRepository.saveAll(patientsNeedUpdate)
    }
}