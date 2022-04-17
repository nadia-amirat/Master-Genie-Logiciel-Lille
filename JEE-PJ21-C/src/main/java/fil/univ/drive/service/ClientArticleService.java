package fil.univ.drive.service;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.panier.Cart;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.domain.stock.Stock;
import fil.univ.drive.exception.NegativeQuantityException;
import fil.univ.drive.exception.ResourceNotFoundException;
import fil.univ.drive.repository.stock.StockRepository;
import fil.univ.drive.web.dto.ArticleEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientArticleService {

	@Autowired
	StockRepository<Stock<? extends Article>> stockRepository;

	public void addArticleToCart(Long stockId, ArticleEntry article, Cart cart) {
		Optional<Stock<? extends Article>> stock = stockRepository.findById(stockId);
		if (!stock.isPresent()) {
			throw new ResourceNotFoundException("Le stock demandé n'existe pas !");
		}
		if (stock.get().getQuantity() < 0) {
			throw new NegativeQuantityException("Il n y a plus de quantité !");
		}
		ArticleEntry found_article = null;
		for (ArticleEntry art : cart.getArticles())
			if (art.getId().equals(article.getId()))
				found_article = art;

		if (found_article != null) {
			found_article.setQty(found_article.getQty() + article.getQty());
			found_article.setName(stock.get().getArticle().getName());
		} else if (found_article == null && article != null) {

			found_article = article;
			found_article.setQty(article.getQty());
			found_article.setName(stock.get().getArticle().getName());
		}

		cart.addArticleToCart(found_article);
		cart.setArticles(cart.getArticles().stream().distinct().collect(Collectors.toList()));
		stock.get().setQuantity(stock.get().getQuantity() - 1);
		stockRepository.save(stock.get());
	}

	public List<Stock<? extends Article>> findArticles() {
		List<Stock<? extends Article>> stockList = stockRepository.findAll();
		List<Stock<? extends Article>> res = new ArrayList<>();
		for (Stock<? extends Article> s : stockList
		) {
			if (s instanceof PerishableStock) {
				if (((((PerishableStock) s).getExpiryDate().compareTo(LocalDate.now().plusDays(5))) > 0) && s.getQuantity() > 0) {
					res.add(s);

				}
			}
			if ((s instanceof NonPerishableStock) && s.getQuantity() > 0) {
				res.add(s);
			}
		}
		return res;
	}
}
