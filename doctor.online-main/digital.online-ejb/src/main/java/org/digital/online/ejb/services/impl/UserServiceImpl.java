package org.digital.online.ejb.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.digital.online.ejb.dtos.DoctorDetailsDTO;
import org.digital.online.ejb.dtos.DoctorSlotDTO;
import org.digital.online.ejb.dtos.PatientDetailsDTO;
import org.digital.online.ejb.dtos.UserDTO;
import org.digital.online.ejb.entities.DoctorAvailabilitySlot;
import org.digital.online.ejb.entities.DoctorDetails;
import org.digital.online.ejb.entities.PatientDetails;
import org.digital.online.ejb.entities.Role;
import org.digital.online.ejb.entities.Specialization;
import org.digital.online.ejb.entities.User;
import org.digital.online.ejb.repositories.DoctorDetailsRepository;
import org.digital.online.ejb.repositories.RoleRepository;
import org.digital.online.ejb.repositories.SpecializationRepository;
import org.digital.online.ejb.repositories.UserRepository;
import org.digital.online.ejb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	SpecializationRepository specializationRepository;
	
	@Autowired
	DoctorDetailsRepository doctorDetailsRepository;

	@Override
	public UserDTO createUser(UserDTO userDTO) {
		User user = new User();
		user.setName(userDTO.name);
		user.setUsername(userDTO.username);
		user.setEmail(userDTO.email);
		user.setPassword(new BCryptPasswordEncoder().encode(userDTO.password));

		Role role = roleRepository.findByName(userDTO.role).orElseThrow(() -> new RuntimeException("Invalid role"));
		user.setRole(role);

		if ("DOCTOR".equalsIgnoreCase(userDTO.role)) {
			DoctorDetails details = new DoctorDetails();
			Optional<Specialization> specialization = specializationRepository
					.findById(userDTO.doctorDetails.specialization);
			details.setSpecialization(specialization.isPresent() ? specialization.get() : null);
			details.setQualifications(userDTO.doctorDetails.qualifications);
			details.setUser(user);
			List<DoctorAvailabilitySlot> doctorAvailabilitySlots = getSlotsFromDTO(
					userDTO.doctorDetails.availabilitySlots, details);
			details.setAvailabilitySlots(doctorAvailabilitySlots);
			user.setDoctorDetails(details);
		} else if ("PATIENT".equalsIgnoreCase(userDTO.role)) {
			PatientDetails details = new PatientDetails();
			details.setAge(userDTO.patientDetails.age);
			details.setGender(userDTO.patientDetails.gender);
			details.setBloodGroup(userDTO.patientDetails.bloodGroup);
			details.setUser(user);
			user.setPatientDetails(details);
		}

		userRepository.save(user);
		userDTO.id = user.getId();
		return userDTO;
	}

	@Override
	public UserDTO getUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		UserDTO dto = new UserDTO();
		dto.id = user.getId();
		dto.name = user.getName();
		dto.username = user.getUsername();
		dto.email = user.getEmail();
		dto.role = user.getRole().getName();

		if ("DOCTOR".equalsIgnoreCase(dto.role) && user.getDoctorDetails() != null) {
			DoctorDetailsDTO d = new DoctorDetailsDTO();
			d.specialization = user.getDoctorDetails().getSpecialization().getId();
			d.qualifications = user.getDoctorDetails().getQualifications();

			List<DoctorSlotDTO> slotsDTO = getDTOFromSlots(user.getDoctorDetails().getAvailabilitySlots());
			d.availabilitySlots = slotsDTO;
			dto.doctorDetails = d;
		}

		if ("PATIENT".equalsIgnoreCase(dto.role) && user.getPatientDetails() != null) {
			PatientDetailsDTO p = new PatientDetailsDTO();
			p.age = user.getPatientDetails().getAge();
			p.gender = user.getPatientDetails().getGender();
			p.bloodGroup = user.getPatientDetails().getBloodGroup();
			dto.patientDetails = p;
		}

		return dto;
	}

	@Override
	public List<UserDTO> getAllAdmins() {
		return userRepository.findAllAdmins().stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	@Override
	public List<UserDTO> getAllDoctors() {
		return userRepository.findAllDoctors().stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	@Override
	public List<UserDTO> getAllPatients() {
		return userRepository.findAllPatients().stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	
	@Override
	public List<UserDTO> getDoctorsByDepartment(Long id) {
		return userRepository.findDoctorsBySpecializationId(id).stream().map(this::mapToDTO).collect(Collectors.toList());
	}


	// Helper
	private UserDTO mapToDTO(User user) {
		UserDTO dto = new UserDTO();
		dto.id = user.getId();
		dto.name = user.getName();
		dto.username = user.getUsername();
		dto.email = user.getEmail();
		dto.role = user.getRole().getName();

		if ("DOCTOR".equalsIgnoreCase(dto.role) && user.getDoctorDetails() != null) {
			DoctorDetailsDTO d = new DoctorDetailsDTO();
			d.specialization = user.getDoctorDetails().getSpecialization().getId();
			d.qualifications = user.getDoctorDetails().getQualifications();
			List<DoctorSlotDTO> slotsDTO = getDTOFromSlots(user.getDoctorDetails().getAvailabilitySlots());
			d.availabilitySlots = slotsDTO;
			dto.doctorDetails = d;
		}

		if ("PATIENT".equalsIgnoreCase(dto.role) && user.getPatientDetails() != null) {
			PatientDetailsDTO p = new PatientDetailsDTO();
			p.age = user.getPatientDetails().getAge();
			p.gender = user.getPatientDetails().getGender();
			p.bloodGroup = user.getPatientDetails().getBloodGroup();
			dto.patientDetails = p;
		}

		return dto;
	}

	private List<DoctorSlotDTO> getDTOFromSlots(List<DoctorAvailabilitySlot> availabilitySlots) {
		List<DoctorSlotDTO> slotsDTO = new ArrayList<>();
		for (DoctorAvailabilitySlot slot : availabilitySlots) {
			slotsDTO.addFirst(new DoctorSlotDTO(slot.getDayOfWeek(), slot.getStartTime(), slot.getEndTime()));
		}
		return slotsDTO;
	}

	@Override
	public void deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("User with ID " + id + " not found");
		}
	}

	@Override
	@Transactional
	public boolean updateUser(Long id, User updatedUser) {
		Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isEmpty()) {
			return false;
		}

		User existingUser = optionalUser.get();

		// Update basic user fields
		existingUser.setName(updatedUser.getName());
		existingUser.setUsername(updatedUser.getUsername());
		existingUser.setEmail(updatedUser.getEmail());

		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			existingUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
		}

		// Update role if changed
		if (updatedUser.getRole() != null
				&& !existingUser.getRole().getName().equalsIgnoreCase(updatedUser.getRole().getName())) {

			Role newRole = roleRepository.findByName(updatedUser.getRole().getName())
					.orElseThrow(() -> new RuntimeException("Invalid role"));
			existingUser.setRole(newRole);
		}

		// Update Doctor or Patient details if role-specific info exists
//		if ("DOCTOR".equalsIgnoreCase(existingUser.getRole().getName())) {
//			 DoctorDetails doctorDetails = existingUser.getDoctorDetails();
//			if (existingUser.getDoctorDetails() == null) {
//				existingUser.setDoctorDetails(new DoctorDetails());
//				existingUser.getDoctorDetails().setUser(existingUser);
//			}
//
//			existingUser.getDoctorDetails().setSpecialization(updatedUser.getDoctorDetails().getSpecialization());
//			existingUser.getDoctorDetails().setQualifications(updatedUser.getDoctorDetails().getQualifications());
//
//			// Clear existing slots (if needed)
//			doctorDetails.getAvailabilitySlots().clear();
//
//			// Add updated slots
//			if (updatedUser.getDoctorDetails().getAvailabilitySlots() != null) {
//				for (DoctorAvailabilitySlot slot : updatedUser.getDoctorDetails().getAvailabilitySlots()) {
//					slot.setDoctorDetails(doctorDetails); // IMPORTANT
//					doctorDetails.getAvailabilitySlots().add(slot);
//				}
//			}
//			existingUser.setDoctorDetails(doctorDetails);
//
//		} 

		if ("DOCTOR".equalsIgnoreCase(existingUser.getRole().getName())) {
			DoctorDetails doctorDetails = existingUser.getDoctorDetails();

			if (doctorDetails == null) {
				doctorDetails = new DoctorDetails();
				doctorDetails.setUser(existingUser);
				existingUser.setDoctorDetails(doctorDetails);
			}

			// Update fields
			doctorDetails.setSpecialization(updatedUser.getDoctorDetails().getSpecialization());
			doctorDetails.setQualifications(updatedUser.getDoctorDetails().getQualifications());

			// Clear and update availability slots
			doctorDetails.getAvailabilitySlots().clear();

			if (updatedUser.getDoctorDetails().getAvailabilitySlots() != null) {
				for (DoctorAvailabilitySlot slot : updatedUser.getDoctorDetails().getAvailabilitySlots()) {
					slot.setDoctorDetails(doctorDetails); // very important
					doctorDetails.getAvailabilitySlots().add(slot);
				}
			}

			// You may need to explicitly save doctorDetails if not using cascade
			 doctorDetailsRepository.save(doctorDetails);
		}

		else if ("PATIENT".equalsIgnoreCase(existingUser.getRole().getName())) {
			if (existingUser.getPatientDetails() == null) {
				existingUser.setPatientDetails(new PatientDetails());
				existingUser.getPatientDetails().setUser(existingUser);
			}

			existingUser.getPatientDetails().setAge(updatedUser.getPatientDetails().getAge());
			existingUser.getPatientDetails().setGender(updatedUser.getPatientDetails().getGender());
			existingUser.getPatientDetails().setBloodGroup(updatedUser.getPatientDetails().getBloodGroup());
		}

		userRepository.save(existingUser);
		return true;
	}

	public List<DoctorAvailabilitySlot> getSlotsFromDTO(List<DoctorSlotDTO> availabilitySlots,
			DoctorDetails doctorDetails) {
		List<DoctorAvailabilitySlot> slots = new ArrayList<>();
		for (DoctorSlotDTO slotDTO : availabilitySlots) {
			slots.add(new DoctorAvailabilitySlot(doctorDetails, slotDTO.getDayOfWeek(), slotDTO.getStartTime(),
					slotDTO.getEndTime()));

		}
		return slots;
	}


}
