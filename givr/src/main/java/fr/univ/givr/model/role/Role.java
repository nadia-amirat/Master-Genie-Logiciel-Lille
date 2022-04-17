package fr.univ.givr.model.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.univ.givr.model.account.Account;
import lombok.*;

import javax.persistence.*;

/**
 * Role representing one permission for a user.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "ROLE")
@Entity
public class Role {

    /**
     * USER role permission.
     */
    public static final String USER = "ROLE_USER";

    /**
     * ADMIN role permission.
     */
    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Id of an account.
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * Name of the permission.
     */
    @Column(name = "permission")
    private String permission;

    @JsonIgnore
    @ManyToOne
    @JoinTable(
            name = "ACCOUNT_ROLE",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "account_id", referencedColumnName = "ID")
    )
    private Account account;
}
