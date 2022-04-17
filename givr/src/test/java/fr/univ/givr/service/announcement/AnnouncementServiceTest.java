package fr.univ.givr.service.announcement;

import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.repository.AnnouncementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AnnouncementServiceTest {

    @Autowired
    private AnnouncementService announcementService;
    Announcement a,b,c;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @BeforeEach
    void setUp() {
        announcementRepository.deleteAll();
        a = new Announcement();
        a.setTitle("Boisson énergétique");
        a.setDescription("une boisson energisante");
//        a.setImage("https://static1.chronodrive.com/img/PM/P/0/76/0P_61276.gif");
        announcementRepository.save(a);
        b = new Announcement();
        b.setTitle("Papier Cadeau");
        b.setDescription("cadeau papier");
//        b.setImage("https://static1.chronodrive.com/img/PM/P/0/72/0P_348972.gif");
        announcementRepository.save(b);

        c = new Announcement();
        c.setTitle("Pur jus d'orange");
//        c.setImage("https://static1.chronodrive.com/img/PM/P/0/42/0P_40042.gif");
        c.setDescription("jus d'orange");
        announcementRepository.save(c);
    }
    @Test
    public void searchBoissonTest(){
        assertEquals(announcementService.searchAnnouncement("Boisson",0).getContent().size(),1);
    }

    @Test
    public void searchPapierTest(){
        assertEquals(announcementService.searchAnnouncement("Papier",0).getContent().size(),1);
    }

    @Test
    public void searchJusTest(){
        assertEquals(announcementService.searchAnnouncement("jus",0).getContent().size(),1);
        assertEquals(announcementService.searchAnnouncement("orange",0).getContent().size(),1);
    }
    @Test
    public void searchWithoutKeyWord(){
        assertEquals(announcementService.searchAnnouncement("",0).getContent().size(),3);
    }

    @Test
    public void searchWithKeyWord(){
        assertEquals(announcementService.searchAnnouncement("ca",0).getContent().size(),1);
        assertEquals(announcementService.searchAnnouncement("b",0).getContent().size(),1);
        assertEquals(announcementService.searchAnnouncement("a",0).getContent().size(),3);
    }


}

