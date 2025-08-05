package org.digital.online.ejb.repositories;

import java.util.List;
import java.util.Optional;

import org.digital.online.ejb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.role.name = 'ADMIN'")
	List<User> findAllAdmins();

	@Query("SELECT u FROM User u WHERE u.role.name = 'DOCTOR'")
	List<User> findAllDoctors();

	@Query("SELECT u FROM User u WHERE u.role.name = 'PATIENT'")
	List<User> findAllPatients();
	
	@Query("SELECT u FROM User u JOIN u.doctorDetails d WHERE d.specialization.id = :specializationId")
    List<User> findDoctorsBySpecializationId(@Param("specializationId") Long specializationId);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Object findByEmail(String patientEmail);

}
