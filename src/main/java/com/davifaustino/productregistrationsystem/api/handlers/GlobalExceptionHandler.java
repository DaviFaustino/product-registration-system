package com.davifaustino.productregistrationsystem.api.handlers;

import java.sql.Timestamp;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.davifaustino.productregistrationsystem.api.dtos.ErrorResponseDto;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleRecordConflictException(RecordConflictException e, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), new Timestamp(System.currentTimeMillis()), request.getRequestURI(), request.getMethod());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), new Timestamp(System.currentTimeMillis()), request.getRequestURI(), request.getMethod());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), new Timestamp(System.currentTimeMillis()), request.getRequestURI(), request.getMethod());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), new Timestamp(System.currentTimeMillis()), request.getRequestURI(), request.getMethod());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
