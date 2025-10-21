<script lang="ts">
	import { goto } from '$app/navigation';
	import CategoryComponent from '$lib/components/admin/Category.svelte';
	import TariffComponent from '$lib/components/admin/Tariff.svelte';
	import { onMount } from 'svelte';

	// Check authentication on page load
	onMount(() => {
		const token = localStorage.getItem('jwt'); // Use same key as layout
		const role = localStorage.getItem('role');
		
		if (!token || role !== 'ROLE_ADMIN') { // Use same role format as layout
			// Redirect to 403 page for unauthorized access
			goto('/error/403');
		}
	});

	let mode = 'tariff';

	let success = '';
	let error = '';

	let createTariffBoolean = false;
	let createCategoryBoolean = false;

	type ProductCategory = {
		categoryCode: string;
		categoryName: string;
		description: string;
		id: number;
		isActive: boolean;
	};

	type TariffRecord = {
		id: number;
		createdAt: string; // ISO date string
		updatedAt: string; // ISO date string
		effectiveDate: string; // ISO date string
		expiryDate: string; // ISO date string
		exportingCountryCode: string;
		importingCountryCode: string;
		preferentialTariff: boolean;
		productCategory: ProductCategory;
		rateUnit: string;
		tariffRate: number;
		tariffType: string;
	};
	let allTariff: TariffRecord[] = [];
	let selectedTariff: TariffRecord = blankTariff();
	function blankTariff() {
		return {
			id: 0,
			createdAt: '',
			updatedAt: '',
			effectiveDate: '',
			expiryDate: '',
			exportingCountryCode: '',
			importingCountryCode: '',
			preferentialTariff: false,
			productCategory: {
				categoryCode: 0,
				categoryName: '',
				description: '',
				id: 0,
				isActive: false
			},
			rateUnit: '',
			tariffRate: 0,
			tariffType: ''
		};
	}

	let create = false;

	function createButton() {
		if (mode == 'tariff') {
			createTariffBoolean = true;
		} else {
			createCategoryBoolean = true;
		}
	}
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Admin</h1>

	{#if error}
		<div class="alert alert-error">
			<svg
				xmlns="http://www.w3.org/2000/svg"
				class="h-6 w-6 shrink-0 stroke-current"
				fill="none"
				viewBox="0 0 24 24"
			>
				<path
					stroke-linecap="round"
					stroke-linejoin="round"
					stroke-width="2"
					d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
				/>
			</svg>
			<span>{error}</span>
		</div>
	{/if}

	{#if success}
		<div class="alert alert-success">
			<svg
				xmlns="http://www.w3.org/2000/svg"
				class="h-6 w-6 shrink-0 stroke-current"
				fill="none"
				viewBox="0 0 24 24"
			>
				<path
					stroke-linecap="round"
					stroke-linejoin="round"
					stroke-width="2"
					d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
				/>
			</svg>
			<span>{success}</span>
		</div>
	{/if}

	<!-- Two-column layout -->
	<div class="grid grid-cols-1">
		<!-- Tariffs Card -->
		<div class="card bg-base-100 p-6 shadow-md">
			<div class="flex items-center justify-between py-6">
				<div class="flex">
					<h2
						class="border-primary mb-1 cursor-pointer px-2 text-lg font-semibold {mode == 'tariff'
							? 'border-b-2'
													: ''}"
												on:click={() => {
							mode = 'tariff';
						}}
					>
						Tariffs
					</h2>
					<h2
						class="mb-1 cursor-pointer px-2 text-lg font-semibold {mode == 'category'
							? 'border-b-2'
													: ''}"
												on:click={() => {
							mode = 'category';
						}}
					>
						Product Categories
					</h2>
											</div>
								</div>
			{#if mode == 'tariff'}
				<TariffComponent bind:createTariffBoolean />
					{/if}

			{#if mode == 'category'}
				<CategoryComponent bind:createCategoryBoolean />
			{/if}
		</div>
	</div>
</div>
