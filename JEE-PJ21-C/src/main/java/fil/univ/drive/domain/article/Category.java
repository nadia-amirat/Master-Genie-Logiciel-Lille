package fil.univ.drive.domain.article;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    @OneToMany(mappedBy = "categories")
    private List<Article> articles;

    public Category(String name){
        this.name = name;
    }
    public Category(String name,List<Article> articles){
        this.name = name;
        this.articles = articles;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
