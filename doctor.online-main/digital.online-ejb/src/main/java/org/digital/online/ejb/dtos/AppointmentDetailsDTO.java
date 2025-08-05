package org.digital.online.ejb.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.digital.online.ejb.enums.AppointmentStatus;

import com.fasterxml.jackson.annotation.JsonFormat;




public class AppointmentDetailsDTO {

	private Long id;

	private String doctorName;
	private String patientName;
	private String patientEmail;
	private String patientPhone;
	private Integer patientAge;
	private String patientGender;

	private String symptoms;
	private boolean isInstant;

	private AppointmentStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy")
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy")
	private LocalDateTime requestedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy")
	private LocalDateTime confirmedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy")
	private LocalDateTime completedAt;

	private String sessionLink;
	private boolean isForSelf;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientEmail() {
		return patientEmail;
	}

	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}

	public String getPatientPhone() {
		return patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
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

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public boolean isInstant() {
		return isInstant;
	}

	public void setInstant(boolean isInstant) {
		this.isInstant = isInstant;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public LocalDateTime getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(LocalDateTime requestedAt) {
		this.requestedAt = requestedAt;
	}

	public LocalDateTime getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(LocalDateTime confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public String getSessionLink() {
		return sessionLink;
	}

	public void setSessionLink(String sessionLink) {
		this.sessionLink = sessionLink;
	}

	public boolean isForSelf() {
		return isForSelf;
	}

	public void setForSelf(boolean isForSelf) {
		this.isForSelf = isForSelf;
	}

}
