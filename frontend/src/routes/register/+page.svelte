<script>
	import { goto } from '$app/navigation';
	import { registerUser } from '$lib/api/users';
	import Alert from '$lib/components/Alert.svelte';

	let firstName = '';
	let lastName = '';
	let username = '';
	let email = '';
	let password = '';
	let password2 = '';
	let isLoading = false;
	let error = '';
	let success = '';

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

	async function register() {
		if (!firstName || !lastName || !username || !email || !password || !password2) {
			error = 'Please fill in all fields before registering.';
			return;
		}

		if (!validateEmail()) {
			error = 'The email is not in a valid format.';
			return;
		}

		if (!checkPasswords()) {
			return; // Error already set by checkPasswords()
		}

		isLoading = true;
		error = '';
		success = '';

		try {
			const result = await registerUser({
				firstName,
				lastName,
				username,
				email,
				password
			});

			success = 'Registration successful! Redirecting to login...';
			
			// Redirect to login page after 2 seconds
			setTimeout(() => {
				goto('/login');
			}, 2000);

		} catch (err) {
			error = err instanceof Error ? err.message : 'Registration failed. Please try again.';
			console.error('Registration error:', err);
		} finally {
			isLoading = false;
		}
	}
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Dashboard</h1>
	
	<!-- Global Alerts - Below page title -->
	{#if error}
		<Alert 
			type="error" 
			message={error} 
			show={true}
			autoDismiss={true}
		/>
	{/if}

	{#if success}
		<Alert 
			type="success" 
			message={success} 
			show={true}
			autoDismiss={true}
		/>
	{/if}

	<!-- One-column layout -->
	<div class="flex justify-center pt-10">
		<div class="card bg-base-100 p-6 shadow-md relative">
			{#if isLoading}
				<div class="absolute inset-0 z-10 flex items-center justify-center bg-base-100/70">
					<span class="loading loading-spinner loading-lg text-primary"></span>
				</div>
			{/if}
			<h2 class="mb-1 text-lg font-semibold">Register</h2>


			<form class="space-y-4"  on:submit|preventDefault={register}>
				<!-- First Name -->
				<div class="form-control">
					<label class="label text-sm font-medium" for="firstNameInput">First Name</label>
					<input
						type="text"
						id="firstNameInput"
						bind:value={firstName}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your first name"
						required
					/>
				</div>

				<!-- Last Name -->
				<div class="form-control">
					<label class="label text-sm font-medium" for="lastNameInput">Last Name</label>
					<input
						type="text"
						id="lastNameInput"
						bind:value={lastName}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your last name"
						required
					/>
				</div>

				<!-- Username -->
				<div class="form-control">
					<label class="label text-sm font-medium" for="usernameInput">Username</label>
					<input
						type="text"
						id="usernameInput"
						bind:value={username}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your username"
						required
					/>
				</div>

				<!-- Email -->
				<div class="form-control">
					<label class="label text-sm font-medium" for="emailInput">Email</label>
					<input
						type="email"
						id="emailInput"
						bind:value={email}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your email"
						required
					/>
				</div>

				<!-- Password -->
				<div class="form-control">
					<label class="label text-sm font-medium" for="passwordInput">Password</label>
					<input
						type="password"
						id="passwordInput"
						bind:value={password}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your password"
						required
					/>
				</div>

				<div>
					<!-- to load the color classes, if not it will not work -->
					<p class="text-green-500 text-red-500 invisible"></p>
					<p class="mt-2 text-sm text-gray-500">
						Password requirements:<br />
						<span class="text-{passwordLength() ? 'green' : 'red'}-500 text-xl">{passwordLength() ? '✔' : '✖'}</span>
						Contain at least 8 characters<br />
						<span class="text-{passwordUpperLower() ? 'green' : 'red'}-500 text-xl">{passwordUpperLower() ? '✔' : '✖'}</span>
						Contain both lower and uppercase letters<br />
						<span class="text-{passwordNumber() ? 'green' : 'red'}-500 text-xl">{passwordNumber() ? '✔' : '✖'}</span>
						Contain at least 1 number<br />
						<span class="text-{passwordSpecialCharacter() ? 'green' : 'red'}-500 text-xl">{passwordSpecialCharacter() ? '✔' : '✖'}</span>
						Contain at least 1 special character<br />
					</p>
				</div>

				<!-- Password -->
				<div class="form-control">
					<label class="label text-sm font-medium" for="password2Input">Confirm Password</label>
					<input
						type="password"
						id="password2Input"
						bind:value={password2}
						class="input input-bordered w-full text-sm"
						placeholder="Enter your password again"
						required
					/>
				</div>

				<!-- Submit -->
				<div class="form-control flex justify-around">
					<button class="btn btn-primary btn-sm invisible w-1/3" aria-label="Spacer button"></button>
					<button 
						type="submit" 
						class="btn btn-primary btn-sm w-1/3"
						class:loading={isLoading}
						disabled={isLoading}
					>
						{#if isLoading}
							<span class="loading loading-spinner loading-sm text-primary-content"></span>
							Registering...
						{:else}
							Register
						{/if}
					</button>
				</div>
			</form>
		</div>
	</div>
</div>
