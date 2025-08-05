package org.digital.online.ejb.services;


import org.digital.online.ejb.dtos.PrescriptionDTO;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

public interface PrescriptionService {
	
	ResponseEntity<String> savePrescription(PrescriptionDTO prescription);
	
	void generatePrescriptionPdf(Long appointmentId, HttpServletResponse response) throws Exception;

}
