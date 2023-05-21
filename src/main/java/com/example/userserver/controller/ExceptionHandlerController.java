package com.example.userserver.controller;

import com.example.userserver.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleUnwantedException(IllegalArgumentException e) {
        // Log lỗi ra và ẩn đi message thực sự
        e.printStackTrace();  // Thực tế người ta dùng logger
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(400);
        responseDTO.setMessage(e.getMessage());

        return ResponseEntity.status(400).body(responseDTO);
    }

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleAuthenticationException(Exception e) {
        e.printStackTrace();
        log.error("Unauthorized request: " + e.getMessage(), e);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(403);
        responseDTO.setMessage(e.getMessage());

        return ResponseEntity.ok().body(responseDTO);
    }
}
