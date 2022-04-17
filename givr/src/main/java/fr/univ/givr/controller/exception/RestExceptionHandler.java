package fr.univ.givr.controller.exception;

import fr.univ.givr.model.StatusResource;
import fr.univ.givr.utils.RestUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest exception handler to catch and send error information to a client.
 */
@RestControllerAdvice(basePackages = "fr.univ.givr.controller.rest")
@Slf4j
@AllArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final RestUtils restUtils;

    /**
     * Handle an unknown exception.
     * if the exception is annotated by {@link ResponseStatus}, retrieve the information from it to return the status and body,
     * otherwise, send an internal server error to the client.
     *
     * @param ex Exception caught.
     * @return A response entity with the error.
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<StatusResource> handleUnknown(Throwable ex) {
        return createErrorWithDefaultOrResponseStatus(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur imprévue s'est produite",
                ex
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return new ResponseEntity<>(StatusResource.builder()
                                            .status(status)
                                            .messages(fieldsToMessages(fieldErrors))
                                            .build(), headers, status);
    }

    /**
     * Get the messages for all field error.
     *
     * @param fieldErrors All fields where the format is not valid.
     * @return A collection of message.
     */
    private Collection<String> fieldsToMessages(List<FieldError> fieldErrors) {
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
    }

    /**
     * Handle the access denied exception.
     *
     * @return A response entity with the error with unauthorized status.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StatusResource> handleUnknown() {
        return restUtils.statusToResponse(StatusResource.builder()
                                                  .messages(List.of(
                                                          "Vous n'êtes pas autorisé à intéragir avec cette ressource"))
                                                  .status(HttpStatus.UNAUTHORIZED)
                                                  .build()
        );
    }

    /**
     * Create an instance of {@link StatusResource} with the default values if the annotation {@link ResponseStatus} is not found
     * on the class.
     *
     * @param defaultStatus Default http status if the annotation {@link ResponseStatus} is not found.
     * @param defaultError  Default http status if the annotation {@link ResponseStatus} is not found or if the reason is empty.
     * @param exception     Exception thrown.
     * @return A new instance of {@link StatusResource} in response to describe the problem to the customer.
     */
    private ResponseEntity<StatusResource> createErrorWithDefaultOrResponseStatus(
            HttpStatus defaultStatus,
            String defaultError,
            Throwable exception
    ) {
        var status = defaultStatus;
        var msgError = defaultError;

        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);

        if (responseStatus != null) {
            status = responseStatus.value();
            String reason = responseStatus.reason();

            if (!reason.isEmpty()) {
                msgError = reason;
            }
        } else {
            log.error("An exception without [{}] annotation has been thrown", ResponseStatus.class, exception);
        }

        return restUtils.statusToResponse(StatusResource.builder()
                                                  .messages(List.of(msgError))
                                                  .status(status)
                                                  .build()
        );
    }
}
