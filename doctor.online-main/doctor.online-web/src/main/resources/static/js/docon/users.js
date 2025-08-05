$(document).ready(function() {
	console.log("<<Users Javascript>>");
	$('#users-table').DataTable({
		paging: true,
		searching: true,
		info: true,
		processing: true
	});

	$('#users-table').on('click', '.clickable-row', function(e) {
		e.preventDefault();
		const id = $(this).data('id');
		if (!$(e.target).closest('.action-buttons a').length) {
			openAddUserModal();
		}
	});

	/*	$(document).on('click', '.edit-btn', function(e) {
			e.preventDefault();
			const id = $(this).data('id');
			alert('Edit clicked for ID: ' + id);
			// redirect or open modal
		});*/

	$("#users-table").on('click', '.delete-btn', function(e) {
		e.preventDefault();
		const id = $(this).data('id');
		alert('Delete clicked for ID: ' + id);
		// confirm delete or send request
	});

});

function openAddUserModal() {
	const myModal = new bootstrap.Modal(document.getElementById('addUserModal'));
	myModal.show();
}
