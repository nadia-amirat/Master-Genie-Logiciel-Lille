package fr.univ.givr.configuration;

import fr.univ.givr.model.role.Role;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Pair;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration for roles.
 */
public class RoleConfiguration {

    /**
     * Build the role hierarchy about the roles.
     *
     * @return Instance of role hierarchy.
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        List<Pair<String, String>> roles = List.of(
                Pair.of(Role.ADMIN, Role.USER)
        );

        roleHierarchy.setHierarchy(buildHierarchyPresentation(roles));
        return roleHierarchy;
    }

    /**
     * Transform a list of roles to the hierarchy presentation.
     *
     * @param roles Set of roles.
     * @return Hierarchy presentation of roles.
     */
    private String buildHierarchyPresentation(@NonNull Collection<Pair<String, String>> roles) {
        return roles.stream().map(rls -> rls.getFirst() + " > " + rls.getSecond())
                .collect(Collectors.joining(" \n "));
    }
}
