package finalprjct.doctor.online.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {

	@GetMapping("/")
	public String getHomeTemplate() {
		return "home";
	}
	
	
	@GetMapping("/form-elements")
	public String getFormTemplate() {
		return "form-elements";
	}
	
	@GetMapping("/my-profile")
	public String getBasicTableTemplate() {
		return "my-profile";
	}
}
