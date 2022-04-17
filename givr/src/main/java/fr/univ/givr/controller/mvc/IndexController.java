package fr.univ.givr.controller.mvc;

import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.service.announcement.AnnouncementService;
import fr.univ.givr.utils.MVCUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * MVC controller for the index page.
 */
@AllArgsConstructor
@Controller
@RequestMapping
public class IndexController {

    /**
     * Utils to manage model for the JSP.
     */
    private final MVCUtils mvcUtils;

    private final AnnouncementService announcementService;

    /**
     * Load the index page.
     *
     * @param request HTTP request.
     * @param model   Model to add information to build JSP.
     * @param user    Spring user from the authentication.
     * @return The name of the claim jsp.
     */
    @GetMapping
    public String getIndex(
            HttpServletRequest request,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        mvcUtils.insertAllInfo(model, user, request);
        List<Announcement> tenLast = announcementService.getTenLastAnnoucement();
        model.addAttribute("ten_last_donations",tenLast);
        return "index";
    }
}
