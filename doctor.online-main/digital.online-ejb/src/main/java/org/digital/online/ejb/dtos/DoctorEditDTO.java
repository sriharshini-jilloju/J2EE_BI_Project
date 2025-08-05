package org.digital.online.ejb.dtos;

import java.util.List;

import org.digital.online.ejb.entities.User;



public class DoctorEditDTO {
	public User user;
	public Long specialization;
	public List<DoctorSlotDTO> doctorSlots;
}
