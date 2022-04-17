package fil.univ.drive.domain.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Product extends Article{


    public Product(String name, Long categoryId,double price,Category categories){
        super(name,categoryId,categories,price);
    }
    public Product(String name, Long categoryId,double price){
        super(name,categoryId,price);

    }

}
