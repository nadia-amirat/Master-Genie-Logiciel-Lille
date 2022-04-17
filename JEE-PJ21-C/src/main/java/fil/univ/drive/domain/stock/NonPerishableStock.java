package fil.univ.drive.domain.stock;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.NonPerishableArticle;
import lombok.*;

import javax.persistence.*;

/**
 * Represents a collection of
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class NonPerishableStock extends Stock<NonPerishableArticle> {

	public NonPerishableStock(Long quantity, NonPerishableArticle article) {
		super(quantity);
		this.article = article;
	}

	@Override
	public Article getReferencedArticle() {
		return article;
	}
}
