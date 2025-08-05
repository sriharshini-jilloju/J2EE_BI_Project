package org.digital.online.ejb.repositories;

import org.digital.online.ejb.entities.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDetailsRepository extends JpaRepository<PatientDetails, Long> {

}
