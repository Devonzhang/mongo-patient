package com.example.patient.dto

import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated

class DoctorRequestDTO(
    @Validated
    @NotNull
    val name: String,
    val description: String
)