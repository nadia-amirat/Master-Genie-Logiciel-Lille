package fil.univ.drive.service;

import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.Category;
import fil.univ.drive.domain.article.Product;
import fil.univ.drive.exception.ArticleNotFound;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.article.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminArticleService {

    @Autowired
    private ArticleRepository<Article> articleRepository;

    @Autowired
    private CategoryRepository<Category> categoryCategoryRepository;

    @Data
    @AllArgsConstructor
    public class SearchResponse {
        public List<Article> articles;
        public Page<Article> page;

        public SearchResponse(List<Article> articles){
            this.articles = articles;
        }
    }

    public SearchResponse search(String category, String name, String reference, int page, int size) throws ArticleNotFound {
        List<Article> articles = new ArrayList<>();
        Pageable numPage = PageRequest.of(page, size);
        
        if ((category.equals("All")) && name.isEmpty() && reference.isEmpty()){
            Page<Article> a = articleRepository.findAll(numPage);
            if (a == null || a.isEmpty()){
                throw new ArticleNotFound("there is no product");
            }else {
                articles = a.getContent();
                return new SearchResponse(articles, a);
            }
        }else if(!category.equals("All") && name.isEmpty() && reference.isEmpty()){
            try {
                Category categorie = categoryCategoryRepository.findByName(category);
                Page<Article> a = articleRepository.findByCategories(categorie,numPage);
                if (a.isEmpty() || a == null){
                    throw new ArticleNotFound(String.format("ERROR: there is no product with category : %s",category));
                }
                articles = a.getContent();
                return new SearchResponse(articles, a);

            }catch (Exception e){
                throw new ArticleNotFound(String.format("ERROR: there is no product with category : %s",category));
            }

        }else if(category.equals("All") && !name.isEmpty() && reference.isEmpty()){
            try {
                Page<Article> p = articleRepository.findByName(name,numPage);
                articles = p.getContent();
                System.out.println(articles);
                if (!articles.isEmpty() && articles != null){
                    return new SearchResponse(articles, p);
                } else
                    throw new ArticleNotFound(String.format("ERROR: there is no product with name : %s",name));
            }catch (ArticleNotFound e){
                throw new ArticleNotFound(String.format("ERROR: there is no product with name : %s",name));
            }

        }else if(category.equals("All") && name.isEmpty() && (!reference.isEmpty() || reference != null)){
            Article a;
            try {
                a = articleRepository.getById(Long.parseLong(reference));
                System.out.println("Articel : " + a.getName());
                if (a != null){
                    articles.add(a);
                    return new SearchResponse(articles);
                }else{
                    throw new ArticleNotFound(String.format("ERROR : there is no article with reference : %s",reference));
                }
            }catch (Exception e){
                throw new ArticleNotFound(String.format("ERROR : there is no article with reference : %s",reference));
            }

        }else if(!category.equals("All") && !name.isEmpty() && reference.isEmpty()){
            try {
                Category cat = categoryCategoryRepository.findByName(category);
                Page<Article> p  = articleRepository.findByNameAndCategories(name,cat,numPage);
                articles = p.getContent();
                if (articles.size()> 0){
                    return new SearchResponse(articles,p);
                }
                else{
                    throw new ArticleNotFound(String.format("ERROR : there is no article with category %s and name %s",category,name));
                }
            }catch (ArticleNotFound e ){
                throw new ArticleNotFound(String.format("ERROR : there is no article with category %s and name %s",category,name));
            }

        }else if(!category.equals("All") && name.isEmpty() && !reference.isEmpty()){
            try {
                Category c = categoryCategoryRepository.findByName(category);
                articles = articleRepository.findByIdAndCategories(Long.parseLong(reference),c);
                if (articles.size()> 0)
                    return new SearchResponse(articles);
                else
                    throw new ArticleNotFound(String.format("ERROR : there is no article with category %s and reference %s",category,reference));
            }catch (ArticleNotFound e){
                throw new ArticleNotFound(String.format("ERROR : there is no article with category %s and reference %s",category,reference));

            }
        }else if(category.equals("All") && !name.isEmpty() && !reference.isEmpty()){
            try {
                articles = articleRepository.findByIdAndName(Long.parseLong(reference),name);
                if (articles.size() > 0)
                    return new SearchResponse(articles);
                else
                    throw new ArticleNotFound(String.format("ERROR : there is no article with Name : %s and reference: %s",name,reference));
            }catch (ArticleNotFound e){
                throw new ArticleNotFound(String.format("ERROR : there is no article with Name : %s and reference: %s",name,reference));
            }
        }
        try {
            Category c = categoryCategoryRepository.findByName(category);
            Article a = articleRepository.findByIdAndNameAndCategories(Long.parseLong(reference),name,c);

            if (a != null){
                articles.add(a);
                return new SearchResponse(articles);
            }
            throw new ArticleNotFound(String.format("ERROR : there is no article with category: %s , Name : %s and reference: %s ",category,name,reference));
        }catch (ArticleNotFound e){
            throw new ArticleNotFound(String.format("ERROR : there is no article with category: %s , Name : %s and reference: %s ",category,name,reference));

        }
    }


    public boolean addProduct(String productName,
                                 String categoryName,
                                Integer categoryId,
                                String price) throws ArticleNotFound {
        Category category;
        category = categoryCategoryRepository.findByName(categoryName);
        if (category == null){
            category = new Category(categoryName);
            categoryCategoryRepository.save(category);
        }


        Long catId = Long.parseLong(String.valueOf(categoryId));
        System.out.println("category id : "+catId);
        double parsePrice;
        try {
            parsePrice = Double.parseDouble(price);
        }catch (Exception e){
            throw new ArticleNotFound(String.format("Please provide a good price value, ex : X.X or X as X is a number"));
        }
        System.out.println("price parse : "+parsePrice);
        Product p = new Product(productName,catId,parsePrice,category);
        System.out.println("article : "+p);

        articleRepository.save(p);
        if(articleRepository.existsById(p.getId()))
            return true;
        return false;
    }

}
