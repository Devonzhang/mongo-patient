package com.example.patient.controller

import com.example.patient.dto.DoctorRequestDTO
import com.example.patient.entity.Doctor
import com.example.patient.repository.DoctorRepository
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(controllers = [DoctorController::class])
@ExtendWith(SpringExtension::class, MockKExtension::class)
internal class DoctorControllerTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var doctorRepository: DoctorRepository

    private val objectMapper = jacksonObjectMapper()

    private var fakeDoctor1 = Doctor(
        "testDoctor1",
        "this is a test doctor",
        1
    )
    private var fakeDoctor2 = Doctor(
        "testDoctor2",
        "this is a test doctor",
        5
    )
    private var fakeDoctorList = listOf(fakeDoctor1, fakeDoctor2)

    @Test
    fun `should return all doctors`() {
        every {
            doctorRepository.findAll()
        } returns fakeDoctorList

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("testDoctor1"))
            .andExpect(jsonPath("$[0].patientNumber").value(1))
            .andExpect(jsonPath("$[1].name").value("testDoctor2"))
            .andExpect(jsonPath("$[1].patientNumber").value(5))
    }

    @Test
    fun `should return one doctor`() {
        every {
            doctorRepository.findOneById(fakeDoctor1.id)
        } returns fakeDoctor1

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/${fakeDoctor1.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("testDoctor1"))
            .andExpect(jsonPath("$.patientNumber").value(1))
    }

    @Test
    fun `should throw when doctor id not exist`() {
        val invalidId = UUID.randomUUID()
        every {
            doctorRepository.findOneById(invalidId)
        } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/$invalidId"))
            .andExpect(status().isBadRequest)
            .andExpect { jsonPath("$.message").value("No patient with id $invalidId") }
    }

    @Test
    fun `should create a new doctor and return`() {
        val doctorRequest = DoctorRequestDTO("testAddDoctor", "this is a test doctor")
        val saveDoctor = Doctor(doctorRequest.name, doctorRequest.description)
        every {
            doctorRepository.save(any())
        } returns saveDoctor

        mockMvc.perform(
            MockMvcRequestBuilders.post("/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorRequest))
        )
            .andExpect(jsonPath("$.name").value("testAddDoctor"))
            .andExpect(jsonPath("$.patientNumber").value(0))
            .andExpect(jsonPath("$.description").value("this is a test doctor"))
    }

    @Test
    fun `should fail to create a doctor without name`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/doctors")
        )
            .andExpect(status().isBadRequest)
            .andExpect { content().string("Invalid Name!") }
    }


}