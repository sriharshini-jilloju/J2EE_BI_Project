$(document).ready(function() {
	togglePatientDetails();

	let currentStep = 1;
	let selectedDepartment = null;
	let selectedAppointmentType = null;
	let selectedDoctor = null;
	let selectedDate = null;
	let selectedSlot = null;

	// Mock data
	const departments = [
		{ id: 1, name: 'Oncology', icon: 'fas fa-ribbon', description: 'Cancer treatment and care', color: '#dc3545' },
		{ id: 2, name: 'General Physician', icon: 'fas fa-user-md', description: 'General health checkups', color: '#198754' },
		{ id: 3, name: 'Neurology', icon: 'fas fa-brain', description: 'Brain and nervous system', color: '#d63384' },
		{ id: 4, name: 'Pediatrics', icon: 'fas fa-child', description: 'Child healthcare', color: '#0dcaf0' },
		{ id: 5, name: 'Dentist', icon: 'fas fa-tooth', description: 'Dental care and treatment', color: '#20c997' },
		{ id: 6, name: 'Dietician', icon: 'fas fa-apple-alt', description: 'Nutrition and diet planning', color: '#28a745' },
		{ id: 7, name: 'Diabetes Specialist', icon: 'fas fa-syringe', description: 'Diabetes management', color: '#ffc107' },
		{ id: 8, name: 'Physiotherapist', icon: 'fas fa-walking', description: 'Physical therapy and rehabilitation', color: '#17a2b8' },
		{ id: 9, name: 'Orthopedic', icon: 'fas fa-bone', description: 'Bone and joint care', color: '#6f42c1' },
		{ id: 10, name: 'Urology', icon: 'fas fa-kidneys', description: 'Urinary system care', color: '#fd7e14' },
		{ id: 11, name: 'Sexology', icon: 'fas fa-venus-mars', description: 'Sexual health specialist', color: '#e83e8c' },
		{ id: 12, name: 'Pathology', icon: 'fas fa-microscope', description: 'Lab tests and diagnosis', color: '#6c757d' },
		{ id: 13, name: 'Dermatology', icon: 'fas fa-spa', description: 'Skin and hair care', color: '#fd7e14' },
		{ id: 14, name: 'Gynaecology', icon: 'fas fa-female', description: 'Women health specialist', color: '#e83e8c' },
		{ id: 15, name: 'Gastroenterology', icon: 'fas fa-pills', description: 'Digestive system care', color: '#20c997' },
		{ id: 16, name: 'Psychiatry', icon: 'fas fa-head-side-virus', description: 'Mental health care', color: '#6f42c1' },
		{ id: 17, name: 'Ophthalmology', icon: 'fas fa-eye', description: 'Eye care specialist', color: '#17a2b8' },
		{ id: 18, name: 'Cardiology', icon: 'fas fa-heartbeat', description: 'Heart and cardiovascular care', color: '#dc3545' },
		{ id: 19, name: 'ENT', icon: 'fas fa-head-side-cough', description: 'Ear, Nose & Throat', color: '#ffc107' }
	];

	const doctors = {
		1: [ // Oncology
			{ id: 1, name: 'Dr. John Smith', experience: '15 years', rating: 4.8, image: 'https://via.placeholder.com/100x100' },
			{ id: 2, name: 'Dr. Sarah Johnson', experience: '12 years', rating: 4.9, image: 'https://via.placeholder.com/100x100' }
		],
		2: [ // General Physician
			{ id: 3, name: 'Dr. Michael Brown', experience: '10 years', rating: 4.7, image: 'https://via.placeholder.com/100x100' },
			{ id: 4, name: 'Dr. Emily Davis', experience: '8 years', rating: 4.8, image: 'https://via.placeholder.com/100x100' }
		],
		18: [ // Cardiology
			{ id: 5, name: 'Dr. Robert Wilson', experience: '20 years', rating: 4.6, image: 'https://via.placeholder.com/100x100' },
			{ id: 6, name: 'Dr. Lisa Anderson', experience: '14 years', rating: 4.9, image: 'https://via.placeholder.com/100x100' }
		],
		13: [ // Dermatology
			{ id: 7, name: 'Dr. Mark Thompson', experience: '18 years', rating: 4.5, image: 'https://via.placeholder.com/100x100' }
		]
		// Other departments have no doctors available
	};

	// Set minimum date to today
	const today = new Date().toISOString().split('T')[0];
	$('#dateSelect').attr('min', today);

	// Initialize
	loadDepartments();

	// Event handlers
	$('#nextBtn').click(handleNext);
	$('#prevBtn').click(handlePrev);
	$('#dateSelect').change(loadAvailableSlots);
	$('#clearSelection').click(clearSelection);
	$('#appointmentForm').submit(handleSubmit);

	function loadDepartments() {
		let html = '';
		departments.forEach(dept => {
			html += `
                       <div class="col-lg-3 col-md-4 col-sm-6 mb-3">
                           <div class="card department-card h-100" data-dept-id="${dept.id}">
                               <div class="card-body text-center">
                                   <i class="${dept.icon} fa-3x mb-3" style="color: ${dept.color}"></i>
                                   <h6 class="card-title">${dept.name}</h6>
                                   <p class="card-text small text-muted">${dept.description}</p>
                               </div>
                           </div>
                       </div>
                   `;
		});
		$('#departmentsContainer').html(html);

		$('.department-card').click(function() {
			$('.department-card').removeClass('selected');
			$(this).addClass('selected');
			selectedDepartment = $(this).data('dept-id');

			// Auto-navigate to next step after brief delay
			setTimeout(() => showStep(2), 300);
		});
		$("#departmentSection").show();
		$("#loader").hide();
	}

	function loadDoctors() {

		$.ajax({
			url: '/users/doctors-list/by-department/' + selectedDepartment,
			type: 'GET',
			dataType: 'json',
			beforeSend: function() {
				$('#doctorsContainer').html('<div class="text-center">Loading doctors...</div>');
			},
			success: function(deptDoctors) {
				let html = '';

				if (!deptDoctors || deptDoctors.length === 0) {
					html = `
							<div class="col-12">
								<div class="alert alert-warning text-center" role="alert">
									<i class="fas fa-exclamation-triangle fa-2x mb-3"></i>
									<h5>No Doctors Available</h5>
									<p class="mb-0">No doctors are currently available for this department. Please try selecting a different department.</p>
								</div>
							</div>`;
				} else {
					deptDoctors.forEach(doctor => {
						html += `
								<div class="col-lg-4 col-md-6 mb-3">
									<div class="card doctor-card h-100" data-doctor-id="${doctor.id}">
										<div class="card-body text-center">
											<img src="../img/doctor-icon.png" class="rounded-circle mb-3" width="80" height="80" alt="${doctor.name}">
											<h6 class="card-title">${doctor.name}</h6>
											<p class="card-text small">
												<i class="fas fa-user-graduate me-1"></i>15<br>
												<i class="fas fa-star text-warning me-1"></i>4.5/5
											</p>
										</div>
									</div>
								</div>`;
					});
				}

				$('#doctorsContainer').html(html);

				$('.doctor-card').click(function() {
					$('.doctor-card').removeClass('selected');
					$(this).addClass('selected');
					selectedDoctor = $(this).data('doctor-id');

					// Auto-navigate to next step after brief delay
					setTimeout(() => showStep(4), 300);
				});
			},
			error: function() {
				$('#doctorsContainer').html('<div class="alert alert-danger text-center">Failed to load doctors. Please try again later.</div>');
			}
		});


		/*	const deptDoctors = doctors[selectedDepartment] || [];
			let html = '';
	
			if (deptDoctors.length === 0) {
				html = `
						   <div class="col-12">
							   <div class="alert alert-warning text-center" role="alert">
								   <i class="fas fa-exclamation-triangle fa-2x mb-3"></i>
								   <h5>No Doctors Available</h5>
								   <p class="mb-0">No doctors are currently available for this department. Please try selecting a different department</p>
								   <button type="button" class="btn btn-outline-primary mt-3" onclick="showStep(1)">
									   <i class="fas fa-arrow-left me-2"></i>Back to Departments
								   </button>
							   </div>
						   </div>
					   `;
			} else {
				deptDoctors.forEach(doctor => {
					html += `
							   <div class="col-lg-4 col-md-6 mb-3">
								   <div class="card doctor-card h-100" data-doctor-id="${doctor.id}">
									   <div class="card-body text-center">
										   <img src="../img/doctor-icon.png" class="rounded-circle mb-3" width="80" height="80" alt="${doctor.name}">
										   <h6 class="card-title">${doctor.name}</h6>
										   <p class="card-text small">
											   <i class="fas fa-user-graduate me-1"></i>${doctor.experience}<br>
											   <i class="fas fa-star text-warning me-1"></i>${doctor.rating}/5
										   </p>
									   </div>
								   </div>
							   </div>
						   `;
				});
			}
	
			$('#doctorsContainer').html(html);
	
			$('.doctor-card').click(function() {
				$('.doctor-card').removeClass('selected');
				$(this).addClass('selected');
				selectedDoctor = $(this).data('doctor-id');
	
				// Auto-navigate to next step after brief delay
				setTimeout(() => showStep(4), 300);
			});*/
	}

	function handleNext() {
		if (currentStep === 1 && selectedDepartment) {
			showStep(2);
			const deptName = departments.find(d => d.id === selectedDepartment)?.name;
			$('#selectedDeptInstant').text(deptName);
		} else if (currentStep === 2 && selectedAppointmentType) {
			if (selectedAppointmentType === 'instant') {
				showStep(5); // Skip to patient info
			} else {
				showStep(3);
				loadDoctors();
			}
		} else if (currentStep === 3 && selectedDoctor) {
			showStep(4);
		} else if (currentStep === 4 && (selectedSlot || selectedAppointmentType === 'instant')) {
			showStep(5);
		} else {
			toastr.warning('Please complete the current step before proceeding.');
		}
	}

	function handlePrev() {
		if (currentStep === 5 && selectedAppointmentType === 'instant') {
			showStep(2);
		} else if (currentStep > 1) {
			showStep(currentStep - 1);
		}
	}

	$("#backToDepartments").click(function() {
		console.log("backToDepartments");
		showStep(1);
	});

	function showStep(step) {
		// Hide all sections
		$('.section').removeClass('active');

		// Update step indicators
		$('.step').removeClass('active completed');
		for (let i = 1; i < step; i++) {
			$(`#step${i}`).addClass('completed');
		}
		$(`#step${step}`).addClass('active');

		// Show current section
		currentStep = step;
		switch (step) {
			case 1:
				$('#departmentSection').addClass('active');
				$('#prevBtn').hide();
				$('#nextBtn').show().text('Next').append('<i class="fas fa-arrow-right ms-2"></i>');
				break;
			case 2:
				$('#appointmentTypeSection').addClass('active');
				$('#prevBtn').show();
				$('#nextBtn').show();
				break;
			case 3:
				$('#doctorSection').addClass('active');
				$('#prevBtn').show();
				$('#nextBtn').show();
				break;
			case 4:
				$('#schedulingSection').addClass('active');
				$('#prevBtn').show();
				$('#nextBtn').show();
				break;
			case 5:
				if (selectedAppointmentType === 'instant') {
					$('#instantSection').addClass('active');
					$('#patientSection').addClass('active');
				} else {
					$('#patientSection').addClass('active');
				}
				$('#prevBtn').show();
				$('#nextBtn').hide();
				break;
		}
	}

	// Appointment type selection with auto-navigation
	$(document).on('click', '.appointment-type-card', function() {
		$('.appointment-type-card').removeClass('border-primary');
		$(this).addClass('border-primary');
		selectedAppointmentType = $(this).data('type');

		// Auto-navigate based on selection
		if (selectedAppointmentType === 'instant') {
			setTimeout(() => showStep(5), 300); // Small delay for visual feedback
		} else {
			setTimeout(() => {
				showStep(3);
				loadDoctors();
			}, 300);
		}
	});

	function loadAvailableSlots() {
		if (!selectedDoctor || !$('#dateSelect').val()) return;

		selectedDate = $('#dateSelect').val();
		$('#slotsSection').show();
		$('#slotsContainer').html(''); // Clear previous slots    
		$('#slotLoadingSpinner').show();  // Show the spinner
		      
		// Simulate API call
		setTimeout(renderSlots, 1000);
	}

	function renderSlots() {
		if (!selectedDoctor || !selectedDate) return;

		$.ajax({
			url: '/appointments/slots',
			method: 'GET',
			data: {
				doctorId: selectedDoctor,
				date: selectedDate
			},
			success: function(response) {
				const timeSlots = response.timeSlots;
				const bookedSlots = response.bookedSlots;
				let html = '<div class="row g-2">';
				if (timeSlots.length === 0) {
					html = `
				                    <div class="text-center text-muted py-3">
				                        <i class="fas fa-calendar-times fa-2x mb-2"></i>
				                        <p>No slots available for the selected date.</p>
				                    </div>
				                `;
				} else {
					timeSlots.forEach(time => {
						const isBooked = bookedSlots.includes(time);
						const slotClass = isBooked ? 'slot booked' : 'slot';
						html += `
									                   <div class="col-md-2 col-sm-3 col-4">
									                       <div class="${slotClass}" data-time="${time}" ${!isBooked ? 'onclick="selectSlot(this)"' : ''}>
									                           <div class="text-center p-2">
									                               <small>${formatTime12Hour(time)}</small>
									                           </div>
									                       </div>
									                   </div>
									               `;
					});

				}
				html += '</div>';
				$('#slotLoadingSpinner').hide();
				$('#slotsContainer').html(html);
			},
			error: function() {
				$('#slotLoadingSpinner').hide();
				$('#slotsContainer').html('<p class="text-danger">Failed to load slots.</p>');
			}
		});



		/*console.log(selectedDoctor);
		console.log(selectedDate);
		// Mock slots generation
		const timeSlots = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30'];
		const bookedSlots = ['09:30', '11:00', '15:00']; // Mock booked slots

		let html = '<div class="row g-2">';
		timeSlots.forEach(time => {
			const isBooked = bookedSlots.includes(time);
			const slotClass = isBooked ? 'slot booked' : 'slot';
			html += `
					   <div class="col-md-2 col-sm-3 col-4">
						   <div class="${slotClass}" data-time="${time}" ${!isBooked ? 'onclick="selectSlot(this)"' : ''}>
							   <div class="text-center p-2">
								   <small>${formatTime12Hour(time)}</small>
							   </div>
						   </div>
					   </div>
				   `;
		});
		html += '</div>';

		$('#slotsContainer').html(html);*/
	}

	window.selectSlot = function(element) {
		$('.slot').removeClass('selected');
		$(element).addClass('selected');

		selectedSlot = {
			time: $(element).data('time'),
			date: selectedDate,
			doctorId: selectedDoctor
		};

		// Show selected slot info
		findDoctorInfo(selectedDoctor).then(doctorInfo => {
			console.log("Dr." + doctorInfo.name);
			const deptName = departments.find(d => d.id === selectedDepartment)?.name;
			const formattedDate = new Date(selectedDate).toLocaleDateString('en-US', {
				weekday: 'long',
				year: 'numeric',
				month: 'long',
				day: 'numeric'
			});

			$('#selectedDetails').html(`
			                   <p class="mb-1"><strong>Department:</strong> ${deptName}</p>
			                   <p class="mb-1"><strong>Doctor:</strong> ${doctorInfo.name}</p>
			                   <p class="mb-1"><strong>Date:</strong> ${formattedDate}</p>
			                   <p class="mb-0"><strong>Time:</strong> ${formatTime12Hour(selectedSlot.time)}</p>
			               `);

			$('#selectedSlotInfo').show();
		});
		/*const doctorInfo = findDoctorInfo(selectedDoctor);
		const deptName = departments.find(d => d.id === selectedDepartment)?.name;
		const formattedDate = new Date(selectedDate).toLocaleDateString('en-US', {
			weekday: 'long',
			year: 'numeric',
			month: 'long',
			day: 'numeric'
		});

		$('#selectedDetails').html(`
				   <p class="mb-1"><strong>Department:</strong> ${deptName}</p>
				   <p class="mb-1"><strong>Doctor:</strong> ${doctorInfo.name}</p>
				   <p class="mb-1"><strong>Date:</strong> ${formattedDate}</p>
				   <p class="mb-0"><strong>Time:</strong> ${formatTime12Hour(selectedSlot.time)}</p>
			   `);

		$('#selectedSlotInfo').show();*/

		// Auto-navigate to patient form after brief delay
		//setTimeout(() => showStep(5), 800);
	};

	function clearSelection() {
		$('.slot').removeClass('selected');
		selectedSlot = null;
		$('#selectedSlotInfo').hide();
	}

	/*function findDoctorInfo(doctorId) {
			for (let deptId in doctors) {
				const doctor = doctors[deptId].find(d => d.id === doctorId);
				if (doctor) return doctor;
			}
			return { name: 'Unknown Doctor' };
		}*/




	function formatTime12Hour(time24) {
		const [hours, minutes] = time24.split(':').map(Number);
		const period = hours >= 12 ? 'PM' : 'AM';
		const hours12 = hours % 12 || 12;
		return `${hours12}:${minutes.toString().padStart(2, '0')} ${period}`;
	}

	function validateForm() {
		const name = $('#patientName').val().trim();
		const phone = $('#patientPhone').val().trim();
		const age = $('#patientAge').val();
		const gender = $('#patientGender').val();
		const symptoms = $('#symptoms').val().trim();
		const isForSelf = $('#isForSelf').val() === 'true';

		if (!isForSelf) {
			if (!name) {
				toastr.warning('Please enter patient name');
				$('#patientName').focus();
				return false;
			}

			if (!phone || phone.length < 10) {
				toastr.warning('Please enter a valid phone number (minimum 10 digits)');
				$('#patientPhone').focus();
				return false;
			}

			if (!age || age < 1 || age > 120) {
				toastr.warning('Please enter a valid age (1-120)');
				$('#patientAge').focus();
				return false;
			}

			if (!gender) {
				toastr.warning('Please select gender');
				$('#patientGender').focus();
				return false;
			}

		}

		if (!symptoms) {
			toastr.warning('Please describe your symptoms or reason for visit');
			$('#symptoms').focus();
			return false;
		}

		return true;
	}

	// Create appointment objects for both instant and scheduled
	let appointmentData;

	async function handleSubmit(e) {
		e.preventDefault();

		if (!validateForm()) {
			return;
		}
		const isForSelf = $('#isForSelf').val() === 'true';
		const selectedDoctorInfo = selectedDoctor ? await findDoctorInfo(selectedDoctor) : null;

		if (selectedAppointmentType === 'instant') {
			appointmentData = {
				isForSelf: isForSelf,
				appointmentType: 'instant',
				departmentId: selectedDepartment,
				departmentName: departments.find(d => d.id === selectedDepartment)?.name,
				patientName: $('#patientName').val().trim(),
				patientPhone: $('#patientPhone').val().trim(),
				patientEmail: $('#patientEmail').val().trim() || null,
				patientAge: parseInt($('#patientAge').val()),
				patientGender: $('#patientGender').val(),
				emergencyContact: $('#emergencyContact').val().trim() || null,
				symptoms: $('#symptoms').val().trim(),
				priority: 'high' // For instant appointments
			};
		} else {
			appointmentData = {
				isForSelf: isForSelf,
				appointmentType: 'scheduled',
				departmentId: selectedDepartment,
				departmentName: departments.find(d => d.id === selectedDepartment)?.name,
				doctorId: selectedDoctor,
				doctorName: selectedDoctorInfo.name,
				appointmentDate: selectedDate,
				appointmentTime: selectedSlot?.time,
				patientName: $('#patientName').val().trim(),
				patientPhone: $('#patientPhone').val().trim(),
				patientEmail: $('#patientEmail').val().trim() || null,
				patientAge: parseInt($('#patientAge').val()),
				patientGender: $('#patientGender').val(),
				emergencyContact: $('#emergencyContact').val().trim() || null,
				symptoms: $('#symptoms').val().trim(),
				priority: 'normal'
			};
		}



		/*// Show loading
		$('#bookAppointment').html('<span class="spinner-border spinner-border-sm me-2"></span>Booking...').prop('disabled', true);*/

		showAppointmentConfirmation(appointmentData)


	}



	function showAppointmentConfirmation(appointmentData) {
		const $view = $('#appointmentDetailsView');
		$view.empty(); // Clear previous content

		var fieldsToShow = {};
		if (appointmentData.isForSelf) {
			fieldsToShow = {
				appointmentType: 'Appointment Type',
				departmentName: 'Department',
				doctorName: 'Doctor',
				appointmentDate: 'Date',
				appointmentTime: 'Time',
				emergencyContact: 'Emergency Contact',
				symptoms: 'Symptoms',
				priority: 'Priority'
			}
		} else {
			fieldsToShow = {
				appointmentType: 'Appointment Type',
				departmentName: 'Department',
				doctorName: 'Doctor',
				appointmentDate: 'Date',
				appointmentTime: 'Time',
				patientName: 'Patient Name',
				patientPhone: 'Phone',
				patientEmail: 'Email',
				patientAge: 'Age',
				patientGender: 'Gender',
				emergencyContact: 'Emergency Contact',
				symptoms: 'Symptoms',
				priority: 'Priority'
			}
		}

		for (let key in fieldsToShow) {
			if (appointmentData[key]) {
				$view.append(`
	        <div class="mb-2">
	          <strong>${fieldsToShow[key]}:</strong> ${appointmentData[key]}
	        </div>
	      `);
			}
		}

		$('#confirmAppointmentModal').modal('show');
	}


	$('#confirmSubmitBtn').on('click', function() {
		// Ajax submission
		submitAppointment(appointmentData);
	});




	function submitAppointment(data) {
		// Example Ajax call - replace with your actual endpoint
		$.ajax({
			url: selectedAppointmentType === 'instant' ? '/appointments/book-instant' : '/appointments/book-scheduled',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success: function(response) {
				console.log('Success Response:', response);
				//location.reload();
				$('#confirmAppointmentModal').modal('hide');
				$("#appointmentSuccessModal").modal("show");
				toastr.success(response);
			},
			error: function(xhr, status, error) {
				console.error('Error:', error);
				console.log('Appointment Data for debugging:', data);
				toastr.error("Unable to save the appointment!");
				//location.reload();
			},
			complete: function() {
				$('#bookAppointment').html('<i class="fas fa-check me-2"></i>Book Appointment').prop('disabled', false);
			}
		});
	}

	function togglePatientDetails() {
		const isForSelf = $('#isForSelf').val() === 'true';

		if (isForSelf) {
			$('#patientDetails').hide();

			// Remove required attributes
			$('#patientName, #patientPhone, #patientEmail, #patientAge, #patientGender').removeAttr('required');
		} else {
			$('#patientDetails').show();

			// Add required attributes (except email, which you don't want to enforce)
			$('#patientName, #patientPhone, #patientAge, #patientGender').attr('required', true);
		}
	}




	$('#isForSelf').on('change', togglePatientDetails);

	$("#gotoPendingBtn").click(function() {
		window.location.href = '/appointments/awaiting';
	});
	$("#reloadPageBtn").click(function() {
		location.reload();
	});

	async function findDoctorInfo(doctorId) {
		try {
			const response = await fetch(`/users/${doctorId}`);
			if (!response.ok) throw new Error("Doctor not found");
			return await response.json();
		} catch (e) {
			console.error("Error fetching doctor:", e);
			return { name: 'Unknown Doctor' };
		}
	}


});