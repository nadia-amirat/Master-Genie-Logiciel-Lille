package fil.univ.drive.domain.article;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * It is an article that cannot be `périmé`
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NonPerishableArticle extends Article{
    public NonPerishableArticle(String name, Long categoryId) {
        super(name, categoryId);
    }
}
