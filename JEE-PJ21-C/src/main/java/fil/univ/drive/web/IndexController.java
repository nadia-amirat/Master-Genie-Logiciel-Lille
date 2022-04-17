package fil.univ.drive.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

	@GetMapping({"/", "/index"})
	public String test(Model model, @RequestParam(value="name", required=false, defaultValue="World") String name) {
		System.out.println("In test");
		return "index";
	}
}
