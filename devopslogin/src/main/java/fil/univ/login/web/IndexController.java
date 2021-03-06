
package fil.univ.login.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class IndexController {

    @GetMapping({ "/login", "/"})
    public String test(Model model, @RequestParam(value="name", required=false, defaultValue="World") String name) {
        return "index";
    }

    @GetMapping({"/newAccount"})
    public String newAccountRedirection() {
        return "createAccount";
    }
}

