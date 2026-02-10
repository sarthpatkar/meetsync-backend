package com.meetingscheduler.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,String>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "database error: " + ex.getRootCause().getMessage()));
    }

    // ✅ Custom meeting conflict handler
    @ExceptionHandler(MeetingConflictException.class)
    public ResponseEntity<Map<String, String>> handleMeetingConflict(MeetingConflictException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}