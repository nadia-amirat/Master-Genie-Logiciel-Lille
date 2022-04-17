package fil.univ.drive.web;
import fil.univ.drive.APIClient;
import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.PerishableArticleRepository;
import fil.univ.drive.repository.stock.PerishableStockRepository;
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
class ProduitDlcControllerTest {

    @Autowired APIClient apiClient;
    @Autowired PerishableStockRepository perishableStockRepository;
    @Autowired PerishableArticleRepository perishableArticleRepository;
    @Autowired CustomerOrderRepository customerOrderRepository;

    @BeforeEach
    public void setUp() {
        customerOrderRepository.deleteAll();
        perishableStockRepository.deleteAll();
        perishableArticleRepository.deleteAll();
    }

    @Test
    @DisplayName("get produit dlc adds all perishable products to the model")
    public void getProduitDlcAddsAllPerishableProductsToTheModel() throws Exception {
        // Given
        List<PerishableArticle> articles = List.of(
            new PerishableArticle("Lait", 1L),
            new PerishableArticle("Raviolis frais", 2L),
            new PerishableArticle("Tomates", 3L)
        );
        perishableArticleRepository.saveAll(articles);
        List<PerishableStock> nonExpiredStocks = List.of(
            new PerishableStock(5L, articles.get(0), LocalDate.now().plusDays(10)),
            new PerishableStock(7L, articles.get(0), LocalDate.now().plusDays(1)),
            new PerishableStock(5L, articles.get(1), LocalDate.now())
        );
        List<PerishableStock> expiredStocks = List.of(
            new PerishableStock(11L, articles.get(1), LocalDate.now().minusDays(10)),
            new PerishableStock(12L, articles.get(1), LocalDate.now().minusDays(20)),
            new PerishableStock(13L, articles.get(2), LocalDate.now().minusDays(1))
        );
        perishableStockRepository.saveAll(nonExpiredStocks);
        perishableStockRepository.saveAll(expiredStocks);

        // When
        MvcResult result = apiClient.getExpiredArticles();

        // Then
        Object actualExpiredStocks = result.getRequest().getAttribute(ArticleDlcController.PERISHABLES_ATTRIBUTE);
        then(actualExpiredStocks).isNotNull().isInstanceOf(List.class);
        List<PerishableStock> castExpiredStocks = (List<PerishableStock>) actualExpiredStocks;
        then(castExpiredStocks).hasSameElementsAs(expiredStocks);
    }
}
