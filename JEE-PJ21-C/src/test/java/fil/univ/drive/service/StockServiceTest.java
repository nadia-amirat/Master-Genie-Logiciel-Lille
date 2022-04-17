package fil.univ.drive.service;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.exception.IllegalResourceException;
import fil.univ.drive.exception.NegativeQuantityException;
import fil.univ.drive.exception.ResourceNotFoundException;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.stock.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class StockServiceTest {
	@Autowired StockRepository<Stock<? extends Article>> stockRepository;
	@Autowired ArticleRepository<Article> articleRepository;
	@Autowired StockService stockService;
	@Autowired CustomerOrderRepository customerOrderRepository;


    @BeforeEach
	public void setUp() {
		customerOrderRepository.deleteAll();
		stockRepository.deleteAll();
		articleRepository.deleteAll();
	}

	@Test
	@DisplayName("get all stocks returns a list of all the stocks")
	public void getAllStocksReturnsAListOfAllTheStocks() {
		var article1 =  new NonPerishableArticle("npArticle", 0L);
		var article2 = new PerishableArticle("pArticle", 0L);
		articleRepository.saveAll(List.of(article1, article2));
		var stocks = List.of(
				new NonPerishableStock(10L, article1),
				new PerishableStock(10L, article2, LocalDate.now())
		);
		stockRepository.saveAll(stocks);
		then(stockRepository.findAll()).hasSameElementsAs(stocks);

		List<Stock<? extends Article>> res = stockService.getAllStocks();

		then(stocks).hasSameElementsAs(res);
	}

	@Test
	@DisplayName("cannot delete a stock that does not exist")
	public void cannotDeleteAStockThatDoesNotExist() {
		Throwable thrown = catchThrowable(() -> stockService.deleteStock(0L));

		then(thrown).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	@DisplayName("can delete a stock that exists")
	public void canDeleteAStockThatExists() {
		NonPerishableArticle article = anyNonPerishableArticle();
		articleRepository.save(article);
		NonPerishableStock stock = new NonPerishableStock(10L, article);
	    stockRepository.save(stock);
		assert stock.getStockId() != null;
		assert stockRepository.existsById(stock.getStockId());

		stockService.deleteStock(stock.getStockId());

		then(stockRepository.existsById(stock.getStockId())).isFalse();
	}

	@Test
	@DisplayName("adjusting the stock adjusts the quantity of articles in it")
	public void adjustingTheStockAdjustsTheQuantityOfArticlesInIt() {
		long initialQuantity = 10L;
		NonPerishableArticle article = anyNonPerishableArticle();
		articleRepository.save(article);
		NonPerishableStock stock = new NonPerishableStock(initialQuantity, article);
		stockRepository.save(stock);
		long movedQuantity = 5L;

		stockService.adjustStock(movedQuantity, stock.getStockId());

		var newStock = stockRepository.findById(stock.getStockId()).orElseThrow();
		then(newStock.getQuantity()).isEqualTo(movedQuantity);
	}

	@Test
	@DisplayName("cannot adjust the stock to a negative quantity of articles")
	public void cannotAdjustTheStockToANegativeQuantityOfArticles() {
		long initialQuantity = 10L;
		NonPerishableArticle article = anyNonPerishableArticle();
		articleRepository.save(article);
		NonPerishableStock stock = new NonPerishableStock(initialQuantity, article);
		stockRepository.save(stock);
		long movedQuantity = -15L;

		Throwable thrown = catchThrowable(() -> stockService.adjustStock(movedQuantity, stock.getStockId()));

		then(thrown).isInstanceOf(NegativeQuantityException.class);
	}

	private NonPerishableArticle anyNonPerishableArticle() {
		return new NonPerishableArticle("anyNonPerishableArticle", 0L);
	}

	@Test
	@DisplayName("cannot adjust a stock that does not exist")
	public void cannotAdjustAStockThatDoesNotExist() {
		Throwable thrown = catchThrowable(() -> stockService.adjustStock(0L, 0L));

		then(thrown).isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("not found");
	}

	@Nested
	@DisplayName("when creating stocks")
	public class WhenCreatingStocks {
		@Test
		@DisplayName("can add a non perishable stock of an article")
		public void canAddANonPerishableStockOfAnArticle() {
			var article = anyNonPerishableArticle();
			articleRepository.save(article);
			long quantity = 10L;

			stockService.createStockOfArticle(quantity, article.getId());

			var res = stockRepository.findByArticleId(article.getId());
			then(res).isInstanceOf(NonPerishableStock.class);
			NonPerishableStock nonPerishableStock = (NonPerishableStock) res;
			then(nonPerishableStock.getArticle()).isEqualTo(article);
			then(nonPerishableStock.getQuantity()).isEqualTo(quantity);
		}

		@Test
		@DisplayName("cannot add a non perishable stock of a perishable article")
		public void cannotAddANonPerishableStockOfAPerishableArticle() {
			var article = new PerishableArticle("Milk", 1L);
			articleRepository.save(article);
			long quantity = 10L;

			Throwable thrown = catchThrowable(() -> stockService.createStockOfArticle(quantity, article.getId()));

			then(thrown).isInstanceOf(IllegalResourceException.class);
		}

		@Test
		@DisplayName("cannot add a non perishable stock of an unexisting article")
		public void cannotAddANonPerishableStockOfAnUnexistingArticle() {
			Throwable thrown = catchThrowable(() -> stockService.createStockOfArticle(0L, 1L));

			then(thrown).isInstanceOf(ResourceNotFoundException.class);
		}

		@Test
		@DisplayName("cannot create a stock with a negative quantity")
		public void cannotCreateAStockWithANegativeQuantity() {
			var article = new NonPerishableArticle("Milk", 1L);
			articleRepository.save(article);
			long quantity = -10L;

			Throwable thrown = catchThrowable(() -> stockService.createStockOfArticle(quantity, article.getId()));

			then(thrown).isInstanceOf(NegativeQuantityException.class);
		}

	}
}
