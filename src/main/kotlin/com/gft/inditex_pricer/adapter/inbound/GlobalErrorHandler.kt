package com.gft.inditex_pricer.adapter.inbound.api

import com.gft.inditex_pricer.adapter.inbound.dto.PriceNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

@ControllerAdvice
class GlobalErrorHandler {

    data class ErrorDTO(
        val code: String,
        val message: String,
        val status: Int,
        val timestamp: String,
        val path: String
    )

    private fun body(code: String, message: String, status: HttpStatus, path: String) =
        ErrorDTO(
            code = code,
            message = message,
            status = status.value(),
            timestamp = OffsetDateTime.now().toString(),
            path = path
        )

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(ex: MissingServletRequestParameterException, request: HttpServletRequest): ResponseEntity<ErrorDTO> {
        val msg = "Missing required parameter '${ex.parameterName}'"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body("MISSING_PARAMETER", msg, HttpStatus.BAD_REQUEST, request.requestURI))
    }

    @ExceptionHandler(DateTimeParseException::class)
    fun handleBadDate(ex: DateTimeParseException, request: HttpServletRequest): ResponseEntity<ErrorDTO> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body("INVALID_DATE", "Invalid 'applicationDate' format, expected ISO-8601", HttpStatus.BAD_REQUEST, request.requestURI))
    }

    @ExceptionHandler(PriceNotFoundException::class)
    fun handlePriceNotFound(ex: PriceNotFoundException, request: HttpServletRequest): ResponseEntity<ErrorDTO> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(body("PRICE_NOT_FOUND", ex.message ?: "Price not found", HttpStatus.NOT_FOUND, request.requestURI))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArg(ex: IllegalArgumentException, request: HttpServletRequest): ResponseEntity<ErrorDTO> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body("BAD_REQUEST", ex.message ?: "Invalid request", HttpStatus.BAD_REQUEST, request.requestURI))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorDTO> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(body("INTERNAL_ERROR", "Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, request.requestURI))
    }
}
