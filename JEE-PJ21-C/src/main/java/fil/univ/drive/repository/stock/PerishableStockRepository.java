package fil.univ.drive.repository.stock;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.stock.NonPerishableStock;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.web.*;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerishableStockRepository extends StockRepository<PerishableStock> {
   public List<PerishableStock> findByArticle(Article article);
    public List<PerishableStock> findAllByExpiryDateBefore(LocalDate expiryDate);
    public void deleteById(Long stockId);
}
