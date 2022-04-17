package fr.univ.givr.model.announcement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.univ.givr.model.account.Account;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Table(name = "ANNOUNCEMENT")
@Entity
public class Announcement {

    /**
     * Id of the announcement
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * Title.
     */
    @Column(name = "title")
    private String title;

    /**
     * Description.
     */
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "DONATION_IMAGES",
            joinColumns = @JoinColumn(name = "announcement_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "ID")
    )
    private List<DonationImage> images;

    @JsonIgnore
    @ManyToOne
    @JoinTable(
            name = "ACCOUNT_ANNOUNCEMENT",
            joinColumns = @JoinColumn(name = "announcement_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "account_id", referencedColumnName = "ID")
    )
    private Account account;
}
