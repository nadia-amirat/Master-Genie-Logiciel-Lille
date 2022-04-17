package fr.univ.givr.controller.mvc;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * MVC controller to get the register page.
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    /**
     * Load the register jsp page.
     * If the user is not connected, redirect to the index page.
     *
     * @param user Account of the user.
     * @return The name of the JSP or the redirection to the login page.
     */
    @GetMapping
    public Object getRegisterPage(@AuthenticationPrincipal User user) {
        if (user != null) {
            return new RedirectView("/");
        }
        return "register";
    }
}
