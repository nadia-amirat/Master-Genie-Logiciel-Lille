package fr.univ.givr.controller.mvc;

import fr.univ.givr.utils.MVCUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * MVC controller to get the login page.
 */
@AllArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    private final MVCUtils mvcUtils;

    /**
     * Load the login jsp page.
     * If the user is already authenticated, redirect it on the index page.
     *
     * @param model Model MVC.
     * @param user  Account of the user.
     * @return The name of the JSP.
     */
    @GetMapping
    public Object getLoginPage(
            HttpServletRequest request,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            return new RedirectView("/");
        }
        mvcUtils.insertPageInfo(model, request);
        return "login";
    }
}
