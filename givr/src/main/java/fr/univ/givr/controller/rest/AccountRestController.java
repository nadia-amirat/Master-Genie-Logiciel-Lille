package fr.univ.givr.controller.rest;

import fr.univ.givr.model.StatusResource;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.DeleteAccountDTO;
import fr.univ.givr.model.account.ModifyAccountDTO;
import fr.univ.givr.model.account.UserInfo;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.service.AuthenticateService;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.utils.RestUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.HashSet;

/**
 * Controller to interact with the account of a customer.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

    /**
     * Modify account data with all null values.
     */
    private static final ModifyAccountDTO MODIFY_ACCOUNT_WITH_NULLS = new ModifyAccountDTO(null, null, null, null);

    /**
     * Service to interact with account data.
     */
    private final AccountService accountService;

    /**
     * Rest utils to build response.
     */
    private final RestUtils restUtils;

    /**
     * Service to manage authentication.
     */
    private final AuthenticateService service;

    /**
     * Get a piece of information about an account by his email.
     * The data retrieved are not critical.
     *
     * @param email Account email.
     * @return The information about the account.
     */
    @Secured(Role.ADMIN)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserInfo getUserInformationAccount(@RequestParam("email") String email) {
        return accountService.getUserInfoByEmail(email);
    }

    /**
     * Modify the account of the user.
     *
     * @param headers       Http request header.
     * @param user          Client information who wants change information.
     * @param modifyAccount Data that will be modified about the user account.
     */
    @Secured(Role.USER)
    @PatchMapping("/modify")
    public ResponseEntity<StatusResource> modify(
            @RequestHeader HttpHeaders headers,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ModifyAccountDTO modifyAccount
    ) throws MessagingException {
        if (!wantsToModifyAtLeastOneField(modifyAccount)) {
            return restUtils.statusToResponse(new StatusResource(
                    HttpStatus.NOT_MODIFIED,
                    "Nécessaire d'avoir au minimum une donnée définie"
            ));
        }

        Collection<String> messages = new HashSet<>();

        String email = user.getUsername();
        Account account = accountService.getByEmailOrThrow(email);

        String newEmail = modifyAccount.getEmail();
        if (newEmail != null && !newEmail.equals(account.getEmail())) {
            accountService.existsEmailThrow(newEmail);
            accountService.sendMailToModifyEmail(headers, email, newEmail);
            messages.add("Un mail a été envoyé à votre adresse mail, veuillez confirmer votre choix de changer d'adresse mail");
        }

        if (accountService.modifyInfoAccount(account, modifyAccount)) {
            messages.add("Vos coordonnées ont été modifiées");
        }

        if (messages.isEmpty()) {
            return restUtils.statusToResponse(new StatusResource(HttpStatus.NOT_MODIFIED, "Aucune donnée n'a été modifiée"));
        }

        return restUtils.statusToResponse(new StatusResource(
                HttpStatus.OK,
                messages
        ));
    }

    /**
     * Check if at least one field must be modified.
     *
     * @param modifyAccount New data for account.
     * @return {@code true} if there is one field not null, {@code false} otherwise.
     */
    private boolean wantsToModifyAtLeastOneField(@NonNull ModifyAccountDTO modifyAccount) {
        return !modifyAccount.equals(MODIFY_ACCOUNT_WITH_NULLS);
    }

    /**
     * Delete user's account.
     *
     * @param headers          Http request header.
     * @param user             Client information who wants change information.
     * @param deleteAccountDTO User's email
     */
    @Secured(Role.USER)
    @DeleteMapping
    public ResponseEntity<StatusResource> delete(HttpServletResponse response,
                                                 @RequestHeader HttpHeaders headers,
                                                 @AuthenticationPrincipal User user, @RequestBody DeleteAccountDTO deleteAccountDTO) throws MessagingException {
        Collection<String> messages = new HashSet<>();

        String userEmail = user.getUsername();
        if (!userEmail.equals(deleteAccountDTO.getEmail())) {
            return restUtils.statusToResponse(new StatusResource(HttpStatus.NOT_MODIFIED,
                    "Veuillez saisir votre propre addresse mail"));
        }

        Account account = accountService.getByEmailOrThrow(userEmail);
        accountService.deleteAccountById(account.getId());
        service.removeCookieAuth(response);
        return restUtils.statusToResponse(new StatusResource(
                HttpStatus.OK,
                "Votre compte a bien été supprimé"
        ));

    }
}
