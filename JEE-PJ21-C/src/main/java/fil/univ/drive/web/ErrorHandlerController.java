package fil.univ.drive.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ErrorHandlerController implements ErrorController{


	@RequestMapping("/error")
	public String getErrorPath(Model model, HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		var msg = String.format("status %d reason : %s",
				statusCode, exception==null? "N/A": exception.getMessage());
		log.error(msg);
		model.addAttribute("errorMessage", msg);
		return "error";
	}
}
