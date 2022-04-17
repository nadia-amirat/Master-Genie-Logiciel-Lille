package fil.univ.drive.web;


import fil.univ.drive.APIClient;
import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.stock.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Ce test fait va appeler le ProduitDlcController comme si on était dans le navigateur
 * <p>
 * On va nous même dire ce qu'il y a dans la base de données pour avoir un meilleur contrôle dessus.
 * <p>
 * Pour vérifier que le controller marche bien, on vérifie que l'attribut passé au modèle est correct
 */
@SuppressWarnings("unchecked")
@SpringBootTest
public class ArticlesControllerTest {

    @Autowired APIClient apiClient;
    @Autowired StockRepository<Stock<? extends Article>> stockRepository;
    @Autowired ArticleRepository articleRepository;
    @Autowired CustomerOrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        stockRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @Test
    @DisplayName("list of items available and have expiration date greater than 5 days")
    public void getProduitDlcAddsAllPerishableProductsToTheModel() throws Exception {
        // Given
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
            new PerishableStock(42L, (PerishableArticle) articles.get(6), LocalDate.now().minusDays(7)),
            new PerishableStock(42L, (PerishableArticle) articles.get(6), LocalDate.now().minusDays(7)),

            new PerishableStock(200L, (PerishableArticle) articles.get(10), LocalDate.now().plusDays(5)),
            new PerishableStock(80L, (PerishableArticle) articles.get(11), LocalDate.now().plusDays(10))

        );
        articleRepository.saveAll(articles);
        stockRepository.saveAll(stocks);

        // When
        MvcResult result = apiClient.getAvailableArticles();

        // Then
        Object actualAvailableArticles = result.getRequest().getAttribute(ClientArticleController.ARTICLES_ATTRIBUTE);
        then(actualAvailableArticles).isNotNull().isInstanceOf(List.class);
        List<Article> castAvailableArticles = (List<Article>) actualAvailableArticles;
        then(castAvailableArticles).hasSameElementsAs((Iterable<? extends Article>) actualAvailableArticles);
    }

}
