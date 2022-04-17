package fil.univ.drive.web;

import fil.univ.drive.exception.InvalidRoleException;
import fil.univ.drive.service.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping()
@Slf4j
public class ConnectionController {

	public static final String COOKIE_KEY = "drive-connection";

	@GetMapping("/connect")
	public String getConnectionPage(Model model, HttpServletResponse response,
									@CookieValue(name = COOKIE_KEY, defaultValue = "DISCONNECTED") Role role) {
		response.addCookie(new Cookie(COOKIE_KEY, role.name()));
		model.addAttribute("actualRole", role.name());
		return "connexion";
	}

	@PostMapping("/connect")
	public RedirectView connect(@RequestParam Role role, HttpServletResponse response) {
		log.info("Connecting as {}", role);
		response.addCookie(new Cookie(COOKIE_KEY, role.name()));
		return new RedirectView("/connect");
	}

	@GetMapping("/disconnect")
	public RedirectView disconnect(HttpServletResponse response) {
		response.addCookie(new Cookie(COOKIE_KEY, Role.DISCONNECTED.name()));
		return new RedirectView("/connect");
	}

	public static void checkUserIsCorrectRole(HttpServletRequest request, Role expectedRole) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(COOKIE_KEY)) {
					Role actualRole = Role.valueOf(cookie.getValue());
					if (actualRole.equals(expectedRole)) {
						return;
					} else {
						throw new InvalidRoleException(cookie.getValue() + " is not an authorized role for this page",
								actualRole);
					}
				}
			}
		}
		throw new InvalidRoleException("You must be connected to access this page", Role.DISCONNECTED);
	}
}
