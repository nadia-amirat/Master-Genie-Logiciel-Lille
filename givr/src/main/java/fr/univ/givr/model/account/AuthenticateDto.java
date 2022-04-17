package fr.univ.givr.model.account;

import fr.univ.givr.constraint.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Class with the authentication field necessary to identify a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateDto {

    /**
     * Email corresponding to the id of the account.
     */
    @Email(message = "L'adresse mail n'est pas conforme")
    @NotEmpty(message = "L'adresse mail ne peut pas être vide")
    private String email;

    /**
     * Password linked to the mail.
     */
    @Password(message = "Le mot de passe doit contenir 8 caractères dont : 1 minuscule, 1 majuscule et 1 chiffre")
    @NotNull(message = "Le mot de passe ne peut pas être vide")
    private String password;

}
