<script lang="ts">
	import { goto } from '$app/navigation';
	import CategoryComponent from '$lib/components/admin/Category.svelte';
	import TariffComponent from '$lib/components/admin/Tariff.svelte';
	import Alert from '$lib/components/Alert.svelte';
	import { onMount } from 'svelte';

	// Server-side data from +page.server.ts
	export let data: any;

	// Gate rendering and redirect if not authorized (prevents SSR leak/flicker)
	let authorized = false;
	onMount(() => {
		const token = localStorage.getItem('jwt');
		const role = localStorage.getItem('role');
		const isAdmin = Boolean(token) && role === 'ROLE_ADMIN';
		if (!isAdmin) goto('/error/403');
		else authorized = true;
	});

	let mode = 'tariff';

	let success = '';
	let error = '';
	let isBusy = false; // page-level busy indicator for create/edit flows

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
				categoryCode: '',
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

{#if authorized}
<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Admin</h1>
	
	<!-- Global Alerts - Below page title -->
	{#if error}
		<Alert 
			type="error" 
			message={error} 
			show={true}
			autoDismiss={true}
		/>
	{/if}

	{#if success}
		<Alert 
			type="success" 
			message={success} 
			show={true}
			autoDismiss={true}
		/>
	{/if}

	<!-- Two-column layout -->
	<div class="grid grid-cols-1">
		<!-- Tariffs Card -->
		<div class="card bg-base-100 p-6 shadow-md relative">
			{#if isBusy}
				<div class="absolute inset-0 z-10 flex items-center justify-center bg-base-100/70">
					<span class="loading loading-spinner loading-lg text-primary"></span>
				</div>
			{/if}
			<div class="flex items-center justify-between py-6">
				<div class="flex">
					<button
						class="border-primary mb-1 cursor-pointer px-2 text-lg font-semibold {mode == 'tariff'
							? 'border-b-2'
													: ''}"
						on:click={() => {
							mode = 'tariff';
						}}
						on:keydown={(e) => {
							if (e.key === 'Enter' || e.key === ' ') {
								mode = 'tariff';
							}
						}}
					>
						Tariffs
					</button>
					<button
						class="border-primary mb-1 cursor-pointer px-2 text-lg font-semibold {mode == 'category'
							? 'border-b-2'
													: ''}"
						on:click={() => {
							mode = 'category';
						}}
						on:keydown={(e) => {
							if (e.key === 'Enter' || e.key === ' ') {
								mode = 'category';
							}
						}}
					>
						Product Categories
					</button>
				</div>
			</div>
			{#if mode == 'tariff'}
				<TariffComponent bind:createTariffBoolean bind:isBusy/>
			{/if}

			{#if mode == 'category'}
				<CategoryComponent bind:createCategoryBoolean bind:isBusy/>
			{/if}
		</div>
	</div>
</div>
{/if}
