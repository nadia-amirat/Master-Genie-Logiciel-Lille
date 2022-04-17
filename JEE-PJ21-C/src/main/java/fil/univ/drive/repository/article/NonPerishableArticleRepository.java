package fil.univ.drive.repository.article;

import fil.univ.drive.domain.article.NonPerishableArticle;
import org.springframework.stereotype.Repository;

@Repository
public interface NonPerishableArticleRepository extends ArticleRepository<NonPerishableArticle> {
}
