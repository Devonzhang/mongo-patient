package com.example.patient.controller

import com.example.patient.dto.PatientRequestDTO
import com.example.patient.entity.Doctor
import com.example.patient.entity.Patient
import com.example.patient.repository.PatientRepository
import com.example.patient.service.PatientAddDoctorService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime
import java.util.*

@WebMvcTest(controllers = [PatientController::class])
@ExtendWith(SpringExtension::class, MockKExtension::class)
internal class PatientControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var patientRepository: PatientRepository

    @MockkBean
    private lateinit var patientAddDoctorService: PatientAddDoctorService

    private val objectMapper = jacksonObjectMapper()

    private var fakeDoctor1 = Doctor(
        "testDoctor1",
        "this is a test doctor",
        0
    )
    private var fakeDoctor2 = Doctor(
        "testDoctor2",
        "this is a test doctor",
        2
    )
    private var fakePatient1 = Patient("testPatient1", "this is a test patient", fakeDoctor2)
    private var fakePatient2 = Patient("testPatient2", "this is a test patient", fakeDoctor2)
    private var fakePatientList = listOf(fakePatient1, fakePatient2)

    @Test
    fun `should return all patients`() {
        every {
            patientRepository.findAll()
        } returns fakePatientList

        mockMvc.perform(MockMvcRequestBuilders.get("/patients"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("testPatient1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("testPatient2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].doctor.name").value("testDoctor2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].doctor.name").value("testDoctor2"))
    }

    @Test
    fun `should return one patient`() {
        every {
            patientRepository.findOneById(fakePatient1.id)
        } returns fakePatient1

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/${fakePatient1.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testPatient1"))

    }

    @Test
    fun `should throw when patient id not exist`() {
        val invalidId = UUID.randomUUID()

        every {
            patientRepository.findOneById(invalidId)
        } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/$invalidId"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect { MockMvcResultMatchers.jsonPath("$.message").value("No patient with id $invalidId") }

    }

    @Test
    fun `should create a new patient and assign a doctor and return`() {
        val patientRequestDTO = PatientRequestDTO("testPatientNew", "this is a test patient request")
        fakeDoctor1.patientNumber++
        fakeDoctor1.modifiedDate = LocalDateTime.now()
        val patientShouldReturn = Patient(patientRequestDTO.name, patientRequestDTO.description, fakeDoctor1)

        every {
            patientAddDoctorService.addDoctor(any())
        } returns patientShouldReturn
        every {
            patientRepository.save(patientShouldReturn)
        } returns patientShouldReturn

        mockMvc.perform(
            MockMvcRequestBuilders.post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientRequestDTO))
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testPatientNew"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.doctor.patientNumber").value(1))
    }

    @Test
    fun `should fail to create a patient without name`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/patients")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect { MockMvcResultMatchers.content().string("Invalid Name!") }
        mockMvc.perform(
            MockMvcRequestBuilders.post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "name":"  ",
                        "description":""
                    }
                """.trimIndent()
                )
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect { MockMvcResultMatchers.jsonPath("$.description").value("Invalid Name!") }
    }
}