<script lang="ts">
	import '../app.css';
	import { page } from '$app/state';
	import { browser } from '$app/environment';
	import { logoutUser, refreshToken } from '$lib/api/users';
	import { goto, beforeNavigate } from '$app/navigation';
	import { onMount } from 'svelte';

	// so that won't have the syntax line error
	let role: string | null = null;
	let fullName: string | null = null;
	let jwt: string | null = null;

	// Validate if user is in a page his allowed to be in
	function validatePageAccess() {
		// No localstorage, need to login again
		if (jwt == null && page.url.pathname != '/login' && page.url.pathname != '/register') {
			goto('/login');
		}

		// Admin page but no admin access
		if (page.url.pathname === '/admin' && (role == null || role != 'ROLE_ADMIN')) {
			goto('/login');
		}
	}

	// Update localstorage
	function updateLocalStorage() {
		if (browser) {
			role = localStorage.getItem('role');
			fullName = localStorage.getItem('fullName');
			jwt = localStorage.getItem('jwt');

			validatePageAccess();
		}
	}

	// Update when login -> dashboard
	beforeNavigate(() => {
		updateLocalStorage();
	});

	// Update on page load/refresh
	onMount(() => {
		updateLocalStorage();
	});

	async function logout() {
		// Clear localStorage and state first
		if (browser) {
			localStorage.clear();
		}
		role = null;
		fullName = null;
		jwt = null;

		// Try to call logout API, but don't let it block the logout process
		try {
			// Only call logout API if we have some authentication state
			if (jwt || fullName || role) {
				await logoutUser();
			}
		} catch (err) {
			console.error('Logout error:', err);
		}

		// Always redirect to login
		goto('/login');
	}

	// 1 minute intervals check/refresh token
	let refreshTokenInterval = setInterval(async () => {
		if (jwt && isExpiringSoon()) {
			const result = await refreshAccessToken();
			if (result && result.message && result.message.jwt) {
				localStorage.setItem('jwt', result.message.jwt);
				jwt = result.message.jwt;
			}
		}
	}, 60 * 1000); // check every minute

	// Check if its gonna expire (left less than 1 minute)
	function isExpiringSoon() {
		// base64 decode the jwt to get expiring date
		const { exp } = JSON.parse(atob(jwt.split('.')[1]));
		const now = Math.floor(Date.now() / 1000);
		return exp - now < 60; // less than 60s left
	}

	// Fetch call to refresh token
	async function refreshAccessToken() {
		try {
			const result = await refreshToken();
			return result;
		} catch (err) {
			// Error means token expired

			console.error('Refresh token expire error:', err);
			// Clear auth state and redirect to login
			if (browser) {
				localStorage.clear();
			}
			role = null;
			fullName = null;
			jwt = null;
			goto('/login?reason=session_expired');
		}
	}
</script>

<div class="drawer lg:drawer-open" data-theme="light">
	<input id="sidebar-toggle" type="checkbox" class="drawer-toggle" />

	<!-- Main content -->
	<div class="drawer-content bg-base-200 flex min-h-screen flex-col">
		<!-- Navbar (mobile only) -->
		{#if page.url.pathname !== '/login' && page.url.pathname !== '/register'}
			<div class="navbar bg-base-100 px-4 shadow lg:hidden">
				<label for="sidebar-toggle" class="btn btn-square btn-ghost drawer-button">
					<!-- Hamburger -->
					<svg
						xmlns="http://www.w3.org/2000/svg"
						class="h-6 w-6"
						fill="none"
						viewBox="0 0 24 24"
						stroke="currentColor"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M4 6h16M4 12h16M4 18h16"
						/>
					</svg>
				</label>
				<span class="text-primary flex-1 text-xl font-bold">TARIFF</span>
			</div>
		{/if}

		<!-- Page content -->
		<main class="flex-1 p-6">
			<slot />
		</main>

		<!-- Modern Footer -->
		<footer class="bg-base-100 mt-6 shadow-inner">
			<div class="mx-auto grid max-w-7xl grid-cols-1 gap-6 px-6 py-8 sm:grid-cols-3">
				<!-- Branding -->
				<div>
					<h2 class="text-primary text-lg font-semibold">TARIFF Dashboard</h2>
					<p class="mt-2 text-sm text-gray-500">
						Simplifying trade calculations and keeping you updated with the latest news.
					</p>
				</div>

				<!-- Quick Links -->
				<div>
					<h3 class="mb-3 text-sm font-semibold text-gray-700">Quick Links</h3>
					<ul class="space-y-2 text-sm">
						<li><a href="/" class="link link-hover">Dashboard</a></li>
						<li><a href="/tariff" class="link link-hover">Tariff Calculator</a></li>
						<li><a href="/settings" class="link link-hover">Settings</a></li>
						<li><a href="/support" class="link link-hover">Support</a></li>
					</ul>
				</div>

				<!-- Social Media -->
				<div>
					<h3 class="mb-3 text-sm font-semibold text-gray-700">Connect with us</h3>
					<div class="flex space-x-4 text-gray-500">
						<!-- Twitter -->
						<a href="https://twitter.com" target="_blank" class="hover:text-primary">
							<svg
								xmlns="http://www.w3.org/2000/svg"
								class="h-5 w-5"
								fill="currentColor"
								viewBox="0 0 24 24"
							>
								<path
									d="M23 3a10.9 10.9 0 0 1-3.14 1.53A4.48 4.48 0 0 0 22.4 1c-.88.52-1.85.9-2.88 1.1A4.52 4.52 0 0 0 16.11 0c-2.5 0-4.5 2.07-4.5 4.63 0 .36.04.72.12 1.05C7.69 5.5 4.07 3.7 1.64.9a4.7 4.7 0 0 0-.61 2.33c0 1.6.8 3.01 2.02 3.84a4.48 4.48 0 0 1-2.05-.6v.06c0 2.23 1.53 4.09 3.56 4.51a4.52 4.52 0 0 1-2.04.08c.57 1.85 2.22 3.2 4.18 3.24A9.06 9.06 0 0 1 0 19.54 12.8 12.8 0 0 0 7.07 22c8.5 0 13.15-7.3 13.15-13.63 0-.21 0-.42-.01-.62A9.73 9.73 0 0 0 23 3z"
								/>
							</svg>
						</a>
						<!-- LinkedIn -->
						<a href="https://linkedin.com" target="_blank" class="hover:text-primary">
							<svg
								xmlns="http://www.w3.org/2000/svg"
								class="h-5 w-5"
								fill="currentColor"
								viewBox="0 0 24 24"
							>
								<path
									d="M20.45 20.45h-3.55v-5.57c0-1.33-.03-3.04-1.85-3.04-1.85 0-2.14 1.44-2.14 2.94v5.67H9.36V9h3.41v1.56h.05c.48-.9 1.64-1.85 3.38-1.85 3.62 0 4.29 2.38 4.29 5.47v6.27zM5.34 7.43a2.06 2.06 0 1 1 0-4.12 2.06 2.06 0 0 1 0 4.12zM7.12 20.45H3.56V9h3.56v11.45zM22.23 0H1.77C.79 0 0 .77 0 1.72v20.55C0 23.23.79 24 1.77 24h20.45c.98 0 1.78-.77 1.78-1.73V1.72C24 .77 23.21 0 22.23 0z"
								/>
							</svg>
						</a>
						<!-- GitHub -->
						<a href="https://github.com" target="_blank" class="hover:text-primary">
							<svg
								xmlns="http://www.w3.org/2000/svg"
								class="h-5 w-5"
								fill="currentColor"
								viewBox="0 0 24 24"
							>
								<path
									d="M12 0C5.37 0 0 5.4 0 12.07c0 5.34 3.44 9.86 8.2 11.47.6.11.82-.26.82-.58 0-.28-.01-1.02-.02-2-3.34.73-4.04-1.63-4.04-1.63-.55-1.41-1.34-1.79-1.34-1.79-1.09-.76.08-.74.08-.74 1.2.09 1.83 1.25 1.83 1.25 1.07 1.86 2.8 1.32 3.49 1.01.11-.79.42-1.32.76-1.62-2.67-.31-5.47-1.35-5.47-6.01 0-1.33.47-2.41 1.24-3.26-.13-.31-.54-1.56.12-3.26 0 0 1.01-.33 3.3 1.25a11.38 11.38 0 0 1 6.01 0c2.29-1.58 3.3-1.25 3.3-1.25.66 1.7.25 2.95.12 3.26.77.85 1.24 1.93 1.24 3.26 0 4.68-2.81 5.69-5.48 5.99.43.38.82 1.13.82 2.29 0 1.65-.02 2.98-.02 3.39 0 .32.22.7.82.58C20.56 21.93 24 17.41 24 12.07 24 5.4 18.63 0 12 0z"
								/>
							</svg>
						</a>
					</div>
				</div>
			</div>

			<!-- Bottom bar -->
			<div class="border-base-300 border-t py-4 text-center text-xs text-gray-500">
				© 2025 Tariff Dashboard. All rights reserved.
			</div>
		</footer>
	</div>

	<!-- Sidebar -->
	{#if page.url.pathname !== '/login' && page.url.pathname !== '/register'}
		<div class="drawer-side">
			<label for="sidebar-toggle" class="drawer-overlay"></label>
			<aside class="bg-base-100 flex min-h-screen w-64 flex-col shadow-lg">
				<!-- Logo -->
				<div class="flex items-center px-4 py-5">
					<div class="bg-primary flex h-10 w-10 items-center justify-center rounded-lg text-white">
						<span class="text-lg font-bold">T</span>
					</div>
					<span class="ml-3 text-lg font-bold">TARIFF</span>
				</div>

				<!-- Navigation -->
				<ul class="menu w-full px-2">
					<li class="menu-title"><span>Main</span></li>
					<li><a href="/" class:active={page.url.pathname === '/'}>Dashboard</a></li>
					{#if role == 'ROLE_ADMIN'}
						<li><a href="/admin" class:active={page.url.pathname === '/admin'}>Admin</a></li>
					{/if}
					<li class="menu-title"><span>Support</span></li>
					<li><a href="/settings" class:active={page.url.pathname === '/settings'}>Settings</a></li>
					<li><a href="/support" class:active={page.url.pathname === '/support'}>Support</a></li>
				</ul>

				<!-- User Footer in Sidebar -->
				<div class="border-base-300 mt-auto border-t px-4 py-5">
					<div class="flex items-center space-x-3">
						<div class="avatar placeholder">
							<div class="bg-neutral text-neutral-content w-10 rounded-full">AD</div>
						</div>
						<div>
							<div class="font-medium">{fullName}</div>
							<div class="text-xs opacity-70">
								{role == 'ROLE_ADMIN' ? 'Administrator' : 'User'}
							</div>
						</div>
						<button class="btn btn-ghost btn-sm ml-auto" on:click={logout}>
							<!-- Logout icon -->
							<svg
								xmlns="http://www.w3.org/2000/svg"
								class="h-4 w-4"
								fill="none"
								viewBox="0 0 24 24"
								stroke="currentColor"
							>
								<path
									stroke-linecap="round"
									stroke-linejoin="round"
									stroke-width="2"
									d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"
								/>
							</svg>
						</button>
					</div>
				</div>
			</aside>
		</div>
	{/if}
</div>
