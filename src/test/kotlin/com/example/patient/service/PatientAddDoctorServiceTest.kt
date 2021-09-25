package com.example.patient.service

import com.example.patient.dto.PatientRequestDTO
import com.example.patient.entity.Doctor
import com.example.patient.entity.Patient
import com.example.patient.exception.PersonNotFoundException
import com.example.patient.repository.DoctorRepository
import com.example.patient.repository.PatientRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockKExtension::class)
@Import(PatientAddDoctorService::class)
internal class PatientAddDoctorServiceTest {
    @Autowired
    private lateinit var patientAddDoctorService: PatientAddDoctorService

    @MockkBean(relaxed = true)
    private lateinit var patientRepository: PatientRepository

    @MockkBean(relaxed = true)
    private lateinit var doctorRepository: DoctorRepository

    private var fakeDoctor1 = Doctor(
        "testDoctor",
        "this is a test doctor",
        1
    )
    private var fakeDoctor2 = Doctor(
        "testDoctor",
        "this is a test doctor",
        5
    )

    private var fakeDoctorList = listOf(fakeDoctor1, fakeDoctor2)
    private var smallFakeDoctorList = listOf(fakeDoctor1)
    private var fakePatient = Patient("testPatient", "this is a test patient", fakeDoctor1)
    private var fakePatientList = listOf(fakePatient)
    private var fakePatientRequest = PatientRequestDTO("testPatientRequest", "this is a test patient request")


    @Test
    fun `should return the patient with the doctor has least patients`() {
        every {
            doctorRepository.findAll()
        } returns fakeDoctorList
        every {
            doctorRepository.save(fakeDoctor1)
        } returns fakeDoctor1
        every {
            doctorRepository.save(fakeDoctor2)
        } returns fakeDoctor2
        every {
            doctorRepository.saveAll(smallFakeDoctorList)
        } returns smallFakeDoctorList
        every {
            patientRepository.findAll()
        } returns fakePatientList

        val patientResult = patientAddDoctorService.addDoctor(fakePatientRequest)

        assertThat(patientResult.doctor).isEqualTo(fakeDoctor1)
        assertThat(fakeDoctor1.patientNumber).isEqualTo(2)

    }

    @Test
    fun `should throw when there is no doctor exists`() {
        every {
            doctorRepository.findAll()
        } returns listOf()
        assertThrows<PersonNotFoundException> {
            patientAddDoctorService.addDoctor(fakePatientRequest)
        }
    }


}