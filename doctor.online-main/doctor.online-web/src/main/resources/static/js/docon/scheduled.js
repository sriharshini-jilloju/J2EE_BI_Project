$(document).ready(function() {
	console.log("<<Scheduled Appointments>>");


	$("#backBtn").click(function() {
		$("#prescription").hide();
		$("#appointments").show();
	});

});

let medicationCount = 0;
const patientData = {
	'P003': { name: 'Michael Brown', age: 28, lastVisit: 'July 15, 2025' },
	'P004': { name: 'Sarah Wilson', age: 55, lastVisit: 'July 28, 2025' },
	'P005': { name: 'Robert Taylor', age: 62, lastVisit: 'July 20, 2025' }
};

// Appointment Management Functions
function approveAppointment(patientId) {
	showSuccess(`Appointment approved for patient ${patientId}. Patient has been notified via SMS and email.`);
	// Remove from pending appointments
	document.querySelector(`button[onclick="approveAppointment('${patientId}')"]`).closest('.col-md-6').remove();
}

function rejectAppointment(patientId) {
	const reason = prompt('Please provide a reason for rejection:');
	if (reason) {
		showSuccess(`Appointment rejected for patient ${patientId}. Patient notified with reason: "${reason}"`);
		document.querySelector(`button[onclick="rejectAppointment('${patientId}')"]`).closest('.col-md-6').remove();
	}
}

function startConsultation(appointmentId) {
	// Switch to prescription tab and load patient
	//document.querySelector('[data-bs-toggle="pill"][href="#prescription"]').click();
	$("#appointments").hide();
	$("#prescription").show();

	$.ajax({
	         url: '/appointments/' + appointmentId,
	         type: 'GET',
	         success: function(data) {
				$('#patientName').val(data.patientName);
			    $('#consultationDate').val(data.date);

				           // Info section
			    $('#patientAge').text(data.patientAge || "-");
				$('#patientBlood').text(data.bloodGroup || 'N/A'); // Assuming blood group not in response
				$('#patientGender').text(data.patientGender || "-");
			    $('#appointmentIdForPrescription').text(data.id); 
	         },
	         error: function(xhr) {
	             toastr.error('Could not fetch patient details.');
	         }
	     });
	
	
}

// Patient Info Functions
function loadPatientInfo() {
	const patientId = document.getElementById('patientSelect').value;
	const patientInfo = document.getElementById('patientInfo');

	if (patientId && patientData[patientId]) {
		const patient = patientData[patientId];
		//document.getElementById('patientName').textContent = patient.name;
		document.getElementById('patientAge').textContent = patient.age;
		document.getElementById('patientId').textContent = patientId;
		document.getElementById('lastVisit').textContent = patient.lastVisit;
		patientInfo.style.display = 'block';
	} else {
		patientInfo.style.display = 'none';
	}
}

// Medication Management
function addMedication() {
	medicationCount++;
	const medicationHtml = `
        <div class="medication-item p-3 mb-3" id="medication${medicationCount}">
            <div class="row">
                <div class="col-md-3">
                    <label class="form-label">Medication Name</label>
                    <input type="text" class="form-control" placeholder="e.g., Amoxicillin">
                </div>
                <div class="col-md-2">
                    <label class="form-label">Dosage</label>
                    <input type="text" class="form-control" placeholder="e.g., 500mg">
                </div>
                <div class="col-md-2">
                    <label class="form-label">Frequency</label>
                    <select class="form-select">
                        <option value="once">Once daily</option>
                        <option value="twice">Twice daily</option>
                        <option value="thrice">Three times daily</option>
                        <option value="four">Four times daily</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Duration</label>
                    <input type="text" class="form-control" placeholder="e.g., 7 days">
                </div>
                <div class="col-md-2">
                    <label class="form-label">Instructions</label>
                    <select class="form-select">
                        <option value="before">Before meals</option>
                        <option value="after">After meals</option>
                        <option value="with">With meals</option>
                        <option value="empty">Empty stomach</option>
                    </select>
                </div>
                <div class="col-md-1 d-flex align-items-end">
                    <button type="button" class="btn btn-outline-danger btn-sm" onclick="removeMedication(${medicationCount})">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </div>
        </div>
    `;
	document.getElementById('medicationsList').insertAdjacentHTML('beforeend', medicationHtml);
}

function removeMedication(id) {
	document.getElementById(`medication${id}`).remove();
}

// Visit Type Management
function toggleFollowup() {
	const visitType = document.getElementById('visitType').value;
	const followupDiv = document.getElementById('followupDateDiv');
	const priorityDiv = document.getElementById('priorityDiv');

	if (visitType === 'followup' || visitType === 'observation' || visitType === 'hospital') {
		followupDiv.style.display = 'block';
		if (visitType === 'hospital') {
			priorityDiv.style.display = 'block';
		} else {
			priorityDiv.style.display = 'none';
		}

		// Set default follow-up date (7 days from now)
		const followupDate = new Date();
		followupDate.setDate(followupDate.getDate() + 7);
		document.getElementById('followupDate').value = followupDate.toISOString().split('T')[0];
	} else {
		followupDiv.style.display = 'none';
		priorityDiv.style.display = 'none';
	}
}

/*// Form Actions
function savePrescription() {
	const patientId = document.getElementById('patientSelect').value;
	if (!patientId) {
		alert('Please select a patient first.');
		return;
	}

	const visitType = document.getElementById('visitType').value;
	let message = `Prescription saved successfully for patient ${patientId}.`;

	if (visitType === 'followup') {
		message += ' Follow-up appointment has been scheduled and patient will be notified.';
	} else if (visitType === 'hospital') {
		message += ' Hospital visit has been scheduled with high priority. Patient and hospital notified.';
	} else if (visitType === 'observation') {
		message += ' Observation period scheduled. Patient will receive monitoring instructions.';
	}

	if (document.getElementById('feedbackRequest').checked) {
		message += ' Patient feedback request will be sent after 24 hours.';
	}

	showSuccess(message);
}*/

function generatePrescription() {
	const patientId = document.getElementById('patientSelect').value;
	if (!patientId) {
		alert('Please select a patient first.');
		return;
	}
	showSuccess('Prescription PDF generated successfully. Document sent to printer and patient email.');
}

function scheduleFollowup() {
	const patientId = document.getElementById('patientSelect').value;
	if (!patientId) {
		alert('Please select a patient first.');
		return;
	}

	const followupDate = document.getElementById('followupDate').value;
	if (!followupDate) {
		alert('Please select a follow-up date.');
		return;
	}

	showSuccess(`Follow-up appointment scheduled for ${followupDate}. Patient will receive confirmation via SMS and email with automatic reminders.`);
}

function clearForm() {
	if (confirm('Are you sure you want to clear all form data?')) {
		document.getElementById('patientSelect').value = '';
		document.getElementById('patientInfo').style.display = 'none';
		document.getElementById('medicationsList').innerHTML = '';
		document.querySelectorAll('textarea, input[type="text"]').forEach(input => input.value = '');
		document.getElementById('visitType').value = 'consultation';
		toggleFollowup();
		medicationCount = 0;
	}
}

function showSuccess(message) {
	document.getElementById('successMessage').textContent = message;
	new bootstrap.Modal(document.getElementById('successModal')).show();
}

// Initialize with some default medications for demo
document.addEventListener('DOMContentLoaded', function() {
	// Set today's date
	const today = new Date().toISOString().split('T')[0];
	document.getElementById('consultationDate').value = today;
});


function savePrescription() {
	const diagnosis = document.querySelector('textarea[placeholder="Enter primary diagnosis..."]').value.trim();
	const symptoms = document.querySelector('textarea[placeholder="Patient symptoms and clinical observations..."]').value.trim();
	const visitType = document.getElementById('visitType').value;
	const followupDate = document.getElementById('followupDate').value;
	const priority = document.getElementById('priority')?.value || null;
	const generalInstructions = document.querySelector('textarea[placeholder="Diet, lifestyle, general care instructions..."]').value.trim();
	const nextVisitInstructions = document.querySelector('textarea[placeholder="Instructions for next visit or follow-up..."]').value.trim();
	const appointmentId = $("#appointmentIdForPrescription").text(); // assuming you store it when loading appointment data

	// Validate required fields
	if (!diagnosis) return toastr.warning("Diagnosis is required.");
	if (!symptoms) return toastr.warning("Symptoms & Observations are required.");
	if ((visitType === 'followup' || visitType === 'hospital' || visitType === 'observation') && !followupDate) {
		return toastr.warning("Follow-up date is required for selected visit type.");
	}
	if (visitType === 'hospital' && !priority) {
		return toastr.warning("Priority is required for hospital visits.");
	}

	// Medications validation
	const medications = [];
	let isMedicationValid = true;

	$("#medicationsList .medication-item").each(function () {
		const name = $(this).find('input[placeholder="e.g., Amoxicillin"]').val().trim();
		const dosage = $(this).find('input[placeholder="e.g., 500mg"]').val().trim();
		const frequency = $(this).find('select').eq(0).val();
		const duration = $(this).find('input[placeholder="e.g., 7 days"]').val().trim();
		const instruction = $(this).find('select').eq(1).val();

		if (!name || !dosage || !frequency || !duration || !instruction) {
			isMedicationValid = false;
			return false; // Break loop
		}

		medications.push({ name, dosage, frequency, duration, instruction });
	});

	if (!isMedicationValid) {
		toastr.warning("Please fill all fields in each medication.");
		return;
	}

	const payload = {
		appointmentId: appointmentId,
		diagnosis: diagnosis,
		symptoms: symptoms,
		visitType: visitType.toUpperCase(),
		followupDate: followupDate || null,
		priority: priority?.toUpperCase() || null,
		generalInstructions: generalInstructions,
		nextVisitInstructions: nextVisitInstructions,
		medications: medications
	};
	
	console.log(payload);

	$.ajax({
		url: "/prescriptions/save",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(payload),
		success: function (response) {
			console.log(response);
			$("#prescription").hide();
			$("#appointments").show();
			removeAppointmentCardById(appointmentId);
			toastr.success(response);
			updateAppointmentCount();
			
		},
		error: function (xhr) {
			console.error(xhr);
			toastr.error("An error occurred while saving the prescription. Please try again.");
		}
	});
}

function removeAppointmentCardById(id) {
    const card = $('#appointment-card-' + id);
    card.remove();

    // If no appointment cards remain, show the empty message
    if ($('#scheduledAppointments .col-md-4').length === 0) {
        $('#scheduledAppointments').append(`
            <div class="text-center text-muted py-5">
                <p>No scheduled appointments</p>
            </div>
        `);
    }
}
function updateAppointmentCount() {
    const count = $('#scheduledAppointments .col-md-4').length;
    $('#noOfAppointments').text(count);
}
