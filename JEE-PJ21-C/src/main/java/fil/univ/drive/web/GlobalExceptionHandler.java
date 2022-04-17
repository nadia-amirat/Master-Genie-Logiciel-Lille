package fil.univ.drive.web;

import fil.univ.drive.exception.InvalidRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handles exceptions by returning a response with an error message and an error code
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler
		extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {IllegalArgumentException.class})
	protected String handleIllegalOperationException(IllegalArgumentException ex, Model model) {
		log.error(ex.getMessage());
		model.addAttribute("errorMessage", ex.getMessage());
		return "error";
	}

	@ExceptionHandler(value = {InvalidRoleException.class})
	protected String handleInvalidRoleException(InvalidRoleException ex, Model model) {
		log.error(ex.getMessage());
		model.addAttribute("role", ex.role.name());
		return "invalid_role";
	}
}
