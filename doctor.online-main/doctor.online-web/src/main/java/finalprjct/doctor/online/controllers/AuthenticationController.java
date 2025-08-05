package finalprjct.doctor.online.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
	
	@GetMapping("/login")
	public String getSignInPage() {
		return "page-signin";
	}
	
	@GetMapping("/signup")
	public String getSignUpPage() {
		return "page-signup";
	}

}
