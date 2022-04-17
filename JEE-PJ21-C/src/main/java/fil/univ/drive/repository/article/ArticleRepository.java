package fil.univ.drive.repository.article;

import fil.univ.drive.domain.article.Article;

import fil.univ.drive.domain.article.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository<T extends Article> extends JpaRepository<T, Long> {
	Article findByName(String name);
	T getById(Long articleId);

    Page<Article> findByName(String name, Pageable page);

    Page<Article> findByNameAndCategories(String name, Category category, Pageable numPage);

    Page<Article> findByCategories(Category category, Pageable numPage);

    List<T> findByIdAndCategories(long parseLong, Category c);

    List<T> findByIdAndName(long reference, String name);

    Article findByIdAndNameAndCategories(long reference, String name, Category category);
}
