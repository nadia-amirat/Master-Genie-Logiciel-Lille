package fr.univ.givr.controller;

import fr.univ.givr.AbstractIT;
import org.junit.jupiter.api.Test;

public class IndexIT extends AbstractIT {

    @Test
    void getIndexTest(){
        getWebTestClient()
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
