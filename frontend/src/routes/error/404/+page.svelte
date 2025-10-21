<script lang="ts">
	import { goto } from '$app/navigation';
</script>

<div class="hero min-h-screen bg-base-200">
	<div class="hero-content text-center">
		<div class="max-w-md">
			<!-- Search Icon -->
			<div class="mb-8">
				<svg
					class="mx-auto h-32 w-32 text-primary"
					fill="none"
					stroke="currentColor"
					viewBox="0 0 24 24"
					xmlns="http://www.w3.org/2000/svg"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="1"
						d="M9.172 16.172a4 4 0 015.656 0M9 12h6m-6-4h6m2 5.291A7.962 7.962 0 0112 15c-2.34 0-4.29-1.009-5.824-2.709M15 12a3 3 0 11-6 0 3 3 0 016 0z"
					></path>
				</svg>
			</div>

			<!-- Main Message -->
			<h1 class="mb-4 text-4xl font-bold text-primary">Page Not Found</h1>
			<p class="mb-2 text-lg text-base-content/80">
				The page you're looking for doesn't exist or has been moved.
			</p>
			<p class="mb-8 text-base-content/70">
				Please check the URL or return to the dashboard.
			</p>

			<!-- Technical Details (Collapsed) -->
			<details class="mb-8 text-left">
				<summary class="cursor-pointer text-sm text-base-content/60 hover:text-base-content/80">
					Technical Details
				</summary>
				<div class="mt-2 rounded bg-base-300 p-3 text-xs text-base-content/70">
					<div class="mb-1 font-mono">Error Code: 404</div>
					<div class="font-mono">Status: Not Found</div>
				</div>
			</details>

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
				<button class="btn btn-outline" on:click={() => window.history.back()}>
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
							d="M10 19l-7-7m0 0l7-7m-7 7h18"
						></path>
					</svg>
					Go Back
				</button>
			</div>
		</div>
	</div>
</div>
