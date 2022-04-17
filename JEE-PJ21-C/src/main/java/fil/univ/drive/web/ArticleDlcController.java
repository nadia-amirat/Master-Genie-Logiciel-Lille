package fil.univ.drive.web;

import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.repository.stock.PerishableStockRepository;
import fil.univ.drive.service.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/stock")
public class ArticleDlcController {
    public static final String PERISHABLES_ATTRIBUTE = "perishables";
    @Autowired
    private PerishableStockRepository perishableStockRepository;

    /**
     * Returns a list of expired articles
     *
     * @param model the model of the page
     * @return a list of expired articles
     */
    @GetMapping("/expired")
    public String getExpiredArticles(Model model, HttpServletRequest request) {
        log.info("Getting expired articles");
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        List<PerishableStock> articlesPerishables = perishableStockRepository.findAllByExpiryDateBefore(
                LocalDate.now());
        model.addAttribute(PERISHABLES_ATTRIBUTE, articlesPerishables);
        return "produit_DLC";
    }
}
