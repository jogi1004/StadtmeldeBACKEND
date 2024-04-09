package com.stadtmeldeapp.Controller;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* @ExceptionHandler(ExampleNotFoundException.class)
    public ResponseEntity<Object> handleExampleNotFoundException(ExampleNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(OtherException.class)
    public ResponseEntity<Object> handleOtherException(OtherException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    } */

    // Weitere Ausnahmen und entsprechende Handler nach Bedarf hinzufügen
}
