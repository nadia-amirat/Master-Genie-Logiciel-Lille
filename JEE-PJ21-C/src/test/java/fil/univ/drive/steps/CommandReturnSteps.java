package fil.univ.drive.steps;

import fil.univ.drive.APIClient;
import fil.univ.drive.domain.CustomerOrder;
import fil.univ.drive.domain.OrderEntry;
import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.stock.StockRepository;
import io.cucumber.java8.Fr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class CommandReturnSteps implements Fr {

	// Dependencies
	@Autowired ArticleRepository<Article> articleRepository;
	@Autowired StockRepository<Stock<? extends Article>> stockRepository;
	@Autowired CustomerOrderRepository customerOrderRepository;
	@Autowired APIClient apiClient;

	// Stateful objects
	Article article;
	PerishableState perishableState;
	CustomerOrder customerOrder;
	Stock<? extends Article> stock;
	MvcResult result;

	public CommandReturnSteps() {
		Before(() -> {
			customerOrderRepository.deleteAll();
			stockRepository.deleteAll();
			articleRepository.deleteAll();
		});

		Soit("un article {string} {perissable}", this::createArticle);
		Et("une commande qui date d'il y a {int} jours", this::createOrder);
		Étantdonnéque("il y a {int} {string} en stock", (Integer quantity, String name) -> createStock(quantity));
		Étantdonnéque("il n'y a pas de stock de {string}", this::deleteStockIfExists);
		Et("{int} {string} dans la commande", (Integer quantity, String arg1) -> addArticleToOrder(quantity));
		Lorsque("l'Employé retire les articles de la commande", this::removeArticlesFromOrder);
		Alors("les articles sont retirés de la commande", this::checkArticlesRemovedFromOrder);
		Alors("un nouveau stock de {int} {string} est crée", this::checkStockExists);
		Et("il doit y avoir {int} {string} en stock", this::checkArticleIsInStock);
		Alors("il y a une erreur", this::checkError);
		Alors("ils sont détruits", this::checkArticlesAreDestroyedFromOrder);

		ParameterType("perissable", "(non )?(périssable)", (String non, String perishable) -> {
			if (non == null) {
				return PerishableState.PERISHABLE;
			} else {
				return PerishableState.NOT_PERISHABLE;
			}
		});

	}

	private void checkArticlesAreDestroyedFromOrder() {
		var newOrder = customerOrderRepository.findById(customerOrder.getId()).orElseThrow();
		then(newOrder.getOrderEntries()).isEmpty();
		if (stock != null) {
			var newStock = stockRepository.findById(stock.getStockId()).orElseThrow();
			then(newStock.getQuantity()).isEqualTo(stock.getQuantity());
		} else {
			then(stockRepository.findByArticleId(article.getId())).isNull();
		}
	}

	private void checkError() {
		then(result.getResponse().getStatus()).isGreaterThanOrEqualTo(400);
	}

	private void checkStockExists(Integer quantity, String name) {
		var stocks = stockRepository.findByArticleName(name);
		then(stocks).hasSize(1);
		then(stocks.get(0).getQuantity().longValue()).isEqualTo(quantity.longValue());
	}

	private void deleteStockIfExists(String name) {
		Article article = articleRepository.findByName(name);
		if (article != null) {
			Stock<?> stock = stockRepository.findByArticleId(article.getId());
			if (stock != null) {
				stockRepository.delete(stock);
			}
		}
	}

	private void removeArticlesFromOrder() {
		result = apiClient.returnArticleFromOrder(customerOrder.getId(), article.getId());
	}

	private void checkArticleIsInStock(Integer quantityInStock, String articleName) {
		var newStock = stockRepository.findByArticleId(articleRepository.findByName(articleName).getId());
		then(newStock).isNotNull();
		then(newStock.getQuantity().longValue()).isEqualTo(quantityInStock.longValue());
	}

	private void checkArticlesRemovedFromOrder() {
		var newOrder = customerOrderRepository.findById(customerOrder.getId()).orElseThrow();
		then(newOrder.getOrderEntries()).isEmpty();
	}

	private void addArticleToOrder(Integer quantity) {
		assert customerOrder.getId() != null;
		customerOrder.getOrderEntries().add(new OrderEntry(article, quantity));
		customerOrderRepository.save(customerOrder);
	}

	private void createStock(Integer quantity) {
		switch (this.perishableState) {
			case NOT_PERISHABLE:
				stock = new NonPerishableStock(quantity.longValue(), (NonPerishableArticle) article);
				break;
			case PERISHABLE:
				stock = new PerishableStock(quantity.longValue(), (PerishableArticle) article,
						LocalDate.now().plusDays(10));
				break;
		}
		stockRepository.save(stock);
	}

	private void createOrder(Integer daysSinceOrder) {
		customerOrderRepository.deleteAll();
		customerOrder = new CustomerOrder(new ArrayList<>(), LocalDate.now().minusDays(daysSinceOrder));
		customerOrderRepository.save(customerOrder);
	}

	private void createArticle(String name, PerishableState perishableState) {
		if (perishableState.equals(PerishableState.PERISHABLE)) {
			article = new PerishableArticle(name, 0L);
		} else if (perishableState.equals(PerishableState.NOT_PERISHABLE)) {
			article = new NonPerishableArticle(name, 0L);
		}
		this.perishableState = perishableState;
		articleRepository.save(article);
	}

	public enum PerishableState {
		PERISHABLE,
		NOT_PERISHABLE
	}
}
