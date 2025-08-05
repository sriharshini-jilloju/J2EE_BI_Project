package org.digital.online.ejb.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_specialization")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "specialization")
    private List<DoctorDetails> doctors;

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

    public List<DoctorDetails> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorDetails> doctors) {
        this.doctors = doctors;
    }
}
