package ir.zabetan.ewallet.web.rest.errors;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice("ir.zabetan.ewallet")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { BadRequestException.class})
    protected ResponseEntity<Object> bad_request_exception (RuntimeException ex, WebRequest request) {

            String bodyOfResponse = "{" +
                    "\"message\": \""+ex.getMessage()+"\"," +
                    "\"code\": \""+ HttpStatus.BAD_REQUEST+"\"" +
                    "}";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(ex, bodyOfResponse,
                httpHeaders, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String bodyOfResponse = "{" +
                "\"message\": \""+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()+"\"," +
                "\"code\": \""+ HttpStatus.BAD_REQUEST+"\"" +
                "}";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(ex, bodyOfResponse,
                httpHeaders, HttpStatus.BAD_REQUEST, request);
    }
}