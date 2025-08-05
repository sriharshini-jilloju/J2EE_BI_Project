package org.digital.online.ejb.services;

import java.util.List;

import org.digital.online.ejb.dtos.DoctorSlotDTO;
import org.digital.online.ejb.dtos.UserDTO;
import org.digital.online.ejb.entities.DoctorAvailabilitySlot;
import org.digital.online.ejb.entities.DoctorDetails;
import org.digital.online.ejb.entities.User;

public interface UserService {

	UserDTO createUser(UserDTO userDTO);

	UserDTO getUser(Long id);

	List<UserDTO> getAllAdmins();

	List<UserDTO> getAllDoctors();

	List<UserDTO> getAllPatients();

	void deleteUser(Long id);

	boolean updateUser(Long id, User user);
	
	List<DoctorAvailabilitySlot> getSlotsFromDTO(List<DoctorSlotDTO> availabilitySlots,DoctorDetails doctorDetails);

	List<UserDTO> getDoctorsByDepartment(Long id);
}
