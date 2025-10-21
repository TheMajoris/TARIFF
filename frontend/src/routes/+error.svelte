<script lang="ts">
	import { goto } from '$app/navigation';
	import { page } from '$app/state';
	
	// Get error details from the page state
	$: status = page.status;
	$: message = page.error?.message || 'An unexpected error occurred';

	// Determine which error page to show based on status
	$: errorType = (() => {
		switch (status) {
			case 404:
				return '404';
			case 403:
				return '403';
			case 500:
			case 501:
			case 502:
			case 503:
				return '501';
			default:
				return 'generic';
		}
	})();
</script>

<svelte:head>
	<title>Error {status} - TARIFF</title>
</svelte:head>

<div class="hero min-h-screen bg-base-200">
	<div class="hero-content text-center">
		<div class="max-w-md">
			<!-- Dynamic Icon based on error type -->
			<div class="mb-8">
				{#if errorType === '404'}
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
				{:else if errorType === '403'}
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
				{:else}
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
				{/if}
			</div>

			<!-- Main Message -->
			<h1 class="mb-4 text-4xl font-bold {errorType === '404' ? 'text-primary' : errorType === '403' ? 'text-error' : 'text-warning'}">
				{#if errorType === '404'}
					Page Not Found
				{:else if errorType === '403'}
					Access Denied
				{:else}
					Service Unavailable
				{/if}
			</h1>
			<p class="mb-2 text-lg text-base-content/80">
				{#if errorType === '404'}
					The page you're looking for doesn't exist or has been moved.
				{:else if errorType === '403'}
					You don't have permission to access this resource.
				{:else}
					This feature is temporarily unavailable or not yet implemented.
				{/if}
			</p>
			<p class="mb-8 text-base-content/70">
				{#if errorType === '404'}
					Please check the URL or return to the dashboard.
				{:else if errorType === '403'}
					If you need access, please contact your administrator.
				{:else}
					Please try again later or contact support if the problem persists.
				{/if}
			</p>

			<!-- Technical Details (Collapsed) -->
			<details class="mb-8 text-left">
				<summary class="cursor-pointer text-sm text-base-content/60 hover:text-base-content/80">
					Technical Details
				</summary>
				<div class="mt-2 rounded bg-base-300 p-3 text-xs text-base-content/70">
					<div class="mb-1 font-mono">Error Code: {status}</div>
					<div class="mb-1 font-mono">Status: {errorType === '404' ? 'Not Found' : errorType === '403' ? 'Forbidden' : 'Not Implemented'}</div>
					{#if message && message !== 'An unexpected error occurred'}
						<div class="mt-2 pt-2 border-t border-base-content/20">
							<div class="text-xs text-base-content/60">Error Message:</div>
							<div class="text-xs font-mono">{message}</div>
						</div>
					{/if}
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
				{#if errorType === '403'}
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
				{:else if errorType !== '404'}
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
				{:else}
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
				{/if}
			</div>
		</div>
	</div>
</div>
