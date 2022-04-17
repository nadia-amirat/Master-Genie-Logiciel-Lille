
package fil.univ.drive.domain.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * An article is an item the shop can sell. It has a unique identifier, a name and a category
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="descriminatorColumn")
public abstract class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long categoryId;

    @ManyToOne
    private Category categories;
    private Double price;

    public Article(String name, Long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }


    public Article(String name, Long categoryId, Category categories, Double price) {
        this.name = name;
        this.categoryId = categoryId;
        this.categories = categories;
        this.price = price;
    }
    public Article(String name, Long categoryId, Double price) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        return id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
