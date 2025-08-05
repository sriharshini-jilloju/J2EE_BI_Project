package finalprjct.doctor.online.controllers;


import org.digital.online.ejb.dtos.PrescriptionDTO;
import org.digital.online.ejb.repositories.AppointmentRepository;
import org.digital.online.ejb.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {
	
	@Autowired
	AppointmentRepository appointmentRepository;
	
	@Autowired
	PrescriptionService prescriptionService;

	@PostMapping("/save")
	public ResponseEntity<String> savePrescription(@RequestBody PrescriptionDTO dto) {
		return prescriptionService.savePrescription(dto);
	}
	
	@GetMapping("/download/{appointmentId}")
    public void downloadPrescription(@PathVariable Long appointmentId, HttpServletResponse response) {
        try {
            prescriptionService.generatePrescriptionPdf(appointmentId, response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
