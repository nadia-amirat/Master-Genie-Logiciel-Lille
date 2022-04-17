package fr.univ.givr.controller.mvc;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.service.account.AccountService;
import fr.univ.givr.service.announcement.AnnouncementService;
import fr.univ.givr.utils.MVCUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/donations")
public class DonationViewController {

    /**
     * Utils to manage model for the JSP.
     */
    private final MVCUtils mvcUtils;

    private final AnnouncementService announcementService;

    private final AccountService accountService;

    @GetMapping
    public String donations(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal User user
    ) {
        mvcUtils.insertAllInfo(model, user, request);
        Page<Announcement> announcementList = announcementService.searchAnnouncement(search, page);
        model.addAttribute("listAnnouncement", announcementList);
        model.addAttribute("search", search);
        return "donations";
    }

    @GetMapping("/{id}")
    public String announcementDetails(
            HttpServletRequest request,
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        mvcUtils.insertAllInfo(model, user, request);
        model.addAttribute("details", announcementService.findByIdOrNull(id));
        return "announcement/announcement_details";
    }

    @GetMapping("/create")
    public String createAnnouncement(
            HttpServletRequest request,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        mvcUtils.insertAllInfo(model, user, request);
        if (user == null) {
            return mvcUtils.redirectToLoginPageWithUnauthorizedMessage(model);
        }

        model.addAttribute("announcement", new Announcement());
        return "announcement/create_announcement";
    }

    /**
     * Load the Edit page.
     *
     * @param id      Announcement Id
     * @param request HTTP request.
     * @param model   Model to add information to build JSP.
     * @param user    Spring user from the authentication.
     * @return The name of the claim jsp.
     */
    @GetMapping("/edit/{id}")
    public String editAnnouncementView(@PathVariable long id,Model model,
     HttpServletRequest request, @AuthenticationPrincipal User user)
    {
        mvcUtils.insertAllInfo(model, user, request);
        if (user == null) {
            return mvcUtils.redirectToLoginPageWithUnauthorizedMessage(model);
        }

        Announcement announcement = announcementService.findById(id);
        if(announcement==null || announcement.getAccount() == null || announcement.getAccount().getEmail()!=user.getUsername()){
            return "error";
        }
        model.addAttribute("announcement",announcement);
        return "announcement/Edit_announcement";
    }

    @GetMapping("/mydonations")
    public String editAnnouncementView(Model model,
                                       HttpServletRequest request, @AuthenticationPrincipal User user)
    {
        if (user == null) {
            return mvcUtils.redirectToLoginPageWithUnauthorizedMessage(model);
        }
        String email = user.getUsername();
        Account account = accountService.getByEmailOrThrow(email);
        List<Announcement> announcementList = announcementService.findByAccount(account);
        model.addAttribute("listAnnouncement",announcementList);
        mvcUtils.insertAllInfo(model, user, request);
        return "announcement/mydonations";
    }

}
