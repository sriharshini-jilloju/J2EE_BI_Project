package org.digital.online.ejb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppointmentRequestDTO {

	@JsonProperty("isForSelf")
	private boolean isForSelf;
	private String appointmentType;

	private Long departmentId;
	private String departmentName;

	private Long doctorId; // nullable for instant
	private String doctorName; // nullable for instant

	private String appointmentDate; // nullable for instant
	private String appointmentTime; // nullable for instant

	private String patientName;
	private String patientPhone;
	private String patientEmail;
	private Integer patientAge;
	private String patientGender;

	private String emergencyContact;
	private String symptoms;

	private String requestedDate; // only for instant
	private String confirmedDate; // only for instant
	private String priority;

	public AppointmentRequestDTO() {
	}


	@JsonProperty("isForSelf")
	public boolean isForSelf() {
		return isForSelf;
	}

	@JsonProperty("isForSelf")
	public void setForSelf(boolean isForSelf) {
		this.isForSelf = isForSelf;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientPhone() {
		return patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public String getPatientEmail() {
		return patientEmail;
	}

	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}
	
	

	public String getConfirmedDate() {
		return confirmedDate;
	}


	public void setConfirmedDate(String confirmedDate) {
		this.confirmedDate = confirmedDate;
	}


	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
}
