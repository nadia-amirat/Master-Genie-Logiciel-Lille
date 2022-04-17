package fil.univ.drive.repository.stock;

import fil.univ.drive.domain.article.NonPerishableArticle;
import fil.univ.drive.domain.stock.NonPerishableStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonPerishableStockRepository extends StockRepository<NonPerishableStock> {
}