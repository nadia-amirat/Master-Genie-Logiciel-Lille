package fil.univ.drive.domain.article;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * It is an article that can be `périmé`
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PerishableArticle extends Article {
    public PerishableArticle(String name, Long categoryId) {
        super(name, categoryId);
    }



}
