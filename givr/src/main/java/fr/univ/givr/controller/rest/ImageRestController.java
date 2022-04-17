package fr.univ.givr.controller.rest;

import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.model.announcement.DonationImage;
import fr.univ.givr.service.announcement.AnnouncementService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/images")
public class ImageRestController {

    private final AnnouncementService announcementService;

    /**
     * Retrieve an image for a donation.
     * @param donationId Donation id.
     * @param imageId Image id registered for the donation.
     * @return Not found if the donation or image is not found, the bytes of images otherwise.
     */
    // TODO set suffix link
    @GetMapping(value = "/{donationId}/{imageId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getDonationImage(
            @PathVariable("donationId") long donationId,
            @PathVariable("imageId") long imageId
    ) {
        // TODO use specific repository
        Announcement donation = announcementService.findById(donationId);
        if(donation == null) return ResponseEntity.notFound().build();

        for(DonationImage image : donation.getImages()) {
            if(image.getId() == imageId) {
                return ResponseEntity.ok(image.getData());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
