<script>
	import '../app.css';
	import { page } from '$app/state';
</script>

<div class="drawer lg:drawer-open" data-theme="light">
	<input id="sidebar-toggle" type="checkbox" class="drawer-toggle" />

	<!-- Main content -->
	<div class="drawer-content bg-base-200 flex min-h-screen flex-col">
		<!-- Navbar (mobile only) -->
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
								<path d="M24 4.557a9.83 9.83 0 01-2.828.775 ..." />
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
								<path d="M19 0h-14c-2.761 ..." />
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
								<path d="M12 .297c-6.63 0-12 5.373 ..." />
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
				<li><a href="/tariff" class:active={page.url.pathname === '/tariff'}>Tariff</a></li>
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
						<div class="font-medium">Admin User</div>
						<div class="text-xs opacity-70">Administrator</div>
					</div>
					<button class="btn btn-ghost btn-sm ml-auto">
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
</div>
