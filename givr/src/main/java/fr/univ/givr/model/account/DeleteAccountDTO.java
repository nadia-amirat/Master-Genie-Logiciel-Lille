package fr.univ.givr.model.account;


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
public class DeleteAccountDTO {
    /**
     * Email corresponding to the id of the account.
     */
    @Email(message = "L'adresse mail n'est pas conforme")
    @Size(min = 1, message = "L'adresse mail ne peut pas être vide")
    private String email;
}
