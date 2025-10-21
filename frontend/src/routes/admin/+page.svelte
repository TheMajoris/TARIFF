<script lang="ts">
	import { createTariff, deleteSpecificTariff, editTariff, getAllTariff } from '$lib/api/tariff';
	import { beforeNavigate } from '$app/navigation';
	import { onMount } from 'svelte';
	import { fetchCountries } from '$lib/api/countries.js';

	import TariffComponent from '$lib/components/admin/Tariff.svelte';

	let success = '';
	let error = '';

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

	// Function to validate Category
	function CategoryValidation() {
		if (
			selectedTariff.productCategory.categoryCode != null &&
			/^\d{6}$/.test(selectedTariff.productCategory.categoryCode)
		) {
			if (
				selectedTariff.productCategory.categoryName != '' &&
				selectedTariff.productCategory.categoryName.length <= 100
			) {
				if (selectedTariff.productCategory.description.length <= 500) {
					return true;
				} else {
					error = 'Category Description can only have up to 500 characters';
				}
			} else {
				error = 'Category Name can only have up to 500 characters';
			}
		} else {
			error = 'Category Code can only be from 100000 to 999999';
		}

		return false;
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
				<h2 class="mb-1 text-lg font-semibold">Tariffs</h2>
				<button class="btn btn-primary" on:click={() => (create = true)}>Create</button>
			</div>
			<TariffComponent bind:create />
		</div>
	</div>
</div>
