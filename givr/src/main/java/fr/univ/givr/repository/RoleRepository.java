package fr.univ.givr.repository;

import fr.univ.givr.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage the permission.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
