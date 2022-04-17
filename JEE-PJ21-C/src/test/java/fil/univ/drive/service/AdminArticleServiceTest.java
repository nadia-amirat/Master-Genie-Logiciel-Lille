package fil.univ.drive.service;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.Category;
import fil.univ.drive.domain.article.Product;
import fil.univ.drive.exception.ArticleNotFound;
import fil.univ.drive.repository.CustomerOrderRepository;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.article.CategoryRepository;
import fil.univ.drive.repository.stock.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminArticleServiceTest {
    @Autowired
    ArticleRepository<Article> articleRepository;

    @Autowired
    CategoryRepository<Category> categoryRepository;

    @Autowired
    AdminArticleService adminArticleService;

    @Autowired StockRepository stockRepository;
    @Autowired CustomerOrderRepository customerOrderRepository;

    private List<Category> categorie;
    private List<Article> product;
    private int maxSize;


    @BeforeEach
    void setUp() {
        customerOrderRepository.deleteAll();
        stockRepository.deleteAll();
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
        categorie = List.of(
                new Category("fruits"),
                new Category("boisson"),
                new Category("maison"),
                new Category("legumes"),
                new Category("viandes")
        );

         product = List.of(
                new Product("jus", 0L,3.4,categorie.get(1)),
                new Product("pomme de terre", 0L,2.4,categorie.get(3)),
                new Product("soda", 0L,1.4,categorie.get(1)),
                new Product("carotte", 0L,10,categorie.get(3)),
                new Product("lit", 0L,14,categorie.get(2)),
                new Product("banane", 0L,14,categorie.get(0))
        );

        categoryRepository.saveAll(categorie);
        articleRepository.saveAll(product);
        maxSize = articleRepository.findAll().size();
    }

    @Test
    void searchWhenCategoryIsAllAndArticleNameAndIdAreEmpty() throws ArticleNotFound {
        assertEquals(adminArticleService.search("All","","",0,maxSize).getArticles().size(),articleRepository.findAll().size());
        assertEquals(adminArticleService.search("All","","",0,maxSize).getArticles(),articleRepository.findAll());
    }

    @Test
    void searchByCategoryOnly() throws ArticleNotFound {
        assertEquals(adminArticleService.search("legumes","","",0,maxSize).getArticles().size(),2);
        assertEquals(adminArticleService.search("fruits","","",0,maxSize).getArticles().size(),1);
    }

    @Test
    void throwExceptionWhenThereIsNoProductWithCategory(){
        assertThrows(ArticleNotFound.class, () -> adminArticleService.search("viandes","","",0,maxSize));
    }

    @Test
    void throwExceptionWhenThereIsNorArticles(){
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
        assertThrows(ArticleNotFound.class ,() -> adminArticleService.search("All","","",0,maxSize));
    }

    @Test
    void searchByArticleNameOnly() throws ArticleNotFound {
        Pageable numPage = PageRequest.of(0, maxSize);
        assertEquals(adminArticleService.search("All","banane","",0,maxSize).getArticles().size(),
                articleRepository.findByName("banane",numPage).getTotalElements());
    }
    @Test
    void throwExceptionWhenSearchByArticleNameOnly(){
        assertThrows(ArticleNotFound.class, () -> adminArticleService.search("All","machine","",0,maxSize));
    }

    @Test
    void throwExceptionWhenSearchByIdOnly() throws ArticleNotFound {
        assertThrows(ArticleNotFound.class,()-> adminArticleService.search("All","","999",0,maxSize));
    }

    @Test
    void searchByCategoryAndArticleName() throws ArticleNotFound {
        assertEquals(adminArticleService.search("legumes","carotte","",0,maxSize).getArticles().size(),1);
        Article a = new Product("carotte",0L,2.3,categorie.get(3));
        articleRepository.save(a);
        assertEquals(adminArticleService.search("legumes","carotte","",0,maxSize).getArticles().size(),2);
    }
    @Test
    void ThrowExceptionWhenSearchByCategoryAndArticleName() throws ArticleNotFound {
        assertThrows(ArticleNotFound.class,() -> adminArticleService.search("legumes","banane","",0,maxSize));
    }

    @Test
    void searchByCategoryAndId() throws ArticleNotFound {
        Long id = product.get(0).getId();
        assertNotNull(adminArticleService.search("boisson","", String.valueOf(id),0,maxSize));
    }

    @Test
    void throwExceptionWhenSearchByCategoryAndId(){
        Long id = product.get(0).getId();
        assertThrows(ArticleNotFound.class,() -> adminArticleService.search("legumes","", String.valueOf(id),0,maxSize));
    }

    @Test
    void searchByNameAndId() throws ArticleNotFound {
        Pageable numPage = PageRequest.of(0, maxSize);
        Long id = product.get(0).getId();
        String name = product.get(0).getName();
        Article a = articleRepository.findByIdAndName(id,name).get(0);
        System.out.println(a.getName());
        assertEquals(adminArticleService.search("All",name, String.valueOf(id),0,maxSize).getArticles().get(0),a);

    }

    @Test
    void throwExceptionWhenSearchByNameAndId() throws ArticleNotFound {
        Long id = product.get(0).getId();
        String name = product.get(0).getName();
        Article a = articleRepository.findByIdAndName(id,name).get(0);
        System.out.println(a.getName());
        assertThrows(ArticleNotFound.class,() -> adminArticleService.search("All","salut", String.valueOf(id),0,maxSize).getArticles().get(0));
    }

    @Test
    void searchByCategoryAndNameAndId() throws ArticleNotFound {
        Long id = product.get(0).getId();
        String name = product.get(0).getName();
        Category c = product.get(0).getCategories();
        assertEquals(adminArticleService.search(c.getName(),name, String.valueOf(id),0,maxSize).getArticles().get(0),product.get(0));
    }

    @Test
    void throwExceptionWhenSearchByCategoryAndNameAndId() throws ArticleNotFound {
        Long id = product.get(0).getId();
        String name = product.get(0).getName();
        Category c = product.get(0).getCategories();
        assertThrows(ArticleNotFound.class,() -> adminArticleService.search(c.getName(),"salut", String.valueOf(id),0,maxSize).getArticles().get(0));
    }


    @Test
    void addProductTest() throws ArticleNotFound {
        String productName = "pomme";
        String categoryName = "fruit";
        Integer categoryId = 0;
        String price = "1.99";
        assertTrue(adminArticleService.addProduct(productName,categoryName,categoryId,price));
    }

    @Test
    void throwExceptionAddProductTest() throws ArticleNotFound {
        String productName = "pomme";
        String categoryName = "fruit";
        Integer categoryId = 0;
        String price = "1,99";
        assertThrows(ArticleNotFound.class,()-> adminArticleService.addProduct(productName,categoryName,categoryId,price));
    }






}
