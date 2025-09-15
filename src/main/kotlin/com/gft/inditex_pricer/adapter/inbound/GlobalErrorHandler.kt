package com.gft.inditex_pricer.adapter.inbound

import com.gft.inditex_pricer.adapter.inbound.dto.PriceNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

@RestControllerAdvice
class GlobalErrorHandler {

    data class ErrorDTO(
        val code: String,
        val message: String,
        val status: Int,
        val timestamp: String,
        val path: String
    )

    private fun body(code: String, message: String, status: HttpStatus, path: String) =
        ErrorDTO(code, message, status.value(), OffsetDateTime.now().toString(), path)

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(ex: MissingServletRequestParameterException, request: HttpServletRequest)
            : ResponseEntity<ErrorDTO> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body("MISSING_PARAMETER", "Missing required parameter '${ex.parameterName}'",
                HttpStatus.BAD_REQUEST, request.requestURI))

    @ExceptionHandler(DateTimeParseException::class)
    fun handleBadDate(ex: DateTimeParseException, request: HttpServletRequest)
            : ResponseEntity<ErrorDTO> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body("INVALID_DATE", "Invalid 'applicationDate' format, expected ISO-8601",
                HttpStatus.BAD_REQUEST, request.requestURI))

    @ExceptionHandler(PriceNotFoundException::class)
    fun handlePriceNotFound(ex: PriceNotFoundException, request: HttpServletRequest)
            : ResponseEntity<ErrorDTO> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(body("PRICE_NOT_FOUND", ex.message ?: "Price not found",
                HttpStatus.NOT_FOUND, request.requestURI))

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArg(ex: IllegalArgumentException, request: HttpServletRequest)
            : ResponseEntity<ErrorDTO> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body("BAD_REQUEST", ex.message ?: "Invalid request",
                HttpStatus.BAD_REQUEST, request.requestURI))

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, request: HttpServletRequest)
            : ResponseEntity<ErrorDTO> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(body("INTERNAL_ERROR", "Unexpected error",
                HttpStatus.INTERNAL_SERVER_ERROR, request.requestURI))
}
