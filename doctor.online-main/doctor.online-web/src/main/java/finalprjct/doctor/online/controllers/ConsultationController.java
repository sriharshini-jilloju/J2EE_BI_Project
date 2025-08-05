package finalprjct.doctor.online.controllers;

import java.time.LocalDate;

import org.digital.online.ejb.dtos.AppointmentDetailsDTO;
import org.digital.online.ejb.dtos.AppointmentRequestDTO;
import org.digital.online.ejb.dtos.SlotDTO;
import org.digital.online.ejb.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/appointments")
public class ConsultationController {

	@Autowired
	AppointmentService appointmentService;

	@GetMapping("/book-appointment")
	public String getBookAppointmentTemplate() {
		return "book-appointment";
	}

	@PostMapping("/book-instant")
	public @ResponseBody String bookInstantAppointment(@RequestBody AppointmentRequestDTO appointmentRequest)
			throws Exception {
		if (!"instant".equalsIgnoreCase(appointmentRequest.getAppointmentType())) {
			throw new Exception("Invalid appointment type for this endpoint");
		}
		return appointmentService.saveInstantAppointment(appointmentRequest);
	}

	@PostMapping("/book-scheduled")
	public @ResponseBody String bookScheduledAppointment(@RequestBody AppointmentRequestDTO appointmentRequest)
			throws Exception {
		if (!"scheduled".equalsIgnoreCase(appointmentRequest.getAppointmentType())) {
			throw new Exception("Invalid appointment type for this endpoint");
		}
		return appointmentService.saveScheduledAppointment(appointmentRequest);
	}

	@GetMapping("/slots")
	public @ResponseBody SlotDTO getSlots(@RequestParam Long doctorId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return appointmentService.getAvailableSlots(doctorId, date);
	}

	@GetMapping("/awaiting")
	public String getAwaitingList(Model model) {
		model.addAttribute("appointments", appointmentService.getAwaitingList());
		model.addAttribute("activeTab", "Awaiting");
		return "consultations";
	}

	@GetMapping("/confirmed")
	public String getConfirmedList(Model model) {
		model.addAttribute("appointments", appointmentService.getConfirmedList());
		model.addAttribute("activeTab", "Confirmed");
		return "consultations";
	}

	@GetMapping("/completed")
	public String getCompletedList(Model model) {
		model.addAttribute("appointments", appointmentService.getCompletedList());
		model.addAttribute("activeTab", "Completed");
		return "consultations";
	}

	@GetMapping("/expired")
	public String getExpiredList(Model model) {
		model.addAttribute("appointments", appointmentService.getExpiredList());
		model.addAttribute("activeTab", "Expired");
		return "consultations";
	}

	@PostMapping("/delete/{id}")
	public @ResponseBody void deleteAppointment(@PathVariable Long id) {
		appointmentService.deleteAppointment(id);
	}
	
	@PostMapping("/confirm/{id}")
	public @ResponseBody void confirmAppointment(@PathVariable Long id) throws Exception {
		appointmentService.confirmAppointment(id);
	}

	@GetMapping("/{id}")
	public @ResponseBody AppointmentDetailsDTO getAppointment(@PathVariable Long id) {
		return appointmentService.getAppointmentById(id);
	}

}
