<script lang="ts">
	import { goto } from '$app/navigation';
</script>

<div class="hero min-h-screen bg-base-200">
	<div class="hero-content text-center">
		<div class="max-w-md">
			<!-- Warning Triangle Icon -->
			<div class="mb-8">
				<svg
					class="mx-auto h-32 w-32 text-warning"
					fill="none"
					stroke="currentColor"
					viewBox="0 0 24 24"
					xmlns="http://www.w3.org/2000/svg"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="1"
						d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"
					></path>
				</svg>
			</div>

			<!-- Error Code -->
			<h1 class="text-6xl font-bold text-warning">501</h1>

			<!-- Error Message -->
			<h2 class="mb-4 text-2xl font-semibold">Server Error</h2>
			<p class="mb-8 text-base-content/70">
				The server encountered an unexpected error. Please try again later or contact support if the
				problem persists.
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
				<button class="btn btn-outline" on:click={() => window.location.reload()}>
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
							d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
						></path>
					</svg>
					Try Again
				</button>
			</div>
		</div>
	</div>
</div>
