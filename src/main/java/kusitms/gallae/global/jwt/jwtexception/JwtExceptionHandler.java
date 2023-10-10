package kusitms.gallae.global.jwt.jwtexception;

import jakarta.servlet.http.HttpServletRequest;
import kusitms.gallae.exception.ErrorResponse;
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
//@RestControllerAdvice
public class JwtExceptionHandler {

}
