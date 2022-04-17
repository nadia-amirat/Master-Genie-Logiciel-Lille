
package fil.univ.login.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 12)
    private String phoneNumber;

    @Column(length = 50)
    private String token;

    /* last connection success */
    private LocalDateTime lastSuccess;

    /* last connection try */
    private LocalDateTime lastTry;


    public User(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User() {}

    public void setToken(String token) {this.token = token;}
    public void setLastTry(LocalDateTime lastTry) {this.lastTry = lastTry;}
    public void setLastSuccess(LocalDateTime lastSuccess) {this.lastSuccess = lastSuccess;}

}

