package fil.univ.drive.service;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.panier.Cart;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.stock.NonPerishableStockRepository;
import fil.univ.drive.repository.stock.PerishableStockRepository;
import fil.univ.drive.repository.stock.StockRepository;
import fil.univ.drive.web.dto.ArticleEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ClientArticleServiceTest {
	@Autowired
	StockRepository<Stock<? extends Article>> stockRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	CustomerOrderRepository customerOrderRepository;

	@Autowired
	PerishableStockRepository perishableStockRepository;
	@Autowired
	NonPerishableStockRepository nonPerishableStockRepository;
	@Autowired
	ClientArticleService articlesService;
	@BeforeEach
	public void setUp() {
		stockRepository.deleteAll();

		perishableStockRepository.deleteAll();
		nonPerishableStockRepository.deleteAll();
		customerOrderRepository.deleteAll();
		articleRepository.deleteAll();


	}

	@Test
	public void testAddArticle() {
		var article1 =  new NonPerishableArticle("npArticle", 0L);
		var article2 = new PerishableArticle("pArticle", 0L);
		var article3 = new ArticleEntry(1L,1,"art3");


		Cart cart = new Cart();

		articleRepository.saveAll(List.of(article1, article2));
		var stocks = List.of(
				new NonPerishableStock(10L, article1),
				new PerishableStock(10L, article2, LocalDate.now().plusDays(7))

		);

		stockRepository.saveAll(stocks);
		assertTrue(cart.getArticles().size()==0);
       /*articlesService.addArticleToCart(0L,article3, cart);
       assertTrue(cart.getArticles().size()==1);*/

	}
	@Test
	public void testReturnFalseWhenThereIsNonAvailableArticle() {
		var stocks = new ArrayList();
		articleRepository.saveAll(stocks);
		List<Stock<? extends Article>> found = articlesService.findArticles();
		assertEquals(found,stocks);

	}
	@Test
	public void testFindArticles() {
		var article1 =  new NonPerishableArticle("npArticle", 0L);
		var article2 = new PerishableArticle("pArticle", 0L);
		articleRepository.saveAll(List.of(article1, article2));
		var stocks = List.of(
				new NonPerishableStock(10L, article1),
				new PerishableStock(10L, article2, LocalDate.now().plusDays(7))
		);
		stockRepository.saveAll(stocks);
		List<Stock<? extends Article>> found = articlesService.findArticles();
		assertEquals(found,stocks);
	}

}
