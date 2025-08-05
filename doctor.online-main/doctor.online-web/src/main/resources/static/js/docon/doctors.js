$(document).ready(function() {
	console.log("<<Doctors Javascript>>");
	$('#users-table').DataTable({
		paging: true,
		searching: true,
		info: true,
		processing: true
	});

	$("#loader").hide();
	$("#table-container").show();


	//loadAdmins();

	/*	$(document).on('click', '.edit-btn', function(e) {
			e.preventDefault();
			const id = $(this).data('id');
			alert('Edit clicked for ID: ' + id);
			// redirect or open modal
		});*/

	/*	$("#users-table").on('click', '.delete-btn', function(e) {
			e.preventDefault();
			const id = $(this).data('id');
			alert('Delete clicked for ID: ' + id);
			// confirm delete or send request
		});*/

	// Handle form submission
	$('#addUserForm').on('submit', function(e) {
		e.preventDefault();

		// Clear all previous error messages before validation
		$('#addUserForm .text-danger').remove();

		const name = $('#user-name').val().trim();
		const email = $('#user-email').val().trim();
		const username = $('#user-username').val().trim();
		const password = $('#user-password').val().trim();
		const role = $('#user-role').val();
		console.log("Checking validations");
		let valid = true;

		if (!name) {
			$('#user-name').after('<div class="text-danger">Name is required.</div>');
			valid = false;
		}

		if (!email || !validateEmail(email)) {
			$('#user-email').after('<div class="text-danger">Valid email is required.</div>');
			valid = false;
		}

		if (!username) {
			$('#user-username').after('<div class="text-danger">Username is required.</div>');
			valid = false;
		}

		if (!password || password.length < 6) {
			$('#user-password').after('<div class="text-danger">Password must be at least 6 characters.</div>');
			valid = false;
		}

		if (!role) {
			$('#user-role').after('<div class="text-danger">Role must be selected.</div>');
			valid = false;
		}

		// Validate Specialization
		if ($("#specialization").val() === null || $("#specialization").val() === "") {
			$("#specialization").addClass("is-invalid");
			valid = false;
		} else {
			$("#specialization").removeClass("is-invalid");
		}

		// Validate Qualifications (at least one checked)
		if ($("#qualification-group input[name='qualifications']:checked").length === 0) {
			$("#qualification-error").removeClass("d-none");
			valid = false;
		} else {
			$("#qualification-error").addClass("d-none");
		}


		// Validate slots
		let slots = [];
		let slotValid = true;
		$('#availabilitySlotsWrapper .availability-slot').each(function(index) {
			const dayOfWeek = $(this).find('.day-of-week').val();
			const startTime = $(this).find('.start-time').val();
			const endTime = $(this).find('.end-time').val();
			if (!dayOfWeek || !startTime || !endTime) {
				$(this).append('<div class="text-danger mt-2">All fields in this slot must be filled.</div>');
				slotValid = false;
				return;
			}
			// Optional: validate that endTime is after startTime
			if (startTime >= endTime) {
				$(this).append('<div class="text-danger mt-2">End time must be after start time.</div>');
				slotValid = false;
				return;
			}
			slots.push({
				dayOfWeek: dayOfWeek,
				startTime: startTime,
				endTime: endTime
			});
		});

		if (!slotValid || slots.length === 0) {
			$('#slot-error').removeClass('d-none').text('Please add at least one valid slot.');
			valid = false;
		} else {
			$('#slot-error').addClass('d-none');
		}



		if (!valid) return;
		console.log("form validation successful");

		let qualifications = [];
		$("input[name='qualifications']:checked").each(function() {
			qualifications.push($(this).val());
		});


		const doctorDetails = {
			specialization: $("#specialization").val(),
			qualifications: qualifications,
			availabilitySlots: slots
		}

		const adminData = {
			name: name,
			email: email,
			username: username,
			password: password,
			role: role,
			doctorDetails: doctorDetails
		};

		console.log(adminData);

		$.ajax({
			url: '/users',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(adminData),
			success: function() {
				toastr.success('Doctor added successfully');
				$('#addUserModal').modal('hide');
				loadAdmins();
				$('#addUserForm')[0].reset();
				$('#addUserForm')[0].classList.remove('was-validated');
			},
			error: function() {
				toastr.error('Error adding doctor');
			}
		});
	});

	// Handle delete
	let deleteUserId = null;

	$('#users-table').on('click', '.delete-btn', function(e) {
		e.preventDefault();
		deleteUserId = $(this).data('id');
		$('#confirmDeleteModal').modal('show');
	});

	$('#confirmDeleteBtn').on('click', function() {
		if (!deleteUserId) return;

		$.ajax({
			url: `/users/delete/${deleteUserId}`,
			type: 'POST',
			success: function() {
				$('#confirmDeleteModal').modal('hide');
				toastr.success('Doctor deleted successfully');
				loadAdmins(); // or loadDoctors(), loadPatients() based on context
			},
			error: function() {
				$('#confirmDeleteModal').modal('hide');
				toastr.error('Failed to delete doctor');
			}
		});
	});


	/*	$('#users-table').on('click', '.delete-btn', function (e) {
		e.preventDefault();
		const userId = $(this).data('id');

		if (confirm('Are you sure you want to delete this admin?')) {
			$.ajax({
				url: `/users/${userId}`,
				type: 'DELETE',
				success: function () {
					toastr.success('Admin deleted successfully');
					loadAdmins();
				},
				error: function () {
					toastr.error('Failed to delete admin');
				}
			});
		}
	});*/

	/*	// Handle edit click
		$('#users-table').on('click', '.edit-btn', function () {
			const userId = $(this).data('id');
			// You can build and open an edit modal here
			// For now, just show a message
			toastr.info(`Edit modal for user ID ${userId} not implemented`);
		});
	*/

	$('#users-table').on('click', '.clickable-row', function(e) {
		e.preventDefault();
		if (!$(e.target).closest('.action-buttons a').length) {
			const userId = $(this).data('id');
			$.ajax({
				url: `/users/${userId}`,
				method: 'GET',
				success: function(user) {
					console.log(user);
					$('#edit-user-id').val(user.id);
					$('#edit-user-name').val(user.name);
					$('#edit-user-email').val(user.email);
					$('#edit-user-username').val(user.username);
					// Load additional fields if needed

					// ✅ Fill Specialization
					$('#edit-specialization').val(user.doctorDetails.specialization);
					console.log(user.doctorDetails.qualifications);
					// ✅ Fill Qualifications (Array of strings)
					if (user.doctorDetails.qualifications && Array.isArray(user.doctorDetails.qualifications)) {
						$("#edit-qualification-group input[name='qualifications']").each(function() {
							if (user.doctorDetails.qualifications.includes($(this).val())) {
								$(this).prop('checked', true);
							} else {
								$(this).prop('checked', false);
							}
						});
					}

					$('#editUserModal').modal('show');

					loadDoctorSlots(user.doctorDetails.availabilitySlots);


				},
				error: function() {
					toastr.error("Failed to load doctor details.");
				}
			});
		}
	});

	// Handle Edit form submission
	$('#editUserForm').on('submit', function(e) {
		e.preventDefault();

		clearEditErrors();

		const userId = $('#edit-user-id').val();
		const name = $('#edit-user-name').val().trim();
		const email = $('#edit-user-email').val().trim();
		const username = $('#edit-user-username').val().trim();

		let isValid = true;

		if (!name) {
			$('#edit-user-name').after('<div class="text-danger">Name is required.</div>');
			isValid = false;
		}

		if (!email || !/^\S+@\S+\.\S+$/.test(email)) {
			$('#edit-user-email').after('<div class="text-danger">Valid email is required.</div>');
			isValid = false;
		}

		if (!username) {
			$('#edit-user-username').after('<div class="text-danger">Username is required.</div>');
			isValid = false;
		}

		// Validate Specialization
		if ($("#edit-specialization").val() === null || $("#edit-specialization").val() === "") {
			$("#edit-specialization").addClass("is-invalid");
			isValid = false;
		} else {
			$("#edit-specialization").removeClass("is-invalid");
		}

		// Validate Qualifications (at least one checked)
		if ($("#edit-qualification-group input[name='qualifications']:checked").length === 0) {
			$("#edit-qualification-error").removeClass("d-none");
			isValid = false;
		} else {
			$("#edit-qualification-error").addClass("d-none");
		}


		// Validate slots
		let slots = [];
		let slotValid = true;
		$('#edit-availabilitySlotsWrapper .availability-slot').each(function(index) {
			const dayOfWeek = $(this).find('.day-of-week').val();
			const startTime = $(this).find('.start-time').val();
			const endTime = $(this).find('.end-time').val();
			if (!dayOfWeek || !startTime || !endTime) {
				$(this).append('<div class="text-danger mt-2">All fields in this slot must be filled.</div>');
				slotValid = false;
				return;
			}
			// Optional: validate that endTime is after startTime
			if (startTime >= endTime) {
				$(this).append('<div class="text-danger mt-2">End time must be after start time.</div>');
				slotValid = false;
				return;
			}
			slots.push({
				dayOfWeek: dayOfWeek,
				startTime: startTime,
				endTime: endTime
			});
		});

		if (!slotValid || slots.length === 0) {
			$('#slot-error').removeClass('d-none').text('Please add at least one valid slot.');
			isValid = false;
		} else {
			$('#slot-error').addClass('d-none');
		}



		if (!isValid) return;

		let qualifications = [];
		$("#edit-qualification-group input[name='qualifications']:checked").each(function() {
			qualifications.push($(this).val());
		});

		const doctorDetails = {
			qualifications: qualifications
		}



		const updatedUser = {
			name,
			email,
			username,
			doctorDetails
		};

		const doctorDTO = {
			user: updatedUser,
			specialization: $("#edit-specialization").val(),
			doctorSlots: slots
		}

		$.ajax({
			url: `/users/doctor/${userId}`,
			method: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(doctorDTO),
			success: function() {
				toastr.success("Doctor updated successfully.");
				$('#editUserModal').modal('hide');
				loadAdmins(); // or loadUsers() depending on your context
			},
			error: function() {
				toastr.error("Failed to update doctor.");
			}
		});
	});





	//Slots
	$('#addSlotBtn').click(function() {
		const slotTemplate = `
	           <div class="availability-slot mb-4 border p-3 rounded position-relative">
	               <div class="row g-3">
	                   <div class="col-md-4">
	                       <label class="form-label fw-semibold">Day of Week</label>
	                       <select class="form-select day-of-week" required>
	                           <option value="" disabled selected>Select Day</option>
	                           <option value="MONDAY">Monday</option>
	                           <option value="TUESDAY">Tuesday</option>
	                           <option value="WEDNESDAY">Wednesday</option>
	                           <option value="THURSDAY">Thursday</option>
	                           <option value="FRIDAY">Friday</option>
	                           <option value="SATURDAY">Saturday</option>
	                           <option value="SUNDAY">Sunday</option>
	                       </select>
	                   </div>
	                   <div class="col-md-4">
	                       <label class="form-label fw-semibold">Start Time</label>
	                       <input type="time" class="form-control start-time" required>
	                   </div>
	                   <div class="col-md-4">
	                       <label class="form-label fw-semibold">End Time</label>
	                       <input type="time" class="form-control end-time" required>
	                   </div>
	               </div>
	               <button type="button" class="btn btn-sm remove-slot-btn position-absolute top-0 end-0"><i class="fas fa-trash-alt" style="color: #dc3545;"></i></button>
	           </div>`;

		$('#availabilitySlotsWrapper').append(slotTemplate);
	});

	//Slots
	$('#edit-addSlotBtn').click(function() {
		const slotTemplate = `
	           <div class="availability-slot mb-4 border p-3 rounded position-relative">
	               <div class="row g-3">
	                   <div class="col-md-4">
	                       <label class="form-label fw-semibold">Day of Week</label>
	                       <select class="form-select day-of-week" required>
	                           <option value="" disabled selected>Select Day</option>
	                           <option value="MONDAY">Monday</option>
	                           <option value="TUESDAY">Tuesday</option>
	                           <option value="WEDNESDAY">Wednesday</option>
	                           <option value="THURSDAY">Thursday</option>
	                           <option value="FRIDAY">Friday</option>
	                           <option value="SATURDAY">Saturday</option>
	                           <option value="SUNDAY">Sunday</option>
	                       </select>
	                   </div>
	                   <div class="col-md-4">
	                       <label class="form-label fw-semibold">Start Time</label>
	                       <input type="time" class="form-control start-time" required>
	                   </div>
	                   <div class="col-md-4">
	                       <label class="form-label fw-semibold">End Time</label>
	                       <input type="time" class="form-control end-time" required>
	                   </div>
	               </div>
	               <button type="button" class="btn btn-sm remove-slot-btn position-absolute top-0 end-0"><i class="fas fa-trash-alt" style="color: #dc3545;"></i></button>
	           </div>`;

		$('#edit-availabilitySlotsWrapper').append(slotTemplate);
	});

	// Remove slot on click
	$('#availabilitySlotsWrapper').on('click', '.remove-slot-btn', function() {
		$(this).closest('.availability-slot').remove();
	});

	// Remove slot on click
	$('#edit-availabilitySlotsWrapper').on('click', '.remove-slot-btn', function() {
		$(this).closest('.availability-slot').remove();
	});




});

/*------------------------------------funcitons-------------------------------------------------*/

function loadDoctorSlots(slots) {
	const $wrapper = $('#edit-availabilitySlotsWrapper');
	$wrapper.empty(); // Clear previous content
	slots.forEach((slot, index) => {
		const slotHtml = `
        <div class="availability-slot mb-4 border p-3 rounded position-relative">
            <div class="row g-3">
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Day of Week</label>
                    <select class="form-select day-of-week" required>
                        <option value="" disabled>Select Day</option>
                        <option value="MONDAY" ${slot.dayOfWeek === 'MONDAY' ? 'selected' : ''}>Monday</option>
                        <option value="TUESDAY" ${slot.dayOfWeek === 'TUESDAY' ? 'selected' : ''}>Tuesday</option>
                        <option value="WEDNESDAY" ${slot.dayOfWeek === 'WEDNESDAY' ? 'selected' : ''}>Wednesday</option>
                        <option value="THURSDAY" ${slot.dayOfWeek === 'THURSDAY' ? 'selected' : ''}>Thursday</option>
                        <option value="FRIDAY" ${slot.dayOfWeek === 'FRIDAY' ? 'selected' : ''}>Friday</option>
                        <option value="SATURDAY" ${slot.dayOfWeek === 'SATURDAY' ? 'selected' : ''}>Saturday</option>
                        <option value="SUNDAY" ${slot.dayOfWeek === 'SUNDAY' ? 'selected' : ''}>Sunday</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Start Time</label>
                    <input type="time" class="form-control start-time" value="${slot.startTime}" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">End Time</label>
                    <input type="time" class="form-control end-time" value="${slot.endTime}" required>
                </div>
            </div>
            <button type="button" class="btn btn-sm remove-slot-btn position-absolute top-0 end-0">
                <i class="fas fa-trash-alt" style="color: #dc3545;"></i>
            </button>
        </div>`;
		$wrapper.append(slotHtml);

	});
}



$('#edit-availabilitySlotsWrapper').on('click', '.remove-slot-btn', function() {
	$(this).closest('.availability-slot').remove();

	// Hide remove buttons if only one remains
	if ($('.availability-slot').length === 1) {
		$('.remove-slot-btn').addClass('d-none');
	}
});


function openAddUserModal() {
	const myModal = new bootstrap.Modal(document.getElementById('addUserModal'));
	myModal.show();
}

// Load admins into table
function loadAdmins() {
	$.ajax({
		url: '/users/doctors-list',
		type: 'GET',
		success: function(admins) {
			const tbody = $('#users-table tbody');
			tbody.empty();
			admins.forEach(admin => {
				const row = `
                <tr class="clickable-row" data-id="${admin.id}">
                    <td>${admin.name}</td>
                    <td>${admin.email}</td>
                    <td>
                        <span class="action-buttons">
                            <a href="#" class="edit-btn" data-id="${admin.id}" title="Edit" hidden> 
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="#" class="delete-btn" data-id="${admin.id}" title="Delete">
                                <i class="fas fa-trash-alt"></i>
                            </a>
                        </span>
                    </td>
                </tr>`;
				tbody.append(row);
			});
		},
		error: function() {
			toastr.error('Failed to load admins');
		}
	});
}

function validateEmail(email) {
	const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	return re.test(email);
}
function clearErrors() {
	$('.text-danger').remove(); // remove old error messages
}
function clearEditErrors() {
	$('#edit-user-form .text-danger').remove();
}

$('#addUserModal').on('hidden.bs.modal', function() {
	$("#qualification-group input[name='qualifications']").prop('checked', false);
	const $wrapper = $('#availabilitySlotsWrapper');
	$wrapper.empty();
});

$('#editUserModal').on('hidden.bs.modal', function() {
	$("#edit-qualification-group input[name='qualifications']").prop('checked', false);
	const $wrapper = $('#edit-availabilitySlotsWrapper');
	$wrapper.empty();
});

