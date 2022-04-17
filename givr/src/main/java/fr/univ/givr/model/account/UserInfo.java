package fr.univ.givr.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserInfo {

    /**
     * Firstname of the user.
     */
    private String firstname;

    /**
     * Lastname of the user.
     */
    private String lastname;

    /**
     * Address where is located the user.
     */
    private String address;
}
