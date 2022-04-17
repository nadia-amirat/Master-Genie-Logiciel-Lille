package fr.univ.givr.model.account;

import fr.univ.givr.constraint.Name;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO class for user data.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccountDTO extends AuthenticateDto {

    /**
     * Firstname of the user.
     */
    @Name(message = "Le prénom doit contenir exclusivement des lettres")
    @Size(max = 100, message = "Le prénom ne peut pas excéder les 100 caractères")
    @NotNull(message = "Le prénom doit être défini")
    private String firstname;

    /**
     * Lastname of the user.
     */
    @Name(message = "Le nom doit contenir exclusivement des lettres")
    @Size(max = 100, message = "Le nom ne peut pas excéder les 100 caractères")
    @NotNull(message = "Le nom doit être défini")
    private String lastname;

    /**
     * Address where is located the user.
     */
    @Size(min = 1, max = 255, message = "L'adresse doit être définie dans un interval de 1 à 255 lettres")
    @NotNull(message = "L'adresse doit être définie")
    private String address;
}
