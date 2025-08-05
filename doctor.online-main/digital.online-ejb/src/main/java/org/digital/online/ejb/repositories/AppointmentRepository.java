package org.digital.online.ejb.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.digital.online.ejb.entities.Appointment;
import org.digital.online.ejb.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

	@Query("SELECT a.startTime FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date = :date")
	List<LocalTime> findBookedStartTimesByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

	List<Appointment> findByStatusAndPatientIdOrderByIdAsc(AppointmentStatus status, Long patientId);
	
	@Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date = :date and status=:status")
	List<Appointment> findAppointmentsByDoctorAndDateAndStatus(@Param("doctorId") Long doctorId, @Param("date") LocalDate date,@Param("status") AppointmentStatus status);


}
