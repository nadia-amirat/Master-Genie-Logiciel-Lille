package fr.univ.givr.controller.rest;

import fr.univ.givr.mapper.AccountMapper;
import fr.univ.givr.model.StatusResource;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.AccountDTO;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.service.register.RegisterService;
import fr.univ.givr.utils.RestUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to register new account.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/register")
public class RegisterRestController {

    /**
     * Service to interact with account data.
     */
    private final AccountService accountService;

    /**
     * Mapper to transform DTO to data.
     */
    private final AccountMapper mapper;

    /**
     * Service to register new account and send confirmation mail.
     */
    private final RegisterService registerService;

    private final RestUtils restUtils;

    /**
     * Endpoint to register a new account.
     * Create a new account unverified and register it.
     *
     * @param headers    Header of the http request.
     * @param accountDto Account data with all necessary information to create a new account.
     * @return Resource to say to the client the status of the request.
     */
    @PostMapping
    public ResponseEntity<StatusResource> registerAccount(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody AccountDTO accountDto
    ) {
        String email = accountDto.getEmail();
        accountService.existsEmailThrow(email);

        Account account = mapper.dtoToAccount(accountDto);

        accountService.createAccount(account);
        registerService.sendEmailConfirmation(headers, email);
        return restUtils.statusToResponse(new StatusResource(HttpStatus.OK, "Un mail a été envoyé"));
    }

    /**
     * Endpoint to obtain a new email to validate the account.
     *
     * @param headers Header of the http request.
     * @param email   Email of the user that will receive the mail.
     * @return Resource to say to the client the status of the request.
     */
    @GetMapping
    public ResponseEntity<StatusResource> resendValidationEmail(
            @RequestHeader HttpHeaders headers,
            @RequestParam("email") String email
    ) {
        registerService.sendEmailConfirmation(headers, email);
        return restUtils.statusToResponse(new StatusResource(
                HttpStatus.OK,
                "Un mail a été envoyé si l'adresse mail est déjà enregistrée"
        ));
    }
}
