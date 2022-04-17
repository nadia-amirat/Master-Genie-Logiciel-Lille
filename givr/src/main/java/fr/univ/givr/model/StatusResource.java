package fr.univ.givr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents information about the status of the request.
 */
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResource {

    /**
     * The human-readable HTTP.
     */
    @JsonProperty("status")
    private HttpStatus status;

    /**
     * The date when the answer is sent.
     */
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * The messages to advice customer
     */
    @JsonProperty("messages")
    private Collection<String> messages;

    public StatusResource(HttpStatus status, Collection<String> messages) {
        this(status, LocalDateTime.now(), messages);
    }

    public StatusResource(HttpStatus status, String... messages) {
        this(status, Arrays.asList(messages));
    }
}