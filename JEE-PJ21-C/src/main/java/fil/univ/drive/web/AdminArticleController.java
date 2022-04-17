package fil.univ.drive.web;


import fil.univ.drive.domain.article.Article;
import fil.univ.drive.domain.article.Category;
import fil.univ.drive.exception.ArticleNotFound;
import fil.univ.drive.repository.article.ArticleRepository;
import fil.univ.drive.repository.article.CategoryRepository;
import fil.univ.drive.service.AdminArticleService;
import fil.univ.drive.service.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(path = "/articles")
public class AdminArticleController {

    @Autowired
    private ArticleRepository<Article> articleRepository;

    @Autowired
    private CategoryRepository<Category> categoryCategoryRepository;

    @Autowired
    AdminArticleService adminArticleService;

    @GetMapping(produces = "text/html")
    public String getPage(Model model, HttpServletRequest request){
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        List<String> categories = categoryCategoryRepository.selectDistinctCategories();
        model.addAttribute("category",categories);

        return "articles";
    }

    @GetMapping(path = "/search",produces = "text/html")
    public String getArticles( @RequestParam(value = "category") String category,
                                           @RequestParam(defaultValue = "",value = "article_name") String name,
                                           @RequestParam(defaultValue = "",value = "ref") String reference,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           Model model,
                               HttpServletRequest request) throws Exception, ArticleNotFound {
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        AdminArticleService.SearchResponse res = null;
        try {
            res =  adminArticleService.search(category,name,reference,page,size);
        }catch (ArticleNotFound e){
            model.addAttribute("error",e.getMessage());
            return getPage(model, request);
        }
        Page<Article> resPage = res.getPage();
        List<Article> articles = res.getArticles();
        if (resPage != null)
            model.addAttribute("list",resPage);
        if (articles != null)
            model.addAttribute("search",articles);

        return getPage(model, request);
    }

    @PostMapping(path = "/add", produces = "text/html")
    public String createProduct(Model model, @RequestParam("product-name") String productName,
                              @RequestParam("category-name") String categoryName,
                              @RequestParam("category-id") Integer categoryId,
                              @RequestParam("product-price") String price,
                                HttpServletRequest request){
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        try {
            boolean result = adminArticleService.addProduct(productName, categoryName, categoryId, price);
            if (result == true){
                model.addAttribute("success", "Product Added Successfully");
            }else {
                model.addAttribute("error", "Impossible to add product");
            }
        }catch (ArticleNotFound e){
            model.addAttribute("error",e.getMessage());
            return getPage(model, request);
        }

        return getPage(model, request);
    }

    @PostMapping(path = "/update", produces = "text/html")
    public String updateProduct(Model model, @RequestParam("product-id") Integer id,
                              @RequestParam("product-name") String pName,
                              @RequestParam("category-name") String cName,
                              @RequestParam("category-id") Integer cId,
                              @RequestParam("product-price") String pPrice,
                                HttpServletRequest request){
        ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
        Category category;
        category = categoryCategoryRepository.findByName(cName);
        if (category == null){
            category = new Category(cName);
            categoryCategoryRepository.save(category);
        }
        double parsePrice;
        try {
            parsePrice = Double.parseDouble(pPrice);
        }catch (Exception e){
            model.addAttribute("error", "ERROR: Please provide a good price value, ex : X.X or X as X is a number");
            return getPage(model, request);
        }

        Article article = articleRepository.getById(Long.parseLong(String.valueOf(id)));
        article.setName(pName);
        article.setCategoryId(Long.parseLong(String.valueOf(cId)));
        article.setPrice(parsePrice);
        article.setCategories(category);
        try {
            articleRepository.save(article);
            model.addAttribute("success", "Product Updated successfully");
        }catch (Exception e){
            model.addAttribute("error", "ERROR: Impossible to update product");
        }

        return getPage(model, request);

    }
}
