package es.josetesan.ai.poc.mcp.amadeus.exception;

import com.amadeus.exceptions.ResponseException;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(ResponseException.class)
    public ResponseEntity<String> handleAmadeusException(ResponseException e) {
        Error error = new Error(e.getCode(), e.getDescription());
        return ResponseEntity.status(e.getResponse().getStatusCode()).body(error.toString());
    }

    // @ExceptionHandler(MissingServletRequestParameterException.class)
    /*
     * public ResponseEntity<String>
     * handleMissingParams(MissingServletRequestParameterException e) {
     * 
     * Error error = new
     * Error("400",String.format("Missing parameter %s",e.getParameterName()));
     * return ResponseEntity.badRequest().body(error.toString());
     * }
     */

    // @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        Error error = new Error("500", e.getMessage());
        return ResponseEntity.internalServerError().body(error.toString());
    }

    public record Error(String code, String message) {
    }
}