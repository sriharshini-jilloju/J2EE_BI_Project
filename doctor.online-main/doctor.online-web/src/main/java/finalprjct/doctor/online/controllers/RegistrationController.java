package finalprjct.doctor.online.controllers;

import java.util.HashMap;
import java.util.Map;

import org.digital.online.ejb.dtos.UserDTO;
import org.digital.online.ejb.repositories.UserRepository;
import org.digital.online.ejb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/user")
public class RegistrationController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	@PostMapping("/check")
	public ResponseEntity<Map<String, Boolean>> checkUserExists(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String username = request.get("username");

		boolean emailExists = userRepository.existsByEmail(email);
		boolean usernameExists = userRepository.existsByUsername(username);

		Map<String, Boolean> response = new HashMap<>();
		response.put("usernameExists", usernameExists);
		response.put("emailExists", emailExists);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/register")
	public @ResponseBody UserDTO createUser(@RequestBody UserDTO dto) {
		dto.role="PATIENT";
		return userService.createUser(dto);
	}

}
