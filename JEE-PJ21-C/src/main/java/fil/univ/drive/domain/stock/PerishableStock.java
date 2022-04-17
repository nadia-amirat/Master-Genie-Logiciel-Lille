package fil.univ.drive.domain.stock;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.PerishableArticle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * A stock of articles that can be `périmé`
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class PerishableStock extends Stock<PerishableArticle> {

	@EqualsAndHashCode.Include
	private LocalDate expiryDate;

	public PerishableStock(Long quantity, PerishableArticle article, LocalDate expiryDate) {
		super(quantity);
		this.article = article;
		this.expiryDate = expiryDate;
	}

	@Override
	public Article getReferencedArticle() {
		return article;
	}

}
