package org.digital.online.ejb.services.impl;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import org.digital.online.ejb.dtos.PrescriptionDTO;
import org.digital.online.ejb.entities.Appointment;
import org.digital.online.ejb.entities.Prescription;
import org.digital.online.ejb.enums.AppointmentStatus;
import org.digital.online.ejb.repositories.AppointmentRepository;
import org.digital.online.ejb.repositories.PrescriptionRepository;
import org.digital.online.ejb.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	PrescriptionRepository prescriptionRepository;

	@Override
	public ResponseEntity<String> savePrescription(PrescriptionDTO dto) {
		try {
			Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
					.orElseThrow(() -> new RuntimeException("Appointment not found"));

			Prescription prescription = new Prescription();
			prescription.setAppointment(appointment);
			prescription.setDiagnosis(dto.getDiagnosis());
			prescription.setSymptoms(dto.getSymptoms());
			prescription.setVisitType(Prescription.VisitType.valueOf(dto.getVisitType().toUpperCase()));
			prescription.setFollowupDate(dto.getFollowupDate());

			if (dto.getPriority() != null && !dto.getPriority().isEmpty()) {
				prescription.setPriority(Prescription.Priority.valueOf(dto.getPriority().toUpperCase()));
			}

			prescription.setGeneralInstructions(dto.getGeneralInstructions());
			prescription.setNextVisitInstructions(dto.getNextVisitInstructions());

			// Convert medication DTOs to medication strings
			prescription
					.setMedications(dto
							.getMedications().stream().map(m -> m.getName() + " | " + m.getDosage() + " | "
									+ m.getFrequency() + " | " + m.getDuration() + " | " + m.getInstruction())
							.collect(Collectors.toList()));

			prescriptionRepository.save(prescription);
			appointment.setStatus(AppointmentStatus.COMPLETED);
			appointment.setCompletedAt(LocalDateTime.now());
			appointmentRepository.save(appointment);
			return ResponseEntity.ok("Prescription saved successfully and appointment marked as completed.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
	}

	@Override
	public void generatePrescriptionPdf(Long appointmentId, HttpServletResponse response) throws Exception {
		Optional<Prescription> optional = prescriptionRepository.findByAppointmentId(appointmentId);
		if (!optional.isPresent()) {
			throw new RuntimeException("Prescription not found for appointment ID: " + appointmentId);
		}

		Prescription prescription = optional.get();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=prescription_" + appointmentId + ".pdf");

		Document document = new Document();
		OutputStream out = response.getOutputStream();
		PdfWriter.getInstance(document, out);
		document.open();

		// === Add styled text logo instead of image ===
		Font logoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
		Paragraph logoText = new Paragraph("doctor.online", logoFont);
		logoText.setSpacingAfter(10f);
		document.add(logoText);

		document.add(Chunk.NEWLINE); // spacer

		Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
		Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
		Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

		document.add(new Paragraph("Prescription Summary", headingFont));
		document.add(Chunk.NEWLINE);

		Appointment appt = prescription.getAppointment();

		document.add(new Paragraph("Patient Name: " + appt.getPatientName(), textFont));
		document.add(
				new Paragraph("Age / Gender: " + appt.getPatientAge() + " / " + appt.getPatientGender(), textFont));
		document.add(new Paragraph("Email: " + appt.getPatientEmail(), textFont));
		document.add(new Paragraph("Phone: " + appt.getPatientPhone(), textFont));
		document.add(new Paragraph("Date: " + appt.getDate(), textFont));
		document.add(new Paragraph("Symptoms: " + appt.getSymptoms(), textFont));
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("Diagnosis", labelFont));
		document.add(new Paragraph(prescription.getDiagnosis(), textFont));
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("Visit Type: " + prescription.getVisitType(), textFont));
		document.add(new Paragraph("Priority: " + prescription.getPriority(), textFont));
		document.add(new Paragraph("Follow-up Date: " + prescription.getFollowupDate(), textFont));
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("Medications", labelFont));
		for (String med : prescription.getMedications()) {
			document.add(new Paragraph("• " + med, textFont));
		}

		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("General Instructions", labelFont));
		document.add(new Paragraph(prescription.getGeneralInstructions(), textFont));

		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Next Visit Instructions", labelFont));
		document.add(new Paragraph(prescription.getNextVisitInstructions(), textFont));

		document.close();
		out.flush();
		out.close();
	}

}
