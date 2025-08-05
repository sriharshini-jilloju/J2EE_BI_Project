package org.digital.online.ejb.dtos;

import java.util.List;

public class DoctorDetailsDTO {
	public Long specialization;
	public List<String> qualifications;
	public List<DoctorSlotDTO> availabilitySlots;
}

