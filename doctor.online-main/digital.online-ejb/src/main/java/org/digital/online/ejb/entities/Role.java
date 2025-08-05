package org.digital.online.ejb.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tbl_role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "perm_id", referencedColumnName = "id"))
	private List<Permission> permissions;

	public Role() {
		super();
	}

	public Role(String name, List<Permission> permissions) {
		super();
		this.name = name;
		this.permissions = permissions;
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

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

}
