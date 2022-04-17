package fr.univ.givr.controller.rest;

import fr.univ.givr.mapper.AnnouncementMapper;
import fr.univ.givr.model.StatusResource;
import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.model.announcement.AnnouncementDTO;
import fr.univ.givr.model.announcement.DonationImage;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.repository.AnnouncementRepository;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.service.announcement.AnnouncementService;
import fr.univ.givr.utils.RestUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/donations")
public class DonationsController {

    private final AnnouncementMapper announcementMapper;

    private final AccountService accountService;

    private final AnnouncementRepository announcementRepository;

    private final AnnouncementService announcementService;

    private final RestUtils restUtils;

    @Secured(Role.USER)
    @PostMapping
    public ResponseEntity<StatusResource> create(@AuthenticationPrincipal User user,
                                                 @Valid @ModelAttribute AnnouncementDTO announcementDTO,
                                                 BindingResult result
    ) {
        if (result.hasErrors()) {
            return restUtils.bindingErrorFieldsToResponse(result);
        }

        Announcement announcement = announcementMapper.dtoToAnnouncement(announcementDTO);
        announcement.setImages(announcementDTO.getImages()
                .stream()
                .map(this::createDonationImage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );

        announcement.setAccount(accountService.getByEmailOrThrow(user.getUsername()));
        announcementRepository.save(announcement);

        return restUtils.statusToResponse(new StatusResource(HttpStatus.CREATED, "Votre donation a été créée !"));
    }

    private DonationImage createDonationImage(MultipartFile image) {
        try {
            return DonationImage.builder()
                    .data(image.getBytes())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteDonations(@RequestBody List<Long> donationsIDs) {
        // Get all ids not in the database
        // If list not empty (so all ids are registered)
        // Delete all
        // Else
        // Notice user about the non registered ids
        // TODO deletes multiple donation in the same time
        return ResponseEntity.ok().build();
    }

    /**
     * Saves the announcement editing.
     * @param id Id of the donation that will be updated.
     * @param announcementDTO Updated announcement
     * @return An HTTP code to indicate if the operation succeed.
     */
    @Secured(Role.USER)
    @PutMapping("/{id}")
    public ResponseEntity<StatusResource> edit(@PathVariable Long id,@AuthenticationPrincipal User user,
                                                 @Valid @ModelAttribute AnnouncementDTO announcementDTO,
                                                 BindingResult result
    ) {
        if (result.hasErrors()) {
            return restUtils.bindingErrorFieldsToResponse(result);
        }
        Announcement announcement = announcementService.findById(id);

        announcement.setImages(announcementDTO.getImages()
                .stream()
                .map(this::createDonationImage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setDescription(announcementDTO.getDescription());
        announcementRepository.save(announcement);

        return restUtils.statusToResponse(new StatusResource(HttpStatus.CREATED, "Votre donation a été mis à jour !"));
    }


}
