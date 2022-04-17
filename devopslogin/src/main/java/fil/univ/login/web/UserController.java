
package fil.univ.login.web;

import fil.univ.login.model.User;
import fil.univ.login.repository.UserRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@Setter
public class UserController {

    @Value("${broker.url}")
    private String BROKER_URL;

    @Value("${token.duration}")
    private int TOKEN_HOUR_DURATION;

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("user/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping(path="/broker")
    public Object processConnexion(String email, String userCode) {
        log.info("Connecting : " + email);
        User db_user = userRepository.findByEmail(email);
        int length = userCode.length();
        String sms_code =  userCode.substring(userCode.length() -6, length);
        if (db_user!=null && (db_user.getPassword()+sms_code).equals(userCode)) {
            log.info("Success");

            String token = generateToken();
            db_user.setToken(token);
            db_user.setLastSuccess(LocalDateTime.now());
            userRepository.save(db_user);

            return new RedirectView("http://" + BROKER_URL + "/authenticate/redirect" //TODO decomment once broker_url define
                    + "?auth=true"
                    + "&token=" + token);

        } else {
            log.info("Fail");
            return new RedirectView("http://" + BROKER_URL + "/authenticate/redirect" //TODO decomment once broker_url define
                    + "?auth=false");

        }
    }

    @PostMapping(path="/createUser")
    public String createUser(String email, String password, String phoneNumber) {
        User db_user = userRepository.save(new User(email, password, phoneNumber));
        return "successCreate";
    }

    @PostMapping(path="/validate_token")
    public void validateUserToken(HttpServletResponse response, @RequestBody String body){
        try {
            // Parse token
            JSONObject json = new JSONObject(body);
            String token = json.getString("token");

            // Verification
            log.info("Verify token : " + token);
            User db_user = userRepository.findByToken(token);
            String validToken = db_user.getToken();
            LocalDateTime lastSucces = db_user.getLastSuccess();
            if (db_user == null || validToken == null || lastSucces == null || isExpired(lastSucces)){
                log.info("Expired token.");
                response.setStatus(403);
            } else {
                if (token.equals(validToken)){
                    log.info("Token valid. User id : " + db_user.getEmail());
                    response.setStatus(200);
                } else {
                    log.info("Invalid token.");
                    response.setStatus(403);
                }

            }
        } catch (JSONException e) {
            log.error("Bad Request. Wrong JSPON syntax : " + body);
            response.setStatus(400);
        }
    }

    @PostConstruct
    public void init_db() {
        log.debug("Initializing database");
        List<User> users = List.of(new User("nadia@gmail.com","12345", "+33619928199"));
        userRepository.saveAll(users);
    }

    private String generateToken(){
        int min = (int)Math.pow(1, 49);
        int max = min*10-1;
        int number=(int)(Math.random()*(max-min+1)+min);
        return Integer.toString(number);
    }

    private boolean isExpired(LocalDateTime date){
        return date.isBefore(LocalDateTime.now().minusHours(TOKEN_HOUR_DURATION));
    }

}

