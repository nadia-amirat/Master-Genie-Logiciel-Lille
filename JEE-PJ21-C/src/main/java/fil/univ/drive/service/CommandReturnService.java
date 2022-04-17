package fil.univ.drive.service;

import fil.univ.drive.domain.CustomerOrder;
import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.exception.IllegalResourceException;
import fil.univ.drive.exception.ResourceNotFoundException;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.stock.NonPerishableStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommandReturnService {
	@Autowired NonPerishableStockRepository nonPerishableStockRepository;
	@Autowired ArticleRepository<Article> articleRepository;
	@Autowired CustomerOrderRepository customerOrderRepository;

	public CustomerOrder findOrder(Long orderId) {
		return customerOrderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("Order %s was not found", orderId)));
	}

	/**
	 * Deletes the item from the order.
	 * If the item is perishable, destroys it, otherwise puts it back into the first available stock, or into a new stock.
	 *
	 * @param orderId   the id of the order
	 * @param articleId the id of the article to remove
	 * @throws fil.univ.drive.exception.IllegalResourceException  if the order was made more than 7 days ago
	 * @throws fil.univ.drive.exception.ResourceNotFoundException if any of the resources is not found
	 */
	public void returnArticleFromOrder(Long orderId, Long articleId) {
		var order = customerOrderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("Order %s was not found", orderId)));
		if (order.getDateOrdered().isBefore(LocalDate.now().minusDays(7)))
			throw new IllegalResourceException(
					String.format("Order %s was made more than 7 days ago, it is not possible to remove items from it", orderId));
		var articleToRemove = articleRepository.findById(articleId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("Article %s was not found", articleId)));
		var orderEntryToRemove = order.getOrderEntries().stream()
				.filter(orderEntry -> orderEntry.getArticle().equals(articleToRemove)).limit(1).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format("Article %s was not in the order", articleId)));
		order.getOrderEntries().remove(orderEntryToRemove);
		customerOrderRepository.save(order);

		if (articleToRemove instanceof NonPerishableArticle) {
			List<NonPerishableStock> stocks = nonPerishableStockRepository.findAllByArticleId(articleToRemove.getId());
			NonPerishableStock stockToSave;
			if (!stocks.isEmpty()) {
				stockToSave = stocks.get(0);
				stockToSave.setQuantity(stockToSave.getQuantity() + orderEntryToRemove.getQuantity());
			} else {
				stockToSave = new NonPerishableStock(
						orderEntryToRemove.getQuantity().longValue(),
						(NonPerishableArticle) articleToRemove);
			}
			nonPerishableStockRepository.save(stockToSave);
		}
	}
}
