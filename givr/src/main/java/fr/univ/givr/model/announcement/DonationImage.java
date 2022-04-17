package fr.univ.givr.model.announcement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Table(name = "IMAGES")
@Entity
public class DonationImage {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "data")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @JsonIgnore
    @ManyToOne
    @JoinTable(
            name = "DONATION_IMAGES",
            joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "announcement_id", referencedColumnName = "ID")
    )
    private Announcement donation;
}
