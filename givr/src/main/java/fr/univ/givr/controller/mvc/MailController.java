package fr.univ.givr.controller.mvc;

import fr.univ.givr.service.AuthenticateService;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.service.register.RegisterService;
import fr.univ.givr.utils.MVCUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MVC controller to get the mail page.
 */
@AllArgsConstructor
@Controller
@RequestMapping("/mail")
@Slf4j
public class MailController {

    /**
     * Utils to add model properties.
     */
    private final MVCUtils mvcUtils;

    /**
     * Register service to confirm account.
     */
    private final RegisterService registerService;

    /**
     * Service to manage authentication.
     */
    private final AuthenticateService service;

    /**
     * Service to interact with account data.
     */
    private final AccountService accountService;

    /**
     * Load the mail jsp page.
     *
     * @param model Model MVC.
     * @param user  Account of the user.
     * @return The name of the JSP.
     */
    @GetMapping("/new-confirm")
    public String getPageToGetNewMail(
            HttpServletRequest request,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        mvcUtils.insertAllInfo(model, user, request);
        return "send-new-mail";
    }

    /**
     * Load the confirm mail page.
     * @param model Model MVC.
     * @param user  Account of the user.
     * @param token Token to confirm mail.
     * @return The name of the JSP.
     */
    @GetMapping("/confirm")
    public String getPageToConfirmMail(
            HttpServletRequest request,
            Model model,
            @AuthenticationPrincipal User user,
            @RequestParam("id") String token
    ) {
        registerService.confirmAccountByToken(token);
        model.addAttribute("message", "Votre compte est maintenant activé");

        mvcUtils.insertAllInfo(model, user, request);
        return "confirm_mail";
    }

    /**
     * Load the confirm change mail page.
     * If the mail is changed, remove the current auth token.
     * @param model Model MVC.
     * @param user  Account of the user.
     * @param token Token to change mail.
     * @param response http response.
     * @return The name of the JSP.
     */
    @GetMapping("/modify")
    public Object getPageToModifyMailConfirm(
            Model model,
            @AuthenticationPrincipal User user,
            @RequestParam("id") String token,
            HttpServletResponse response
    ) {
        if(user == null) {
            return mvcUtils.redirectToLoginPageWithUnauthorizedMessage(model);
        }

        String email = user.getUsername();
        accountService.setNewEmailFromToken(email, token);
        service.removeCookieAuth(response);
        model.addAttribute("message", "Votre mail a été modifié");

        return "confirm_mail";
    }
}
