package org.digital.online.ejb.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.digital.online.ejb.dtos.AppointmentDetailsDTO;
import org.digital.online.ejb.dtos.AppointmentRequestDTO;
import org.digital.online.ejb.dtos.SlotDTO;
import org.digital.online.ejb.entities.Appointment;
import org.digital.online.ejb.entities.DoctorAvailabilitySlot;
import org.digital.online.ejb.entities.User;
import org.digital.online.ejb.enums.AppointmentStatus;
import org.digital.online.ejb.repositories.AppointmentRepository;
import org.digital.online.ejb.repositories.DoctorAvailabilitySlotsRepository;
import org.digital.online.ejb.repositories.UserRepository;
import org.digital.online.ejb.security.ContextLoader;
import org.digital.online.ejb.services.AppointmentService;
import org.digital.online.ejb.tools.SlotGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DoctorAvailabilitySlotsRepository doctorAvailabilitySlotsRepo;

	@Override
	public String saveInstantAppointment(AppointmentRequestDTO dto) {
		Appointment appointment = new Appointment();

		appointment.setInstant(true);
		appointment.setStatus(AppointmentStatus.REQUESTED);
		appointment.setRequestedAt(LocalDateTime.now());
		appointment.setSymptoms(dto.getSymptoms());
		appointment.setForSelf(dto.isForSelf());

		User patientUser = userRepository.findById(ContextLoader.getLoggedInUser().getUser().getId())
				.orElseThrow(() -> new RuntimeException("User not found"));
		appointment.setPatient(patientUser);

		if (!dto.isForSelf()) {
			appointment.setPatientName(dto.getPatientName());
			appointment.setPatientEmail(dto.getPatientEmail());
			appointment.setPatientPhone(dto.getPatientPhone());
			appointment.setPatientAge(dto.getPatientAge());
			appointment.setPatientGender(dto.getPatientGender());
		} else {
			appointment.setPatientName(patientUser.getName());
			appointment.setPatientEmail(patientUser.getEmail());
			// appointment.setPatientPhone(patientUser.getPhone());
			appointment.setPatientAge(patientUser.getPatientDetails().getAge());
			appointment.setPatientGender(patientUser.getPatientDetails().getGender());
		}

		appointmentRepository.save(appointment);
		return "Instant appointment request submitted successfully!";
	}

	@Override
	public String saveScheduledAppointment(AppointmentRequestDTO dto) {
		Appointment appointment = new Appointment();

		appointment.setInstant(false);
		appointment.setStatus(AppointmentStatus.REQUESTED);
		appointment.setRequestedAt(LocalDateTime.now());
		appointment.setDate(LocalDate.parse(dto.getAppointmentDate()));
		LocalTime startTime = LocalTime.parse(dto.getAppointmentTime());
		appointment.setStartTime(startTime);
		appointment.setEndTime(startTime.plusMinutes(30));
		appointment.setSymptoms(dto.getSymptoms());
		appointment.setForSelf(dto.isForSelf());

		// Link doctor
		if (dto.getDoctorId() != null) {
			userRepository.findById(dto.getDoctorId()).ifPresent(appointment::setDoctor);
		}

		// Always link user as patient
		User patientUser = userRepository.findById(ContextLoader.getLoggedInUser().getUser().getId())
				.orElseThrow(() -> new RuntimeException("User not found"));
		appointment.setPatient(patientUser);

		if (!dto.isForSelf()) {
			appointment.setPatientName(dto.getPatientName());
			appointment.setPatientEmail(dto.getPatientEmail());
			appointment.setPatientPhone(dto.getPatientPhone());
			appointment.setPatientAge(dto.getPatientAge());
			appointment.setPatientGender(dto.getPatientGender());
		} else {
			appointment.setPatientName(patientUser.getName());
			appointment.setPatientEmail(patientUser.getEmail());
			// appointment.setPatientPhone(patientUser.getPhone());
			appointment.setPatientAge(patientUser.getPatientDetails().getAge());
			appointment.setPatientGender(patientUser.getPatientDetails().getGender());
		}

		appointmentRepository.save(appointment);
		return "Scheduled appointment request submitted successfully!";
	}

	@Override
	public SlotDTO getAvailableSlots(Long doctorId, LocalDate date) {
		List<DoctorAvailabilitySlot> doctorAvailabilitySlots = doctorAvailabilitySlotsRepo.findByDoctorUserId(doctorId);

		List<LocalTime> allSlotsForDay = SlotGenerator.generateHalfHourSlots(date, doctorAvailabilitySlots);

		// Filter out past time slots if the date is today
		if (date.equals(LocalDate.now())) {
			LocalTime now = LocalTime.now();
			allSlotsForDay = allSlotsForDay.stream().filter(slot -> !slot.isBefore(now)).collect(Collectors.toList());
		}

		List<LocalTime> bookedSlotsForDay = appointmentRepository.findBookedStartTimesByDoctorAndDate(doctorId, date);

		return new SlotDTO(allSlotsForDay.stream().map(LocalTime::toString).collect(Collectors.toList()),
				bookedSlotsForDay.stream().map(LocalTime::toString).collect(Collectors.toList()));
	}

	@Override
	public List<Appointment> getAwaitingList() {
		return appointmentRepository.findByStatusAndPatientIdOrderByIdAsc(AppointmentStatus.REQUESTED,
				ContextLoader.getLoggedInUser().getUser().getId());
	}

	@Override
	public List<Appointment> getConfirmedList() {
		return appointmentRepository.findByStatusAndPatientIdOrderByIdAsc(AppointmentStatus.CONFIRMED,
				ContextLoader.getLoggedInUser().getUser().getId());
	}

	@Override
	public List<Appointment> getCompletedList() {
		return appointmentRepository.findByStatusAndPatientIdOrderByIdAsc(AppointmentStatus.COMPLETED,
				ContextLoader.getLoggedInUser().getUser().getId());
	}

	@Override
	public List<Appointment> getExpiredList() {
		return appointmentRepository.findByStatusAndPatientIdOrderByIdAsc(AppointmentStatus.EXPIRED,
				ContextLoader.getLoggedInUser().getUser().getId());
	}

	@Override
	public void deleteAppointment(Long id) {
		appointmentRepository.deleteById(id);
	}

	@Override
	public AppointmentDetailsDTO getAppointmentById(Long id) {
		return convertToDto(appointmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Appointment with ID " + id + " not found")));
	}

	public AppointmentDetailsDTO convertToDto(Appointment appointment) {
		AppointmentDetailsDTO dto = new AppointmentDetailsDTO();
		dto.setId(appointment.getId());
		dto.setDoctorName(appointment.getDoctor() != null ? appointment.getDoctor().getName() : "N/A");

		if (appointment.isForSelf() && appointment.getPatient() != null) {
			User user = appointment.getPatient();
			dto.setPatientName(user.getName());
			dto.setPatientEmail(user.getEmail());
			// dto.setPatientPhone(user.getPatientDetails() != null ?
			// user.getPatientDetails().getPhone() : null);
			dto.setPatientGender(user.getPatientDetails() != null ? user.getPatientDetails().getGender() : null);
			dto.setPatientAge(user.getPatientDetails() != null ? user.getPatientDetails().getAge() : null);
		} else {
			dto.setPatientName(appointment.getPatientName());
			dto.setPatientEmail(appointment.getPatientEmail());
			dto.setPatientPhone(appointment.getPatientPhone());
			dto.setPatientGender(appointment.getPatientGender());
			dto.setPatientAge(appointment.getPatientAge());
		}

		dto.setSymptoms(appointment.getSymptoms());
		dto.setInstant(appointment.isInstant());
		dto.setStatus(appointment.getStatus());
		dto.setDate(appointment.getDate());
		dto.setStartTime(appointment.getStartTime());
		dto.setEndTime(appointment.getEndTime());
		dto.setRequestedAt(appointment.getRequestedAt());
		dto.setConfirmedAt(appointment.getConfirmedAt());
		dto.setCompletedAt(appointment.getCompletedAt());
		dto.setSessionLink(appointment.getSessionLink());
		dto.setForSelf(appointment.isForSelf());

		return dto;
	}

	@Override
	public List<Appointment> getScheduledAppointments() {
		return appointmentRepository.findAppointmentsByDoctorAndDateAndStatus(
				ContextLoader.getLoggedInUser().getUser().getId(), LocalDate.now(), AppointmentStatus.CONFIRMED);
	}

	@Override
	public List<Appointment> getRequestList() {
		return appointmentRepository.findAppointmentsByDoctorAndDateAndStatus(
				ContextLoader.getLoggedInUser().getUser().getId(), LocalDate.now(), AppointmentStatus.REQUESTED);
	}

	@Override
	public void confirmAppointment(Long id) throws Exception {
		Optional<Appointment> appointOpt = appointmentRepository.findById(id);
		if (appointOpt.isEmpty()) {
			throw new Exception("Appointment not exist");
		}
		Appointment appointment = appointOpt.get();
		appointment.setStatus(AppointmentStatus.CONFIRMED);
		appointment.setConfirmedAt(LocalDateTime.now());
		appointmentRepository.save(appointment);
	}

}
