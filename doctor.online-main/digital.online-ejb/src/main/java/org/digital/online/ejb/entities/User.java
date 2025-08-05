package org.digital.online.ejb.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	@Column(nullable = false, unique = true)
	private String username;

	private String password;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "userid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roleid", referencedColumnName = "id"))
	private Role role;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
	private DoctorDetails doctorDetails;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private PatientDetails patientDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public DoctorDetails getDoctorDetails() {
		return doctorDetails;
	}

	public void setDoctorDetails(DoctorDetails doctorDetails) {
		this.doctorDetails = doctorDetails;
	}

	public PatientDetails getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(PatientDetails patientDetails) {
		this.patientDetails = patientDetails;
	}

	public User() {
		super();
	}

}
