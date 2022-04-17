package fr.univ.givr.repository;

import fr.univ.givr.model.account.Account;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository to manage account data.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Change the verified status for an account.
     * @param email Account's email.
     * @param verified New status.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.verified = :verified WHERE a.email = :email")
    void setVerified(@Param("email") @NonNull String email, @Param("verified") boolean verified);

    /**
     * Find an account from an email.
     * @param email Account's email.
     * @return The instance of the account, {@code null} otherwise.
     */
    Account findByEmail(String email);

    /**
     * Know if an account exists with the email.
     * @param email Email to find an account.
     * @return {@code true} if an account exists, {@code false} otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Change the ban status for an account.
     * @param email Account's email.
     * @param banned New status.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.banned = :banned WHERE a.email = :email")
    void setBanned(String email, boolean banned);
}
