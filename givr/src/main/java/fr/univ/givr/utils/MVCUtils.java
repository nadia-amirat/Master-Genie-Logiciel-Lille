package fr.univ.givr.utils;

import fr.univ.givr.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * Utils class to load model properties.
 */
@AllArgsConstructor
@Component
public class MVCUtils {

    /**
     * Utils to check data about user.
     */
    private final RequestUtils requestUtils;

    /**
     * Insert several properties about the user into the model.
     * Properties :
     * <ul>
     *     <li>isAdmin</li>
     *     <li>isAuth</li>
     * </ul>
     *
     * @param model   Model MVC.
     * @param user    User authenticate, can be null.
     * @param request HTTP request.
     */
    public void insertAllInfo(@NonNull Model model, User user, @NonNull HttpServletRequest request) {
        if (user != null) {
            model.addAttribute("isAdmin", requestUtils.hasPermission(user, Role.ADMIN));
            String path = request.getServletPath();
            model.addAttribute("isMyDonationPage", isPathOfPages(path, "donations/mydonations"));
            model.addAttribute("isAuth", true);
        }
    }

    /**
     * Insert an error message in the model to advice the user that is not authorized to interact with the ressource and
     * create a redirection to the login page.
     *
     * @param model Model MVC.
     * @return A redirection to the login page.
     */
    public String redirectToLoginPageWithUnauthorizedMessage(@NonNull Model model) {
        model.addAttribute("error", "Veuillez-vous connecter afin d'interagir avec cette ressource");
        return "login";
    }

    /**
     * Add the location path information into the model to enable or not the navigation buttons.
     *
     * @param model   Model MVC.
     * @param request HTTP request.
     */
    public void insertPageInfo(@NonNull Model model, @NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        model.addAttribute("isLoginPage", isPathOfPages(path, "login"));

    }

    /**
     * Check if the path begin by the expected path prefixed by '/'.
     *
     * @param path       Current path of the request.
     * @param expectPath Expected path allows checking location.
     * @return {@code true} if the path follows the syntax of the expected path, {@code false} otherwise.
     */
    private boolean isPathOfPages(String path, String expectPath) {
        return path.startsWith('/' + expectPath);
    }

}
