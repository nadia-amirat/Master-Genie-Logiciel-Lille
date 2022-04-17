package fil.univ.drive.domain.stock;

import fil.univ.drive.domain.article.Article;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a collection of articles.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DiscriminatorColumn(name="descriminatorColumn")
public abstract class Stock<T extends Article> implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	Long stockId;
	Long quantity;
	@ManyToOne(targetEntity = Article.class)
	T article;


	public Stock(Long quantity) {
		this.quantity = quantity;
	}

	public abstract Article getReferencedArticle();
}
