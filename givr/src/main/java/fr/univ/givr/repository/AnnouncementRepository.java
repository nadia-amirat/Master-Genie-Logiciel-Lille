package fr.univ.givr.repository;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.announcement.Announcement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to manage announcement data.
 */
@Repository
public interface AnnouncementRepository extends CrudRepository<Announcement, Long> {

    /**
     * Find an announcement from its title.
     *
     * @param title Announcement's title.
     * @return The instance of the announcement, {@code null} otherwise.
     */
    Announcement findByTitle(String title);
    boolean existsById(Long annoucementId);
    void deleteById(Long annoucementId);

    Announcement findById(long id);

    @Query("SELECT a FROM Announcement a WHERE a.title LIKE %?1%"
            + " OR a.description LIKE %?1%")
    Page<Announcement> search(String keyword, Pageable page);
    Page<Announcement> findAll(Pageable page);

    List<Announcement> findFirst10ByOrderByIdDesc();

    public List<Announcement> findByAccount(Account account);

}
