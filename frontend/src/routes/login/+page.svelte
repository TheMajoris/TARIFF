<script>
	let email = '';
	let password = '';
	let rememberMe = false;

	// Function that will use regex to validate the email
	function validateEmail() {
		var re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		return re.test(email);
	}

	function login() {
		if (email && password) {
			if (validateEmail()) {
				alert(`login:
  Username: ${email}
  Password: ${password}
  Remember me: ${rememberMe}`);
			} else {
				alert('The email is not in a valid format.');
			}
		} else {
			alert('Please fill in all fields before logging in.');
		}
	}
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Dashboard</h1>

	<!-- One-column layout -->
	<div class="flex justify-center pt-10">
		<div class="card bg-base-100 p-6 shadow-md">
			<h2 class="mb-1 text-lg font-semibold">Login</h2>

			<form class="space-y-4"  on:submit|preventDefault={login}>
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

				<!-- Remember Me -->
				<div class="form-control">
					<label class="label text-sm font-medium">
						<input type="checkbox" bind:checked={rememberMe} class="checkbox" />
						Remember me
					</label>
				</div>

				<!-- Submit -->
				<div class="form-control flex justify-around">
					<a href="./register" class="btn btn-primary btn-sm w-1/3">Register</a>
					<button type="submit" class="btn btn-primary btn-sm w-1/3">Login</button>
				</div>
			</form>
		</div>
	</div>
</div>
