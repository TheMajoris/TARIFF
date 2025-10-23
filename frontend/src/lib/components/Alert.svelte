<script>
	import { onDestroy } from 'svelte';

	export let type = 'info'; // 'success', 'error', 'warning', 'info'
	export let message = '';
	export let show = false;
	export let autoDismiss = true;

	let alertClass = '';
	let iconSvg = '';
	let timeoutId = null;

	// Set alert styling and icon based on type
	$: {
		switch (type) {
			case 'success':
				alertClass = 'alert-success';
				iconSvg = `<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
				</svg>`;
				break;
			case 'error':
				alertClass = 'alert-error';
				iconSvg = `<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
				</svg>`;
				break;
			case 'warning':
				alertClass = 'alert-warning';
				iconSvg = `<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
				</svg>`;
				break;
			case 'info':
			default:
				alertClass = 'alert-info';
				iconSvg = `<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
				</svg>`;
				break;
		}
	}


	// Auto-dismiss functionality
	$: if (show && autoDismiss && message) {
		// Clear existing timeout
		if (timeoutId) {
			clearTimeout(timeoutId);
		}
		
		// Set new timeout based on type
		const delay = type === 'error' ? 5000 : 3000; // 5s for errors, 3s for others
		
		timeoutId = setTimeout(() => {
			show = false;
		}, delay);
	}

	// Clean up timeout on component destruction
	onDestroy(() => {
		if (timeoutId) {
			clearTimeout(timeoutId);
		}
	});

	// Function to manually dismiss
	function dismiss() {
		show = false;
		if (timeoutId) {
			clearTimeout(timeoutId);
			timeoutId = null;
		}
	}
</script>

{#if show && message}
	<div class="alert {alertClass} mb-4">
		{@html iconSvg}
		<span class="flex-1">{message}</span>
		<button 
			class="btn btn-sm btn-ghost btn-circle" 
			on:click={dismiss}
			aria-label="Close alert"
		>
			<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
				<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
			</svg>
		</button>
	</div>
{/if}
