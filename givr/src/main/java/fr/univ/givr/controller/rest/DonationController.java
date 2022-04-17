package fr.univ.givr.controller.rest;

import fr.univ.givr.model.StatusResource;
import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.repository.AnnouncementRepository;
import fr.univ.givr.service.announcement.AnnouncementService;

import fr.univ.givr.utils.RestUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/donations/{donationId}")
public class DonationController {

    private final AnnouncementRepository announcementRepository;

    private final  AnnouncementService announcementService;

    private final RestUtils restUtils;

    @GetMapping
    public ResponseEntity<Object> getDonation(){
        return ResponseEntity.ok().build();
    }


    /**
     * Deletes a donation by its id.
     * @param donationId The id of the donation to delete.
     * @return An HTTP code to indicate if the operation succeed.
     */
    @Secured(Role.USER)
    @DeleteMapping
    public ResponseEntity<StatusResource> deleteDonation(@PathVariable Long donationId){
        if(announcementRepository.existsById(donationId)) {
            announcementRepository.deleteById(donationId);
            return restUtils.statusToResponse(new StatusResource(HttpStatus.OK, "Votre donation a été supprimée !"));
        }
        return restUtils.statusToResponse(new StatusResource(HttpStatus.NOT_FOUND, "La donation n'existe pas !"));
    }
}
