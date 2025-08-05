package org.digital.online.ejb.dtos;

import java.time.LocalDate;
import java.util.List;

public class PrescriptionDTO {
	private Long appointmentId;
	private String diagnosis;
	private String symptoms;
	private String visitType;
	private LocalDate followupDate;
	private String priority;
	private String generalInstructions;
	private String nextVisitInstructions;

	private List<MedicationDTO> medications;

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public LocalDate getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(LocalDate followupDate) {
		this.followupDate = followupDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getGeneralInstructions() {
		return generalInstructions;
	}

	public void setGeneralInstructions(String generalInstructions) {
		this.generalInstructions = generalInstructions;
	}

	public String getNextVisitInstructions() {
		return nextVisitInstructions;
	}

	public void setNextVisitInstructions(String nextVisitInstructions) {
		this.nextVisitInstructions = nextVisitInstructions;
	}

	public List<MedicationDTO> getMedications() {
		return medications;
	}

	public void setMedications(List<MedicationDTO> medications) {
		this.medications = medications;
	}

	public PrescriptionDTO() {
		super();
	}

}
