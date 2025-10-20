<script>
	import { browser } from '$app/environment';
	import { onMount } from 'svelte';

	let theme = 'light';
	let themes = ['light', 'dark'];

	let fullName = '';
	let role = '';
	let userId = '';

	function applyTheme(value) {
		if (!browser) return;
		document.documentElement.setAttribute('data-theme', value);
		localStorage.setItem('theme', value);
	}

	function initTheme() {
		if (!browser) return;
		const saved = localStorage.getItem('theme');
		theme = saved || theme;
		applyTheme(theme);
	}

	onMount(() => {
		initTheme();
		if (browser) {
			fullName = localStorage.getItem('fullName') || '';
			role = localStorage.getItem('role') || '';
			userId = localStorage.getItem('userId') || '';
		}
	});
</script>

<div class="space-y-6 p-6">
	<h1 class="text-primary text-2xl font-semibold">Settings</h1>

	<div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
		<!-- Theme Settings -->
		<div class="card bg-base-100 p-6 shadow-md">
			<h2 class="mb-2 text-lg font-semibold">Appearance</h2>
			<p class="mb-4 text-xs text-gray-500">Choose a theme for the application</p>
			<div class="form-control">
				<label class="label text-sm font-medium">Theme</label>
				<select bind:value={theme} class="select select-bordered w-full max-w-xs" on:change={(e) => applyTheme(e.target.value)}>
					{#each themes as t}
						<option value={t}>{t}</option>
					{/each}
				</select>
			</div>
		</div>

		<!-- Account Info -->
		<div class="card bg-base-100 p-6 shadow-md">
			<h2 class="mb-2 text-lg font-semibold">Account</h2>
			<p class="mb-4 text-xs text-gray-500">Your profile information</p>
			<div class="space-y-2 text-sm">
				<div class="flex justify-between"><span class="text-base-content/70">Full name</span><span class="font-medium">{fullName || '—'}</span></div>
				<div class="flex justify-between"><span class="text-base-content/70">Role</span><span class="font-medium">{role || '—'}</span></div>
				<div class="flex justify-between"><span class="text-base-content/70">User ID</span><span class="font-medium">{userId || '—'}</span></div>
			</div>
		</div>
	</div>
</div>
