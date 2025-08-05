package org.digital.online.ejb.entities;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor_availability_slots")
public class DoctorAvailabilitySlot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "doctor_details_id")
	private DoctorDetails doctorDetails;

	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	private LocalTime startTime;
	private LocalTime endTime;

	public DoctorAvailabilitySlot(DoctorDetails doctorDetails, DayOfWeek dayOfWeek, LocalTime startTime,
			LocalTime endTime) {
		super();
		this.doctorDetails = doctorDetails;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DoctorDetails getDoctorDetails() {
		return doctorDetails;
	}

	public void setDoctorDetails(DoctorDetails doctorDetails) {
		this.doctorDetails = doctorDetails;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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

	public DoctorAvailabilitySlot() {
		super();
	}

}
