package org.digital.online.ejb.dtos;

public class UserDTO {
	public Long id;
	public String name;
	public String email;
	public String username;
	public String password;
	public String role; // ADMIN, DOCTOR, PATIENT

	public DoctorDetailsDTO doctorDetails;
	public PatientDetailsDTO patientDetails;
}

