package org.digital.online.ejb.tools;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.digital.online.ejb.entities.DoctorAvailabilitySlot;

public class SlotGenerator {

	public static List<LocalTime> generateHalfHourSlots(LocalDate date, List<DoctorAvailabilitySlot> allSlotsForDoctor) {
		DayOfWeek targetDay = date.getDayOfWeek();

		// Filter slots matching the given date's day
		List<DoctorAvailabilitySlot> slotsForThatDay = allSlotsForDoctor.stream()
				.filter(slot -> slot.getDayOfWeek() == targetDay)
				.collect(Collectors.toList());

		List<LocalTime> halfHourSlots = new ArrayList<>();

		for (DoctorAvailabilitySlot slot : slotsForThatDay) {
			LocalTime start = slot.getStartTime();
			LocalTime end = slot.getEndTime();

			while (!start.plusMinutes(30).isAfter(end)) {
				halfHourSlots.add(start);
				start = start.plusMinutes(30);
			}
		}

		return halfHourSlots;
	}
}
