<script>
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import { loginUser } from '$lib/api/users';
	import Alert from '$lib/components/Alert.svelte';


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
				isLoading = true;
				error = '';
				success = '';
				
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
				error = 'The email is not in a valid format.';
			}
		} else {
			error = 'Please fill in all fields before logging in.';
		}
	}
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Dashboard</h1>

	<!-- One-column layout -->
	<div class="flex justify-center pt-10">
		<div class="card bg-base-100 p-6 shadow-md relative">
			{#if isLoading}
				<div class="absolute inset-0 z-10 flex items-center justify-center bg-base-100/70">
					<span class="loading loading-spinner loading-lg text-primary"></span>
				</div>
			{/if}
			<h2 class="mb-1 text-lg font-semibold">Login</h2>

			<!-- Error/Success Messages -->
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
					<button type="submit" class="btn btn-primary btn-sm w-1/3" disabled={isLoading}>
						{#if isLoading}
							<span class="loading loading-spinner loading-sm text-primary-content"></span>
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
