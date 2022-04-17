package fr.univ.givr.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.model.role.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Account with all information about a user.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "ACCOUNT")
@Entity
public class Account {

    /**
     * Id of the account
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * Email.
     */
    @Column(name = "email")
    private String email;

    /**
     * Password linked to the mail.
     */
    @Column(name = "password")
    private String password;

    /**
     * Firstname of the user.
     */
    @Column(name = "firstname")
    private String firstname;

    /**
     * Lastname of the user.
     */
    @Column(name = "lastname")
    private String lastname;

    /**
     * Address where is located the user.
     */
    @Column(name = "address")
    private String address;

    /**
     * Know if the account is verified.
     */
    @Column(name = "verified")
    private boolean verified;

    /**
     * Know if the account is banned.
     */
    @Column(name = "banned")
    private boolean banned;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ACCOUNT_ROLE",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ID")
    )
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ACCOUNT_ANNOUNCEMENT",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "announcement_id", referencedColumnName = "ID")
    )
    private Set<Announcement> announcements;
}
