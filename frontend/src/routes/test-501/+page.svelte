<script lang="ts">
	import { goto } from '$app/navigation';
	import { error } from '@sveltejs/kit';

	// This will trigger a 501 error
	error(501, 'Not Implemented - This feature is not yet implemented');
</script>

<svelte:head>
	<title>Test 501 Error - TARIFF</title>
</svelte:head>

<div class="hero min-h-screen bg-base-200">
	<div class="hero-content text-center">
		<div class="max-w-md">
			<h1 class="text-2xl font-bold">Testing 501 Error</h1>
			<p class="mb-4">This page will trigger a 501 error...</p>
			<button class="btn btn-primary" on:click={() => goto('/')}>
				Go Back to Dashboard
			</button>
		</div>
	</div>
</div>
