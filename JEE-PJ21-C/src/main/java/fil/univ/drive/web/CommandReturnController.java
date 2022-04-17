package fil.univ.drive.web;

import fil.univ.drive.domain.CustomerOrder;
import fil.univ.drive.exception.IllegalResourceException;
import fil.univ.drive.exception.ResourceNotFoundException;
import fil.univ.drive.service.CommandReturnService;
import fil.univ.drive.service.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Controller
@RequestMapping("/order")
@Slf4j
public class CommandReturnController {

	public static final String ERROR_ATTR = "orderError";
	@Autowired CommandReturnService commandReturnService;

	public static final String ERROR_MESSAGE = "errorMessage";

	@GetMapping
	public String getOrderFindingForm(HttpServletRequest request,
									  Model model) {
		ConnectionController.checkUserIsCorrectRole(request, Role.EMPLOYEE);
		return "order_finding_form";
	}

	@GetMapping("/view")
	public String getOrderView(HttpServletRequest request,
							   Model model,
							   @RequestParam Long orderId) {
		ConnectionController.checkUserIsCorrectRole(request, Role.EMPLOYEE);
		CustomerOrder order;
		try {
			order = commandReturnService.findOrder(orderId);
		} catch (ResourceNotFoundException e) {
			model.addAttribute(ERROR_ATTR, true);
			model.addAttribute("errorMessage", e.getMessage());
			return "order_finding_form";
		}
		model.addAttribute("isOld", order.getDateOrdered().isBefore(LocalDate.now().minusDays(7)));
		model.addAttribute("order", order);
		model.addAttribute("orderArticles", order.getOrderEntries());
		return "order_view";
	}

	@GetMapping("/{orderId}/return/{articleId}")
	public String returnItemsFromOrder(HttpServletRequest request,
									   HttpServletResponse response,
									   Model model,
									   @PathVariable Long orderId,
									   @PathVariable Long articleId) {
		ConnectionController.checkUserIsCorrectRole(request, Role.EMPLOYEE);
		log.info("Removing {} article from order {}", articleId, orderId);
		try {
			commandReturnService.returnArticleFromOrder(orderId, articleId);
		} catch (IllegalResourceException | ResourceNotFoundException e) {
			model.addAttribute(ERROR_ATTR, true);
			model.addAttribute(ERROR_MESSAGE, e.getMessage());
			response.setStatus(404);
		}
		return getOrderView(request, model, orderId);
	}
}
