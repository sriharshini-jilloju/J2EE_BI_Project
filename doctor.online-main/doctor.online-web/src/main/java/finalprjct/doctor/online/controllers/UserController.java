package finalprjct.doctor.online.controllers;

import java.util.List;

import org.digital.online.ejb.dtos.DoctorEditDTO;
import org.digital.online.ejb.dtos.UserDTO;
import org.digital.online.ejb.entities.DoctorDetails;
import org.digital.online.ejb.entities.Specialization;
import org.digital.online.ejb.entities.User;
import org.digital.online.ejb.repositories.SpecializationRepository;
import org.digital.online.ejb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	SpecializationRepository specializationRepository;

	@PostMapping
	public @ResponseBody UserDTO createUser(@RequestBody UserDTO dto) {
		return userService.createUser(dto);
	}

	@PostMapping("/admin")
	public @ResponseBody UserDTO createAdminUser(@RequestBody UserDTO dto) {
		return userService.createUser(dto);
	}

	@GetMapping("/{id}")
	public @ResponseBody UserDTO getUser(@PathVariable Long id) {
		return userService.getUser(id);
	}

	@PostMapping("/delete/{id}")
	public @ResponseBody void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
		return userService.updateUser(id, user) ? ResponseEntity.ok().build()
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PutMapping("/doctor/{id}")
	public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody DoctorEditDTO doctorEditDTO) {
		User user = doctorEditDTO.user;
		Specialization specialization = specializationRepository.findById(doctorEditDTO.specialization)
				.orElseThrow(() -> new RuntimeException("Specialization not found"));
		DoctorDetails doctorDetails = user.getDoctorDetails();

		doctorDetails.setSpecialization(specialization);
		doctorDetails.setAvailabilitySlots(userService.getSlotsFromDTO(doctorEditDTO.doctorSlots, doctorDetails));

		user.setDoctorDetails(doctorDetails);
		return userService.updateUser(id, user) ? ResponseEntity.ok().build()
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/admins-list")
	public @ResponseBody List<UserDTO> getAdminsList() {
		return userService.getAllAdmins();
	}

	@GetMapping("/doctors-list")
	public @ResponseBody List<UserDTO> getDoctorsList() {
		return userService.getAllDoctors();
	}
	
	@GetMapping("/doctors-list/by-department/{id}")
	public @ResponseBody List<UserDTO> getDoctorsListByDepartment(@PathVariable Long id) {
		return userService.getDoctorsByDepartment(id);
	}

	@GetMapping("/patients-list")
	public @ResponseBody List<UserDTO> getPatientsList(Model model) {
		return userService.getAllPatients();
	}

	@GetMapping("/admins")
	public String getAdmins(Model model) {
		model.addAttribute("admins", userService.getAllAdmins());
		return "admins";
	}

	@GetMapping("/doctors")
	public String getDoctors(Model model) {
		model.addAttribute("doctors", userService.getAllDoctors());
		model.addAttribute("specializations", specializationRepository.findAll());
		return "doctors";
	}

	@GetMapping("/patients")
	public String getPatients(Model model) {
		model.addAttribute("patients", userService.getAllPatients());
		model.addAttribute("specializations", specializationRepository.findAll());
		return "patients";
	}

}
