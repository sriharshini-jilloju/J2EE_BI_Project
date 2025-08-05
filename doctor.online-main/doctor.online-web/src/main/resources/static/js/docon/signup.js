$(document).ready(function() {
	console.log("<<Patient registration>>");
	$('#responseMessage').text('');

	$('#signupForm').on('submit', function(e) {
		e.preventDefault(); // Prevent default form submission

		$('#responseMessage').text('');

		let isValid = true;

		// Trimmed values
		const name = $('#name').val().trim();
		const username = $('#username').val().trim();
		const email = $('#email').val().trim();
		const password = $('#password').val().trim();
		const gender = $('#gender').val();
		const age = $('#age').val();
		const bloodGroup = $('#bloodGroup').val();

		console.log("name: " + name + " | username: " + username + " | email:" + email + " | password:" + password + " | gender:" + gender + " | age:" + age + " | bloodGroup:" + bloodGroup);

		// Simple field validations
		if (name === '' || name.length < 2) isValid = false;
		if (username === '') isValid = false;

		console.log(isValid);

		if (email === '' || !email.includes('@')) isValid = false;

		console.log(isValid);

		if (password === '' || password.length < 6) {
			$('#responseMessage').text('Password length should be atleast 6 characters!');
			isValid = false;
			return false;
		}

		console.log(isValid);

		if (!gender) isValid = false;

		console.log(isValid);

		if (!age || parseInt(age) <= 0) isValid = false;
		/*if (!bloodGroup) isValid = false;*/

		console.log(isValid);

		if (!isValid) {
			$('#responseMessage').text('Please fill all fields correctly.');
			return false;
		}

		const patientDetails = {
			gender: gender,
			age: age,
			bloodGroup: bloodGroup
		}

		const adminData = {
			name: name,
			email: email,
			username: username,
			password: password,
			patientDetails: patientDetails
		};

		$.ajax({
			url: '/user/check',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ username, email }),
			success: function(response) {
				console.log(response);
				if (response.usernameExists) {
					$('#responseMessage').text('Username already taken!');
					return;
				}

				if (response.emailExists) {
					$('#responseMessage').text('Email already taken!');
					return;
				}

				// ✅ Proceed with registration
				$.ajax({
					url: '/user/register',
					type: 'POST',
					contentType: 'application/json',
					data: JSON.stringify(adminData),
					success: function() {
						$('#signupForm')[0].reset();
						let countdown = 3;
						const redirectUrl = '/login'; // destination after countdown

						 setInterval(function() {
							if (countdown === 0) {
								clearInterval(interval);
								window.location.href = redirectUrl;
							} else {
								$('#responseMessage')
									.removeClass('text-danger')
									.addClass('text-success')
									.text(`Signup successful! Redirecting in ${countdown}...`);
								countdown--;
							}
						}, 1000);
					},
					error: function(err) {
						$('#responseMessage').text('Signup failed: ' + err.responseText);
					}
				});
			},
			error: function() {
				$('#responseMessage').text('Server error. Try again later.');
			}
		});
	});
});