package fil.univ.drive.steps;

import fil.univ.drive.APIClient;
import fil.univ.drive.service.user.Role;
import fil.univ.drive.web.ConnectionController;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.util.Locale;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class ConnectionSteps implements En {

	@Autowired APIClient api;

	Cookie connectionCookie;
	MvcResult result;

	public ConnectionSteps() {
		Given("The User is not already connected", () -> {
			connectionCookie = noCookie();
		});
		Given("The User is already connected as a {word}", (String role) -> {
			connectionCookie = new Cookie(ConnectionController.COOKIE_KEY, role);
		});
		When("The User connects as an {word}", (String role) -> {
			result = api.connect(connectionCookie, role);
		});
		When("The User disconnects", () -> {
			result = api.disconnect(connectionCookie);
		});
		Then("The User is connected as an {word}", (String role) -> {
			Cookie cookie = result.getResponse().getCookie(ConnectionController.COOKIE_KEY);
			checkCookieIsCorrectRole(cookie, Role.valueOf(role.toUpperCase(Locale.ROOT)));
		});
		Then("The User is not connected", () -> {
			Cookie cookie = result.getResponse().getCookie(ConnectionController.COOKIE_KEY);
			checkCookieIsCorrectRole(cookie, Role.DISCONNECTED);
		});
	}

	private void checkCookieIsCorrectRole(Cookie cookie, Role expectedRole) {
		then(cookie).as("The cookie should be set").isNotNull();
		assert cookie != null;
		then(cookie.getValue()).isEqualTo(expectedRole.toString());
	}

	public Cookie noCookie() {
		return new Cookie("noCookie", "noValue");
	}
}
