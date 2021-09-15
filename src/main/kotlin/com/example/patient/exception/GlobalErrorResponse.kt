package com.example.patient.exception

import org.springframework.http.HttpStatus

class GlobalErrorResponse(
    val httpStatus: HttpStatus,
    val errorMessage: String? = "Something went wrong"
)


