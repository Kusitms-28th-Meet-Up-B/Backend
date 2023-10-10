package kusitms.gallae.exception;

import jakarta.servlet.http.HttpServletRequest;
import kusitms.gallae.global.jwt.jwtexception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class UnitExceptionHandler {
    // CustomException
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(JwtException e, HttpServletRequest request) {
        log.error("[CustomException] url: {} | errorType: {} | errorMessage: {} | cause Exception: ",
                request.getRequestURL(), 403, e.getMessage(), e.getCause());

        return ResponseEntity
                .status(403)
                .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "JwtERROR", e.getMessage(), request.getRequestURI()));
    }

    // Not Support Http Method Exception
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMethodException(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request
    ) {
        log.error("[HttpRequestMethodNotSupportedException] " +
                        "url: {} | errorType: {} | errorMessage: {} | cause Exception: ",
                request.getRequestURL(), 400, "지원하지않는 method", e);

        return ResponseEntity
                .status(400)
                .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "NotSupportMethod", e.getMessage(), request.getRequestURI()));
    }

    // Validation Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        String validationMessage = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        log.error("[MethodArgumentNotValidException] url: {} | errorType: {} | errorMessage: {} | cause Exception: ",
                request.getRequestURL(), 401, validationMessage, e);

        return ResponseEntity
                .status(401)
                .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "MethodNotVaild", e.getMessage(), request.getRequestURI()));
    }

    // 이외 Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest request) {
        log.error("[Common Exception] url: {} | errorMessage: {}",
                request.getRequestURL(), e.getMessage());
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Error", e.getMessage(), request.getRequestURI()));
    }
}
