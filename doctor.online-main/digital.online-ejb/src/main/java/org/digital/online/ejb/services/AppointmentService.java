package org.digital.online.ejb.services;

import java.time.LocalDate;
import java.util.List;

import org.digital.online.ejb.dtos.AppointmentDetailsDTO;
import org.digital.online.ejb.dtos.AppointmentRequestDTO;
import org.digital.online.ejb.dtos.SlotDTO;
import org.digital.online.ejb.entities.Appointment;

public interface AppointmentService {

	String saveInstantAppointment(AppointmentRequestDTO appointmentRequest);
	
	String saveScheduledAppointment(AppointmentRequestDTO dto);

	SlotDTO getAvailableSlots(Long doctorId, LocalDate date);

	List<Appointment> getAwaitingList();

	void deleteAppointment(Long id);

	AppointmentDetailsDTO getAppointmentById(Long id);

	List<Appointment> getConfirmedList();

	List<Appointment> getCompletedList();

	List<Appointment> getExpiredList();

	List<Appointment> getScheduledAppointments();

	List<Appointment> getRequestList();

	void confirmAppointment(Long id) throws Exception;

}
