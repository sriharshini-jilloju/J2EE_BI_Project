package org.digital.online.ejb.repositories;

import org.digital.online.ejb.entities.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDetailsRepository extends JpaRepository<DoctorDetails, Long>{

}
