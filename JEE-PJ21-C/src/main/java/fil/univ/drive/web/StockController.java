package fil.univ.drive.web;

import fil.univ.drive.domain.CustomerOrder;
import fil.univ.drive.domain.OrderEntry;
import fil.univ.drive.domain.article.*;
import fil.univ.drive.domain.article.*;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.exception.IllegalResourceException;
import fil.univ.drive.exception.NegativeQuantityException;
import fil.univ.drive.exception.ResourceNotFoundException;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.article.CategoryRepository;
import fil.univ.drive.repository.article.CategoryRepository;
import fil.univ.drive.repository.article.ProductRepository;
import fil.univ.drive.repository.stock.StockRepository;
import fil.univ.drive.service.StockService;
import fil.univ.drive.service.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/stock")
@Slf4j
public class StockController {

    public static final String INVALID_STOCK_QUANTITY = "invalidStockQuantity";
    public static final String ARTICLE_NOT_FOUND = "articleNotFound";
    public static final String INVALID_ARTICLE = "invalidArticle";
    public static final String ERROR_MESSAGE = "errorMessage";

    @Autowired StockRepository<Stock<? extends Article>> stockRepository;
    @Autowired private ArticleRepository<Article> articleRepository;
    @Autowired private CategoryRepository<Category> categoryRepository;
    @Autowired private CustomerOrderRepository customerOrderRepository;
    @Autowired private StockService stockService;

    @GetMapping
    public String manageStocks(Model model, HttpServletRequest request) {
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        model.addAttribute("allStocks", stockService.getAllStocks());
        return "manage_stocks";
    }

    @PostMapping({"/{stockId}/delete"})
    public String deleteStock(Model model, @PathVariable Long stockId, HttpServletRequest request) {
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        stockService.deleteStock(stockId);
        return manageStocks(model, request);
    }

    @PostMapping("/{stockId}/quantity")
    public String changeQuantity(Model model, @PathVariable Long stockId, @RequestParam Long quantity,
                                 HttpServletRequest request) {
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        try {
            stockService.adjustStock(quantity, stockId);
        } catch (NegativeQuantityException e) {
            log.warn(e.getMessage());
            model.addAttribute(INVALID_STOCK_QUANTITY, true);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
        }
        return manageStocks(model, request);
    }

    @PostMapping
    public String addStock(Model model, @RequestParam Long articleId, @RequestParam Long quantity,
                           HttpServletResponse servletResponse, HttpServletRequest request) {
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        try {
            stockService.createStockOfArticle(quantity, articleId);
        } catch (ResourceNotFoundException e) {
            log.warn(e.getMessage());
            model.addAttribute(ARTICLE_NOT_FOUND, true);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            servletResponse.setStatus(404);
        } catch (NegativeQuantityException e) {
            log.warn(e.getMessage());
            model.addAttribute(INVALID_STOCK_QUANTITY, true);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            servletResponse.setStatus(400);
        } catch (IllegalResourceException e) {
            log.warn(e.getMessage());
            model.addAttribute(INVALID_ARTICLE, true);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            servletResponse.setStatus(400);
        }
        return manageStocks(model, request);
    }

    @PostConstruct
    public void init_db() {
        log.debug("Initializing database");
        List<Article> articles = List.of(
                new NonPerishableArticle("Stylo", 0L),
                new NonPerishableArticle("Règle", 0L),
                new PerishableArticle("Lait", 1L),
                new PerishableArticle("Raviolis frais", 2L),
                new PerishableArticle("Tomates", 3L),
                new PerishableArticle("Boisson gazeuse", 2L),
                new PerishableArticle("Bonbons", 2L),

                new NonPerishableArticle("Stylo bleu", 100L), //7
                new NonPerishableArticle("Cahiers", 300L),  //8
                new NonPerishableArticle("Sac", 200L), //9

                new PerishableArticle("Thon", 2L), //10
                new PerishableArticle("Champignons", 2L) //11


        );
        List<Stock<? extends Article>> stocks = List.of(
                new NonPerishableStock(40L, (NonPerishableArticle) articles.get(0)),
                new NonPerishableStock(0L, (NonPerishableArticle) articles.get(1)),
                new NonPerishableStock(100L, (NonPerishableArticle) articles.get(7)),
                new NonPerishableStock(300L, (NonPerishableArticle) articles.get(8)),
                new NonPerishableStock(90L, (NonPerishableArticle) articles.get(9)),

                new PerishableStock(10L, (PerishableArticle) articles.get(2), LocalDate.now().minusDays(30)),
                new PerishableStock(42L, (PerishableArticle) articles.get(3), LocalDate.now().minusDays(5)),
                new PerishableStock(42L, (PerishableArticle) articles.get(4), LocalDate.now().minusDays(5)),
                new PerishableStock(20L, (PerishableArticle) articles.get(5), LocalDate.now().minusDays(5)),
                new PerishableStock(42L, (PerishableArticle) articles.get(6), LocalDate.now().plusDays(15)),
                new PerishableStock(42L, (PerishableArticle) articles.get(6), LocalDate.now().plusDays(30)),

                new PerishableStock(200L, (PerishableArticle) articles.get(10), LocalDate.now().plusDays(5)),
                new PerishableStock(80L, (PerishableArticle) articles.get(11), LocalDate.now().plusDays(10))

                // Pas de stocks pour l' article 4
        );


        List<Category> categorie1 = List.of(
                new Category("fruits"),
                new Category("boisson"),
                new Category("maison"),
                new Category("legumes"),
				new Category("viandes")
        );


        List<Article> product = List.of(
                new Product("jus", 0L, 3.4, categorie1.get(1)),
                new Product("pomme de terre", 0L, 2.4, categorie1.get(3)),
                new Product("soda", 0L, 1.4, categorie1.get(1)),
                new Product("carotte", 0L, 10, categorie1.get(3)),
                new Product("lait", 0L, 14, categorie1.get(2)),
                new Product("banane", 0L, 14, categorie1.get(0))
        );

        List<CustomerOrder> orders = List.of(
                new CustomerOrder(List.of(
                        OrderEntry.of(articles.get(0), 5),
                        OrderEntry.of(articles.get(1), 3),
                        OrderEntry.of(articles.get(2), 1)
                ), LocalDate.now().minusDays(10)),
                new CustomerOrder(List.of(
                        OrderEntry.of(articles.get(0), 3),
                        OrderEntry.of(articles.get(4), 6),
                        OrderEntry.of(articles.get(5), 1)
                ), LocalDate.now().minusDays(5))
        );


        articleRepository.saveAll(articles);
        stockRepository.saveAll(stocks);

        categoryRepository.saveAll(categorie1);
        articleRepository.saveAll(product);
        customerOrderRepository.saveAll(orders);
    }

}
