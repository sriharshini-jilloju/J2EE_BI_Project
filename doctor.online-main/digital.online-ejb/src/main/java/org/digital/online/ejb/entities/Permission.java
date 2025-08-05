package org.digital.online.ejb.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_permission")
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	public Permission() {
		super();
	}

	public Permission(String name) {
		super();
		this.name = name;
	}

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

}
