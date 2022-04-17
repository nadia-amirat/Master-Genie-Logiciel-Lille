package fil.univ.drive.steps;

import fil.univ.drive.APIClient;
import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.stock.StockRepository;
import fil.univ.drive.service.StockService;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class StockHandlingSteps implements En {
	@Autowired StockRepository<Stock<? extends Article>> stockRepository;
	@Autowired ArticleRepository<Article> articleRepository;
	@Autowired StockService stockService;
	@Autowired APIClient api;

	MvcResult result;


	public StockHandlingSteps() {
		Given("there are no articles nor stocks", () -> {
			stockRepository.deleteAll();
			articleRepository.deleteAll();
		});
		Given("non perishable article {string} exists", (String articleName) -> {
			articleRepository.save(new NonPerishableArticle(articleName, 0L));
		});
		Given("perishable article {string} exists", (String articleName) -> {
			articleRepository.save(new PerishableArticle(articleName, 0L));
		});
		When("Admin adds a new stock with of {int} articles {string}", (Integer quantity, String articleName) -> {
			Article article = articleRepository.findByName(articleName);
			Long id;
			if (article != null) {
				id = article.getId();
			} else {
				id = 0L;
			}
			result = api.addStock(id, quantity.longValue());
		});
		Then("A stock of {int} articles {string} is created", (Integer quantity, String articleName) -> {
			Stock<? extends Article> stock = stockRepository.findByArticleId(
					articleRepository.findByName(articleName).getId());
			then(stock.getQuantity()).isEqualTo(quantity.longValue());
			then(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		});
		Then("there is an error", () -> {
			then(result.getResponse().getStatus()).isGreaterThanOrEqualTo(400).isLessThan(500);
		});
		Given("no article exist", () -> {
		});
	}
}
