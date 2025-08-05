package org.digital.online.ejb.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String diagnosis;

    @Column(length = 1000)
    private String symptoms;

    @ElementCollection
    @CollectionTable(name = "prescription_medications", joinColumns = @JoinColumn(name = "prescription_id"))
    @Column(name = "medication")
    private List<String> medications;

    @Enumerated(EnumType.STRING)
    private VisitType visitType;

    private LocalDate followupDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(length = 1000)
    private String generalInstructions;

    @Column(length = 1000)
    private String nextVisitInstructions;

    public enum VisitType {
        CONSULTATION, OBSERVATION, HOSPITAL, FOLLOWUP
    }

    public enum Priority {
        ROUTINE, URGENT, EMERGENCY
    }

    // Getters and setters below

    public Long getId() {
        return id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public LocalDate getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(LocalDate followupDate) {
        this.followupDate = followupDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getGeneralInstructions() {
        return generalInstructions;
    }

    public void setGeneralInstructions(String generalInstructions) {
        this.generalInstructions = generalInstructions;
    }

    public String getNextVisitInstructions() {
        return nextVisitInstructions;
    }

    public void setNextVisitInstructions(String nextVisitInstructions) {
        this.nextVisitInstructions = nextVisitInstructions;
    }
}

