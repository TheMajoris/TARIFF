<script>
	let firstName = '';
	let lastName = '';
	let username = '';
	let email = '';
	let password = '';
	let password2 = '';

	// Function that will use regex to validate the email
	function validateEmail() {
		var re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		return re.test(email);
	}

	// Function to check password length more than or equals to 8
	function passwordLength() {
		return password.length >= 8;
	}

	// Function to check password has both upper and lower characters
	function passwordUpperLower() {
		let hasUpper = /[A-Z]/.test(password);
		let hasLower = /[a-z]/.test(password);

		return hasUpper && hasLower;
	}

	// Function to check password has atleast 1 number
	function passwordNumber(){
		let hasNumber = /\d/.test(password);

		return hasNumber;
	}

	function passwordSpecialCharacter(){
		let hasSpecialCharacter = /[!@#$%^&*(),.?":{}|<>]/.test(password);

		return hasSpecialCharacter;
	}

	// Function to check that the passwords are valid and identical
	function checkPasswords() {
		// check if the passwords are the same
		if (password === password2) {
			if (passwordLength() && passwordNumber() && passwordSpecialCharacter() && passwordUpperLower()) {
				return true;
			} else {
				alert('The password need to fulfil all of the requirements.');
				return false;
			}
		} else {
			alert('The passwords provided are different.');
			return false;
		}
	}

	function register() {
		if (firstName && lastName && username && email && password && password2) {
			if (validateEmail()) {
				if (checkPasswords()) {
					alert(`register:
  Username: ${username}
  Email: ${email}
  Password: ${password}
  Password2: ${password2}`);
				}
			} else {
				alert('The email is not in a valid format.');
			}
		} else {
			alert('Please fill in all fields before registering.');
		}
	}
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Dashboard</h1>

	<!-- One-column layout -->
	<div class="flex justify-center pt-10">
		<div class="card bg-base-100 p-6 shadow-md">
			<h2 class="mb-1 text-lg font-semibold">Register</h2>

			<form class="space-y-4">
				<!-- First Name -->
				<div class="form-control">
					<label class="label text-sm font-medium">First Name</label>
					<input
						type="text"
						bind:value={firstName}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your first name"
						required
					/>
				</div>

				<!-- Last Name -->
				<div class="form-control">
					<label class="label text-sm font-medium">Last Name</label>
					<input
						type="text"
						bind:value={lastName}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your last name"
						required
					/>
				</div>

				<!-- Username -->
				<div class="form-control">
					<label class="label text-sm font-medium">Username</label>
					<input
						type="text"
						bind:value={username}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your username"
						required
					/>
				</div>

				<!-- Email -->
				<div class="form-control">
					<label class="label text-sm font-medium">Email</label>
					<input
						type="email"
						bind:value={email}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your email"
						required
					/>
				</div>

				<!-- Password -->
				<div class="form-control">
					<label class="label text-sm font-medium">Password</label>
					<input
						type="password"
						bind:value={password}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your password"
						required
					/>
				</div>

				<div>
					<p class="mt-2 text-sm text-gray-500">
						Password requirements:<br />
						<span class="text-{passwordLength() ? 'green' : 'red'}-500 text-xl">✖</span>
						Contain at least 8 characters<br />
						<span class="text-{passwordUpperLower() ? 'green' : 'red'}-500 text-xl">✖</span>
						Contain both lower and uppercase letters<br />
						<span class="text-{passwordNumber() ? 'green' : 'red'}-500 text-xl">✖</span>
						Contain at least 1 number<br />
						<span class="text-{passwordSpecialCharacter() ? 'green' : 'red'}-500 text-xl">✖</span>
						Contain at least 1 special character<br />
					</p>
				</div>

				<!-- Password -->
				<div class="form-control">
					<label class="label text-sm font-medium">Confirm Password</label>
					<input
						type="password"
						bind:value={password2}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your password again"
						required
					/>
				</div>

				<!-- Submit -->
				<div class="form-control flex justify-around">
					<button class="btn btn-primary btn-sm invisible w-1/3"></button>
					<button type="button" onclick={register} class="btn btn-primary btn-sm w-1/3"
						>Register</button
					>
				</div>
			</form>
		</div>
	</div>
</div>
