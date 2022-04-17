package fil.univ.drive.repository.stock;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository<T extends Stock<? extends Article>> extends JpaRepository<T, Long> {
	T findByArticleId(Long referencedArticleId);

	List<T> findAllByArticleId(Long referencedArticleId);

	List<T> findByArticleName(String articleName);
}
