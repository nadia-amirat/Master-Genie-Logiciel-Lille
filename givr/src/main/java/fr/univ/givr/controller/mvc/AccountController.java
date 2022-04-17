package fr.univ.givr.controller.mvc;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.utils.MVCUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * MVC controller to get the account page.
 */
@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    /**
     * Service to retrieve account.
     */
    private final AccountService accountService;

    /**
     * Utils to build JSP page.
     */
    private final MVCUtils mvcUtils;

    /**
     * Load the account info jsp page.
     * If the user is not connected, redirect to the login page.
     *
     * @param user Account of the user.
     * @return The name of the JSP or the redirection to the login page.
     */
    @GetMapping
    public Object getInfoAccountPage(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            return mvcUtils.redirectToLoginPageWithUnauthorizedMessage(model);
        }

        Account account = accountService.getByEmailOrThrow(user.getUsername());
        model.addAttribute("email", account.getEmail());
        model.addAttribute("firstname", account.getFirstname());
        model.addAttribute("lastname", account.getLastname());
        model.addAttribute("address", account.getAddress());

        mvcUtils.insertAllInfo(model, user, request);
        return "account";
    }
}
