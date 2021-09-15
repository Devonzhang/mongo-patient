package com.example.patient.service

import com.example.patient.dto.PatientRequestDTO
import com.example.patient.entity.Doctor
import com.example.patient.entity.Patient
import com.example.patient.repository.DoctorRepository
import com.example.patient.repository.PatientRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PatientAddDoctorService(
    private val doctorRepository: DoctorRepository,
    private val patientRepository: PatientRepository
) {
    fun addDoctor(patient: PatientRequestDTO): Patient {
        val doctor = selectDoctor()
        updatePatients(doctor)
        return Patient(patient.name, patient.description, doctor)
    }

    private fun selectDoctor(): Doctor {
        val doctor = doctorRepository.findAll().minByOrNull { it.patientNumber }!!
        doctor.patientNumber++
        doctor.modifiedDate = LocalDateTime.now()
        doctorRepository.save(doctor)
        return doctor
    }

    private fun updatePatients(doctor: Doctor) {
        val patientsNeedUpdate = patientRepository.findAll().filter { it.doctor.id == doctor.id }
        patientsNeedUpdate.forEach {
            it.doctor = doctor
            it.modifiedDate = LocalDateTime.now()
        }
        patientRepository.saveAll(patientsNeedUpdate)
    }
}