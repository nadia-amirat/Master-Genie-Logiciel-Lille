package fil.univ.drive.repository.article;

import fil.univ.drive.domain.article.PerishableArticle;
import org.springframework.stereotype.Repository;

@Repository
public interface PerishableArticleRepository extends ArticleRepository<PerishableArticle> {
    PerishableArticle getById(Long articleId);
}
