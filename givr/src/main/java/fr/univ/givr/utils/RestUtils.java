package fr.univ.givr.utils;

import fr.univ.givr.model.StatusResource;
import lombok.NonNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

/**
 * Utils class to manage response generated by rest controllers.
 */
@Component
public class RestUtils {

    /**
     * Wrap the instance of status ressource to a response entity to send it to the client.
     * The response has the HTTP status defined in the resource instance.
     * The response body corresponds to the resource object.
     *
     * @param status Resource to describe the state of the request.
     * @return The response entity built.
     */
    public ResponseEntity<StatusResource> statusToResponse(@NonNull StatusResource status) {
        return ResponseEntity.status(status.getStatus()).body(status);
    }

    public ResponseEntity<StatusResource> bindingErrorFieldsToResponse(BindingResult result) {
        return statusToResponse(new StatusResource(HttpStatus.BAD_REQUEST, result.getFieldErrors().stream().map(
                DefaultMessageSourceResolvable::getDefaultMessage).collect(
                Collectors.toList())));
    }
}
