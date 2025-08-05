$(document).ready(function() {
	console.log("<<Awaiting Javascript>>");
	$('#requests-table').DataTable({
		paging: true,
		searching: true,
		info: true,
		processing: true,
		order: [[0, 'desc']]
	});

	$("#loader").hide();
	$("#table-container").show();


	let confirmAppointmentId = null;
	let appointmentId = null;
	$('#requests-table').on('click', '.btn-confirm', function(e) {
		e.preventDefault();
		confirmAppointmentId = $(this).data('id');
		$('#confirmDeleteModal').modal('show');
	});
	
	$('#requests-table').on('click', '.prescription-btn', function(e) {
		e.preventDefault();
		appointmentId = $(this).data('id');
		if (!$(e.target).closest('.action-buttons a').length) {
			$.ajax({
				url: `/prescriptions/download/${appointmentId}`,
				type: 'POST',
				success: function() {
					toastr.success('Prescription Downloaded');
				},
				error: function() {
					toastr.error('Failed to download prescription');
				}
			});
		}
	});

	$('#confirmBtn').on('click', function(e) {
		if (!confirmAppointmentId) return;
		if (!$(e.target).closest('.action-buttons a').length) {
		$.ajax({
			url: `/appointments/confirm/${confirmAppointmentId}`,
			type: 'POST',
			success: function() {
				$('#confirmDeleteModal').modal('hide');
				toastr.success('Appointment Confirmed');
				setTimeout(function() {
					location.reload();
				}, 300);
			},
			error: function() {
				$('#confirmDeleteModal').modal('hide');
				toastr.error('Failed to delete doctor');
			}
		});
		}
	});

	$('#requests-table').on('click', '.clickable-row', function(e) {
		e.preventDefault();
		const appointmentId = $(this).data('id');
		if (!$(e.target).closest('.action-buttons a').length) {
		viewAppointmentDetails(appointmentId);
		}
	});

});

function viewAppointmentDetails(appointmentId) {
	$.ajax({
		url: `/appointments/${appointmentId}`,
		method: 'GET',
		success: function(data) {
			$('#appt-id').text(data.id);
			$('#appt-type').text(data.instant ? 'Instant' : 'Scheduled');
			$('#appt-status').text(data.status);
			$('#appt-date').text(data.date || '—');
			$('#appt-time-slot').text(`${data.startTime || '--'} to ${data.endTime || '--'}`);

			$('#appt-doctor').text(data.doctorName || '—');
			$('#appt-self-other').text(data.forSelf ? 'Self' : 'Someone Else');

			// Conditional patient info
			if (data.forSelf) {
				$('#appt-patient').text(data.patientName || '—');
				$('#appt-contact').text(`${data.patientPhone || '—'}, ${data.patientEmail || '—'}`);
				$('#appt-gender-age').text(`${data.patientGender || '—'}, ${data.patientAge || '—'}`);
			} else {
				$('#appt-patient').text(data.patientName || '—');
				$('#appt-contact').text(`${data.patientPhone || '—'}, ${data.patientEmail || '—'}`);
				$('#appt-gender-age').text(`${data.patientGender || '—'}, ${data.patientAge || '—'}`);
			}

			$('#appt-symptoms').text(data.symptoms || '—');
			$('#appt-requested').text(data.requestedAt || '—');
			$('#appt-confirmed').text(data.confirmedAt || '—');
			$('#appt-completed').text(data.completedAt || '—');

			$('#appt-link').html(data.sessionLink ? `<a href="${data.sessionLink}" target="_blank">Join Session</a>` : '—');
			// Show modal
			$('#appointmentDetailsModal').modal('show');
		},
		error: function() {
			toastr.error('Failed to load appointment details.');
		}
	});
}
