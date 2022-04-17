package fil.univ.drive.domain.panier;

import fil.univ.drive.web.dto.ArticleEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Cart {

    List<ArticleEntry> articles;

    public Cart(){
        articles = new ArrayList<>();
    }

    public void addArticleToCart(ArticleEntry art){
        this.articles.add(art);
    }

}
