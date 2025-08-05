package org.digital.online.ejb.repositories;

import java.util.List;

import org.digital.online.ejb.entities.DoctorAvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorAvailabilitySlotsRepository extends JpaRepository<DoctorAvailabilitySlot, Long>{

	@Query("SELECT s FROM DoctorAvailabilitySlot s WHERE s.doctorDetails.user.id = :doctorId")
	List<DoctorAvailabilitySlot> findByDoctorUserId(@Param("doctorId") Long doctorId);


}
