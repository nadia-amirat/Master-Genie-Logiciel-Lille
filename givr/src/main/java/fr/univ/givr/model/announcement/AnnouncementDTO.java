package fr.univ.givr.model.announcement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO class for announcement data.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AnnouncementDTO {

    /**
     * Title of the announcement.
     */
    @Size(max = 60, message = "Le titre ne peut pas excéder les 60 caractères")
    @NotNull(message = "Le titre doit être défini")
    private String title;

    /**
     * Description of the announcement.
     */
    @Size(max = 320, message = "La description ne peut pas excéder les 320 caractères")
    @NotNull(message = "La description doit être définie")
    private String description;

    @NotEmpty(message = "Il faut au minimum une image pour illustrer le donation")
    @Size(max = 5, message = "Au maximum 5 images sont autorisées")
    private List<MultipartFile> images;
}
