package fr.univ.givr.service.announcement;

import fr.univ.givr.exception.donations.DonationNotFoundException;
import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.repository.AnnouncementRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public Announcement findById(Long id) {
        return announcementRepository.findById(id).orElseThrow(DonationNotFoundException::new);
    }

    public Announcement findByIdOrNull(Long id){
        return announcementRepository.findById(id).orElse(null);
    }

    public Announcement save(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    public Page<Announcement> searchAnnouncement(String search, int page){
        Pageable numPage = PageRequest.of(page, 12);
        if (search != null){
            return announcementRepository.search(search,numPage);
        }
        return announcementRepository.findAll(numPage);
    }

    public List<Announcement> getTenLastAnnoucement (){
        return announcementRepository.findFirst10ByOrderByIdDesc();
    }
    
    public List<Announcement> findByAccount(Account account){
        return announcementRepository.findByAccount(account);
    }

}
