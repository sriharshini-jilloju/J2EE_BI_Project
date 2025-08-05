package org.digital.online.ejb.repositories;

import java.util.Optional;

import org.digital.online.ejb.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String role);

}
