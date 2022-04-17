package fil.univ.drive.web;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.panier.Cart;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.exception.NegativeQuantityException;
import fil.univ.drive.exception.ResourceNotFoundException;
import fil.univ.drive.service.ClientArticleService;
import fil.univ.drive.service.user.Role;
import fil.univ.drive.web.dto.ArticleEntry;
import fil.univ.drive.web.dto.SimpleResponse;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class ClientArticleController {
    public static final String ARTICLES_ATTRIBUTE = "articles";
    public static final String CART_ATTRIBUTE = "cart";

    @Autowired
    ClientArticleService articlesService;
    /**
     * Returns a list of items available, if perishable articles display the ones
     * which have expiration date greater than 5 days and quantity of each item.
     * @param model
     * @param req
     * @return list of available items
     */

    @GetMapping("/articles")
    public String getAllAvailableArticles(Model model, HttpServletResponse req, HttpServletRequest request) {
        ConnectionController.checkUserIsCorrectRole(request, Role.CLIENT);

        List<Stock<? extends Article>> res = articlesService.findArticles();
        model.addAttribute(ARTICLES_ATTRIBUTE, res);
        return "articles_dispo";
    }

    /**
     * Add an item to cart if it is available.
     * @param stockId
     * @param session
     * @param article
     * @return
     * @throws DataException
     */

    @ResponseBody
    @PostMapping(value = "/panier/{stockId}",consumes ="application/json")
    public SimpleResponse addArticleToCart(@PathVariable Long stockId, HttpSession session, @RequestBody ArticleEntry article, HttpServletRequest request) throws DataException {
        ConnectionController.checkUserIsCorrectRole(request, Role.CLIENT);

        SimpleResponse sim_res = new SimpleResponse();

        Cart cart = (Cart) session.getAttribute(CART_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_ATTRIBUTE, cart);
        }

        try {
            articlesService.addArticleToCart(stockId, article, cart);
        } catch (ResourceNotFoundException | NegativeQuantityException e) {
            sim_res.status = SimpleResponse.Status.ERROR;
        }

        sim_res.status = SimpleResponse.Status.OK;
        sim_res.message = "added correctly";

        return sim_res;
    }

    /**
     * Returns a list of ordered items (cart items).
     *
     * @param model
     * @param session
     * @return list of items which are in the cart.
     */
    @GetMapping("/panier/articles")
    public String getCartArticles(Model model, HttpSession session, HttpServletRequest request) {
        ConnectionController.checkUserIsCorrectRole(request, Role.CLIENT);

        Cart cart = (Cart) session.getAttribute("cart");
        if(cart == null){
            cart = new Cart();
            session.setAttribute(CART_ATTRIBUTE,cart);
        }
       model.addAttribute(ARTICLES_ATTRIBUTE,cart.getArticles());
        return "validate_panier";
    }

}
