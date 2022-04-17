package fr.univ.givr.configuration;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.model.announcement.DonationImage;
import fr.univ.givr.model.role.Role;
import fr.univ.givr.service.account.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Configuration to load database components.
 */
@AllArgsConstructor
@Configuration
@EnableJpaRepositories(basePackages = "fr.univ.givr.repository")
public class DatabaseConfiguration {

    private final AccountService accountService;

    @PostConstruct
    public void initDatabase() {
        // LinkedHashSet to keep the order of donations:
        Set<Announcement> donations = new LinkedHashSet<>();
        // Adds ten fake donations
        for(int i = 0; i < 5; i++){
            donations.add(this.addFirstFakeDonation());
            donations.add(this.addSecondFakeDonation());
            donations.add(this.addThirdFakeDonation());
        }

        accountService.createAccount(Account.builder()
                                            .address("c")
                                            .email("alt.xo-761ydn4@yopmail.com")
                                            .firstname("a")
                                            .password("Azertyuiop1")
                                            .lastname("b")
                                            .verified(true)
                                            .roles(Set.of(Role.builder().permission(Role.ADMIN).build()))
                                            .announcements(donations)
                                            .build());

    }

    private Announcement addFirstFakeDonation(){
        Announcement fakeDonation = new Announcement();
        fakeDonation.setTitle("Vélo BMX noir");
        try {
            ClassPathResource resource = new ClassPathResource("/images_for_fake_donations/fake_1.jpg");

            DonationImage image = new DonationImage();
            image.setData(resource.getInputStream().readAllBytes());
            fakeDonation.setImages(List.of(image));
        }
        catch (IOException e) {
            // No image
        }
        fakeDonation.setDescription("Ayant récemment emménagé dans un appartement et n'ayant plus le temps de faire du vélo, je souhaiter donner mon BMX.\nLes pneus sont neufs, toujours entretenu, il sera peut être nécessaire de graisser la chaîne.");

        return fakeDonation;
    }

    private Announcement addSecondFakeDonation(){
        Announcement fakeDonation = new Announcement();
        fakeDonation.setTitle("Papier cadeau");
        try {
            ClassPathResource resource = new ClassPathResource("/images_for_fake_donations/fake_2.jpg");

            DonationImage image = new DonationImage();
            image.setData(resource.getInputStream().readAllBytes());
            fakeDonation.setImages(List.of(image));
        }
        catch (IOException e) {
            // No image
        }
        fakeDonation.setDescription("Donne emballage pour vos cadeaux de fin d'année");

        return fakeDonation;
    }

    private Announcement addThirdFakeDonation(){
        Announcement fakeDonation = new Announcement();
        fakeDonation.setTitle("Lot de vaisselle");
        try {
            ClassPathResource resource = new ClassPathResource("/images_for_fake_donations/fake_3.jpg");

            DonationImage image = new DonationImage();
            image.setData(resource.getInputStream().readAllBytes());
            fakeDonation.setImages(List.of(image));
        }
        catch (IOException e) {
            // No image
        }
        fakeDonation.setDescription("Donne vaisselle composée de divers élément, idéal pour un couple d'étudiant ou colocataires.");

        return fakeDonation;
    }

}
