package fil.univ.drive.repository.article;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository<T extends Category> extends JpaRepository<Category, Long> {
    Category findByName(String category);

    @Query("SELECT distinct(ca.name) from Category ca order by ca.name")
    List<String> selectDistinctCategories();
}
