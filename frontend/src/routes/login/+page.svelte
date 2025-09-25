<script>
	import { goto } from '$app/navigation';
	import { loginUser } from '$lib/api/users';
	import { page } from '$app/stores';


	let email = '';
	let password = '';
	let isLoading = false;
	let error = '';
	let success = '';
	
	$: error = $page.url.searchParams.get('reason') === "session_expired" ? "Your session expired. Please log in again." : "";

	// Function that will use regex to validate the email
	function validateEmail() {
		var re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		return re.test(email);
	}

	async function login() {
		if (email && password) {
			if (validateEmail()) {
				try {
					const result = await loginUser({
						email,
						password
					});

					// Store into localstorage
					localStorage.setItem('fullName', result.message.fullName);
					localStorage.setItem('jwt', result.message.jwt);
					localStorage.setItem('role', result.message.role);
					localStorage.setItem('userId', result.message.userId);

					error = '';
					success = 'Login successful! Redirecting to dashboard...';

					// Redirect to login page after 2 seconds
					setTimeout(() => {
						goto('/');
					}, 2000);
				} catch (err) {
					error = err instanceof Error ? err.message : 'Login failed. Please try again.';
					console.error('Login error:', err);
				} finally {
					isLoading = false;
				}
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

			<!-- Error/Success Messages -->
			{#if error}
				<div class="alert alert-error">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						class="h-6 w-6 shrink-0 stroke-current"
						fill="none"
						viewBox="0 0 24 24"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
						/>
					</svg>
					<span>{error}</span>
				</div>
			{/if}

			{#if success}
				<div class="alert alert-success">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						class="h-6 w-6 shrink-0 stroke-current"
						fill="none"
						viewBox="0 0 24 24"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
						/>
					</svg>
					<span>{success}</span>
				</div>
			{/if}

			<form class="space-y-4" on:submit|preventDefault={login}>
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

				<!-- Submit -->
				<div class="form-control flex justify-around">
					<a href="./register" class="btn btn-primary btn-sm w-1/3">Register</a>
					<button type="submit" class="btn btn-primary btn-sm w-1/3">
						{#if isLoading}
							<span class="loading loading-spinner loading-sm"></span>
							Logging in...
						{:else}
							Login
						{/if}
					</button>
				</div>
			</form>
		</div>
	</div>
</div>
