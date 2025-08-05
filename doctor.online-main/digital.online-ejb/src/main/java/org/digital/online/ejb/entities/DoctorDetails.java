package org.digital.online.ejb.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_doctor_details")
public class DoctorDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "specialization_id")
	private Specialization specialization;

	@ElementCollection
	@CollectionTable(name = "doctor_qualifications", joinColumns = @JoinColumn(name = "doctor_id"))
	@Column(name = "qualification", length = 1000)
	private List<String> qualifications;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "doctorDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorAvailabilitySlot> availabilitySlots;

	public List<DoctorAvailabilitySlot> getAvailabilitySlots() {
		return availabilitySlots;
	}

	public void setAvailabilitySlots(List<DoctorAvailabilitySlot> availabilitySlots) {
		this.availabilitySlots = availabilitySlots;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	public List<String> getQualifications() {
		return qualifications;
	}

	public void setQualifications(List<String> qualifications) {
		this.qualifications = qualifications;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DoctorDetails() {
		super();
	}

}
