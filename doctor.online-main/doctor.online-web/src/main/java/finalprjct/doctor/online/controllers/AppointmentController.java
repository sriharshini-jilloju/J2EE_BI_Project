package finalprjct.doctor.online.controllers;

import org.digital.online.ejb.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctors")
public class AppointmentController {
	
	@Autowired
	AppointmentService appointmentService;
	
	@GetMapping("/scheduled")
	public String getDoctorAppointmentsTemplate(Model model) {
		model.addAttribute("scheduledAppointments", appointmentService.getScheduledAppointments());
		return "scheduled";
	}
	
	@GetMapping("/requests")
	public String getAwaitingList(Model model){
		model.addAttribute("appointments", appointmentService.getRequestList());
		model.addAttribute("activeTab", "Requests");
		return "appointments";
	}

}
