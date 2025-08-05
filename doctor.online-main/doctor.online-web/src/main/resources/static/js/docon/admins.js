$(document).ready(function() {
	console.log("<<Admins Javascript>>");
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
	$('#addUserForm').on('submit', function (e) {
	    e.preventDefault();

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

		  if (!valid) return;
		   console.log("form validation successful");
		   
	    const adminData = {
				name: name,
			    email: email,
			    username: username,
			    password: password,
			    role: role
	    };

	    $.ajax({
	        url: '/users',
	        method: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify(adminData),
	        success: function () {
	            toastr.success('Admin added successfully');
	            $('#addUserModal').modal('hide');
	            loadAdmins();
	            $('#addUserForm')[0].reset();
	            $('#addUserForm')[0].classList.remove('was-validated');
	        },
	        error: function () {
	            toastr.error('Error adding admin');
	        }
	    });
	});

	// Handle delete
	let deleteUserId = null;

	$('#users-table').on('click', '.delete-btn', function (e) {
	    e.preventDefault();
	    deleteUserId = $(this).data('id');
	    $('#confirmDeleteModal').modal('show');
	});

	$('#confirmDeleteBtn').on('click', function () {
	    if (!deleteUserId) return;

	    $.ajax({
	        url: `/users/delete/${deleteUserId}`,
	        type: 'POST',
	        success: function () {
	            $('#confirmDeleteModal').modal('hide');
	            toastr.success('User deleted successfully');
	            loadAdmins(); // or loadDoctors(), loadPatients() based on context
	        },
	        error: function () {
	            $('#confirmDeleteModal').modal('hide');
	            toastr.error('Failed to delete user');
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
		     success: function (user) {
		         $('#edit-user-id').val(user.id);
		         $('#edit-user-name').val(user.name);
		         $('#edit-user-email').val(user.email);
				 $('#edit-user-username').val(user.username);
		         // Load additional fields if needed

		         $('#editUserModal').modal('show');
		     },
		     error: function () {
		         toastr.error("Failed to load user details.");
		     }
		 });
	}
});

// Handle Edit form submission
$('#editUserForm').on('submit', function (e) {
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

	  if (!isValid) return;

	  const updatedUser = {
	      name,
	      email,
	      username
	      // Add more fields if needed
	  };

    $.ajax({
        url: `/users/${userId}`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(updatedUser),
        success: function () {
            toastr.success("User updated successfully.");
            $('#editUserModal').modal('hide');
            loadAdmins(); // or loadUsers() depending on your context
        },
        error: function () {
            toastr.error("Failed to update user.");
        }
    });
});




	
	
	
	
	
	
});

function openAddUserModal() {
	const myModal = new bootstrap.Modal(document.getElementById('addUserModal'));
	myModal.show();
}

// Load admins into table
function loadAdmins() {
    $.ajax({
        url: '/users/admins-list',
        type: 'GET',
        success: function (admins) {
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
        error: function () {
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
	