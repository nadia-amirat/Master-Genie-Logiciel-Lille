package fr.univ.givr.model.account;

import fr.univ.givr.constraint.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ModifyAccountDTO {

    /**
     * Email corresponding to the id of the account.
     */
    @Email(message = "L'adresse mail n'est pas conforme")
    @Size(min = 1, message = "L'adresse mail ne peut pas être vide")
    private String email;

    /**
     * Firstname of the user.
     */
    @Name(message = "Le prénom doit contenir exclusivement des lettres")
    @Size(max = 100, message = "Le prénom ne peut pas excéder les 100 caractères")
    private String firstname;

    /**
     * Lastname of the user.
     */
    @Name(message = "Le nom doit contenir exclusivement des lettres")
    @Size(max = 100, message = "Le nom ne peut pas excéder les 100 caractères")
    private String lastname;

    /**
     * Address where is located the user.
     */
    @Size(min = 1, max = 255, message = "L'adresse doit être définie dans un interval de 1 à 255 lettres")
    private String address;
}
