package fil.univ.drive.domain;

import fil.univ.drive.domain.article.Article;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class OrderEntry implements Serializable {
	@ManyToOne
	private Article article;
	private Integer quantity;

	public OrderEntry(Article articles, Integer quantity) {
		this.article = articles;
		this.quantity = quantity;
	}

	public static OrderEntry of(Article article, Integer quantity) {
		return new OrderEntry(article, quantity);
	}
}
