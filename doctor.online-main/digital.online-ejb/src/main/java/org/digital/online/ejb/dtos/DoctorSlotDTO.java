package org.digital.online.ejb.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorSlotDTO {
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;

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

	public DoctorSlotDTO() {
		super();
	}

	public DoctorSlotDTO(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	

}
