package fil.univ.drive.web;

import fil.univ.drive.domain.article.PerishableArticle;
import fil.univ.drive.domain.stock.PerishableStock;
import fil.univ.drive.repository.article.PerishableArticleRepository;
import fil.univ.drive.repository.stock.PerishableStockRepository;
import fil.univ.drive.service.StockService;
import fil.univ.drive.service.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(path = "/stock/perishable")
public class PerishableStockController {

	public static final String ERRORQUANTITY = "error la quantité doit être supérieur 0";
	public static final String ERRORLOT = "error le nombre de  lot  doit être supérieur 0";
	public static final String ERRORARTICLE = "la référence de l'article n'existe pas dans la bases de produit périssable";

	public static final String SUCESSEADDARTICLE = "la quantité a été ajouté";
	public static final String SUCESSEADDSTOCKADD = "un nouveaux stock a été créer";

	@Autowired
	PerishableArticleRepository perishableArticleRepository;

	@Autowired
	PerishableStockRepository perishableStockRepository;

	@Autowired
	StockService stockService;

	@GetMapping({"", "/"})
	public String getPerishableSockform(Model model, HttpServletRequest request) {
		ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
		List<PerishableArticle> perishableArticles = perishableArticleRepository.findAll();
		List<Long> refs = new ArrayList<Long>();
		for (PerishableArticle perishableArticle : perishableArticles) {
			refs.add(perishableArticle.getId());
		}
		model.addAttribute("refs", refs);
		return "manage_perishable_stocks";
	}


	@PostMapping(path = "/")
	public String addStock(Model model, @RequestParam Long articleId, @RequestParam Long quantity,
						   @RequestParam Long lot,
						   @RequestParam("expirationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
								   LocalDate expirationDate, HttpServletResponse servletResponse,
						   HttpServletRequest request) {
		ConnectionController.checkUserIsCorrectRole(request, Role.ADMIN);
		if (!checkFormValitity(model, quantity, lot, articleId)) {
			return getPerishableSockform(model, request);
		}

		try {
			PerishableArticle article = perishableArticleRepository.getById(articleId);
			System.out.println(article.getName());
			List<PerishableStock> perishableStocks = perishableStockRepository.findByArticle(article);
			for (PerishableStock perishableStock : perishableStocks) {
				if (perishableStock.getExpiryDate().equals(expirationDate)) {
					Long newQuantity = perishableStock.getQuantity() + lot * quantity;
					stockService.adjustStock(newQuantity, perishableStock.getStockId());
					model.addAttribute("success", SUCESSEADDARTICLE);
					return getPerishableSockform(model, request);
				}
			}
			PerishableStock perishableStock = new PerishableStock((lot * quantity), article, expirationDate);
			perishableStockRepository.save(perishableStock);
			System.out.println(article.getName());
			model.addAttribute("success", SUCESSEADDSTOCKADD);

			return getPerishableSockform(model, request);
		} catch (Exception e) {
			servletResponse.setStatus(400);
		}

		return getPerishableSockform(model, request);
	}

	private boolean checkFormValitity(Model model, Long quantity, Long lot, Long articleId) {
		boolean res = true;
		if (quantity <= 0) {
			model.addAttribute("quantityError", ERRORQUANTITY);
			res = false;
		}

		if (!perishableArticleRepository.existsById(articleId)) {
			model.addAttribute("ReférenceError",
					"La reférence de l'article n'existe pas dans la base  des article périssable");
			res = false;
		}

		if (lot <= 0) {
			model.addAttribute("lotError", ERRORLOT);
			res = false;
		}
		return res;
	}
}
