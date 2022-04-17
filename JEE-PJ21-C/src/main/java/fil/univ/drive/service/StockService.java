package fil.univ.drive.service;

import fil.univ.drive.Utils;
import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.exception.IllegalResourceException;
import fil.univ.drive.exception.NegativeQuantityException;
import fil.univ.drive.exception.ResourceNotFoundException;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.article.NonPerishableArticleRepository;
import fil.univ.drive.repository.stock.NonPerishableStockRepository;
import fil.univ.drive.repository.stock.PerishableStockRepository;
import fil.univ.drive.repository.stock.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StockService {
	@Autowired StockRepository<Stock<? extends Article>> stockRepository;
	@Autowired ArticleRepository<Article> articleRepository;
	@Autowired NonPerishableArticleRepository nonPerishableArticleRepository;
	@Autowired NonPerishableStockRepository nonPerishableStockRepository;
	@Autowired PerishableStockRepository perishableStockRepository;

	/**
	 * Returns a list of all the stocks
	 */
	public List<Stock<? extends Article>> getAllStocks() {
		return stockRepository.findAll();
	}

	/**
	 * Creates and saves a non-perishable stock of an article
	 *
	 * @param quantity the quantity of articles to add to the stock
	 * @param articleId the id of the article
	 * @throws ResourceNotFoundException if the article does not exist
	 * @throws NegativeQuantityException if the quantity is negative
	 * @throws IllegalResourceException if the article is perishable
	 */
	public void createStockOfArticle(Long quantity, Long articleId) {
		log.debug("Creating stock of article " + articleId);
		if(!articleRepository.existsById(articleId)) {
			throw new ResourceNotFoundException(String.format("The article with id %s does not exist", articleId));
		}
		if(quantity < 0) {
			throw new NegativeQuantityException("Quantity cannot be negative");
		}
		if(!nonPerishableArticleRepository.existsById(articleId)) {
			throw new IllegalResourceException("Cannot add a stock of a perishable article");
		}
		NonPerishableArticle article = nonPerishableArticleRepository.findById(articleId).orElseThrow();
		NonPerishableStock stock = new NonPerishableStock(quantity, article);
		stockRepository.save(stock);
	}

	/**
	 * Adjusts the quantity of a given stock of articles.
	 *
	 * @param newQuantity the new quantity of the stock
	 * @param stockId the stock reference
	 * @throws NegativeQuantityException if the newQuantity is negative
	 * @throws ResourceNotFoundException if the stock does not exist
	 */
	public void adjustStock(Long newQuantity, Long stockId) {
		log.debug("Adjusting stock " + stockId);
		checkStockExists(stockId);
		Stock<? extends Article> stock = stockRepository.findById(stockId).orElseThrow();
		if(newQuantity < 0) {
			throw new NegativeQuantityException("Cannot have a negative stock quantity");
		}
		stock.setQuantity(newQuantity);
		stockRepository.save(stock);
	}

	/**
	 * Simply deletes the given stock
	 *
	 * @param stockId the reference of the stock
	 * @throws ResourceNotFoundException if the stock does not exist
	 */
	public void deleteStock(Long stockId) {
		log.debug("Deleting stock " + stockId);
		checkStockExists(stockId);
		stockRepository.deleteById(stockId);
	}

	private void checkStockExists(Long stockId) {
		if(!stockRepository.existsById(stockId)) {
			throw new ResourceNotFoundException(String.format("The stock of id %s was not found", stockId));
		}
	}
}
