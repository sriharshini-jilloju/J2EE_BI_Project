package org.digital.online.ejb.dtos;

import java.util.List;

public class SlotDTO {
    private List<String> timeSlots;   // e.g., ["09:00", "09:30"]
    private List<String> bookedSlots;

    public SlotDTO(List<String> timeSlots, List<String> bookedSlots) {
        this.timeSlots = timeSlots;
        this.bookedSlots = bookedSlots;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<String> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<String> getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(List<String> bookedSlots) {
        this.bookedSlots = bookedSlots;
    }
}

