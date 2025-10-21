<script lang="ts">
	import { goto } from '$app/navigation';
</script>

<div class="hero min-h-screen bg-base-200">
	<div class="hero-content text-center">
		<div class="max-w-md">
			<!-- Lock Icon -->
			<div class="mb-8">
				<svg
					class="mx-auto h-32 w-32 text-error"
					fill="none"
					stroke="currentColor"
					viewBox="0 0 24 24"
					xmlns="http://www.w3.org/2000/svg"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="1"
						d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"
					></path>
				</svg>
			</div>

			<!-- Error Code -->
			<h1 class="text-6xl font-bold text-error">403</h1>

			<!-- Error Message -->
			<h2 class="mb-4 text-2xl font-semibold">Access Forbidden</h2>
			<p class="mb-8 text-base-content/70">
				You don't have permission to access this resource. Please contact your administrator if you believe
				this is an error.
			</p>

			<!-- Action Buttons -->
			<div class="flex flex-col gap-4 sm:flex-row sm:justify-center">
				<button class="btn btn-primary" on:click={() => goto('/')}>
					<svg
						class="h-4 w-4"
						fill="none"
						stroke="currentColor"
						viewBox="0 0 24 24"
						xmlns="http://www.w3.org/2000/svg"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
						></path>
					</svg>
					Back to Dashboard
				</button>
				<button class="btn btn-outline" on:click={() => goto('/login')}>
					<svg
						class="h-4 w-4"
						fill="none"
						stroke="currentColor"
						viewBox="0 0 24 24"
						xmlns="http://www.w3.org/2000/svg"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1"
						></path>
					</svg>
					Login
				</button>
			</div>
		</div>
	</div>
</div>
