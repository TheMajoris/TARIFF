<script>
	import { getCalculations } from '$lib/api/calculationHistory.js';
	import Alert from '$lib/components/Alert.svelte';
	import { onMount } from 'svelte';

	let calculations = [];
	let loading = true;
	let errorMessage = '';

	onMount(async () => {
		await loadCalculations();
	});

	async function loadCalculations() {
		loading = true;
		errorMessage = '';
		try {
			const result = await getCalculations();

			// Handle the GenericResponse structure from backend
			if (result.data) {
				calculations = result.data;
			} else {
				calculations = [];
			}

			console.log('Loaded calculations:', calculations);
		} catch (error) {
			console.error('Error loading calculations:', error);
			errorMessage = error.message || 'Failed to load calculations';
			calculations = [];
		} finally {
			loading = false;
		}
	}

	async function refreshCalculations() {
		await loadCalculations();
	}
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<div class="flex items-center justify-between">
		<div>
			<h1 class="text-primary text-2xl font-semibold">Calculation History</h1>
			<p class="text-sm text-gray-500">View and manage your saved tariff calculations</p>
		</div>
		<button class="btn btn-primary btn-sm" on:click={refreshCalculations}>
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
					d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
				/>
			</svg>
			Refresh
		</button>
	</div>

	<!-- Alerts -->
	{#if errorMessage}
		<Alert type="error" message={errorMessage} show={true} autoDismiss={true} />
	{/if}

	<!-- Loading State -->
	{#if loading}
		<div class="flex items-center justify-center py-12">
			<span class="loading loading-spinner loading-lg text-primary"></span>
		</div>
	{:else if calculations.length === 0}
		<!-- Empty State -->
		<div class="card bg-base-100 flex flex-col items-center justify-center p-12 shadow-md">
			<svg
				xmlns="http://www.w3.org/2000/svg"
				class="mb-4 h-16 w-16 text-gray-400"
				fill="none"
				viewBox="0 0 24 24"
				stroke="currentColor"
			>
				<path
					stroke-linecap="round"
					stroke-linejoin="round"
					stroke-width="2"
					d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
				/>
			</svg>
			<h3 class="mb-2 text-lg font-semibold text-gray-700">No Saved Calculations</h3>
			<p class="mb-4 text-sm text-gray-500">
				You haven't saved any calculations yet. Start by calculating a tariff on the dashboard.
			</p>
			<a href="/" class="btn btn-primary btn-sm">Go to Calculator</a>
		</div>
	{:else}
		<!-- Calculations Grid -->
		<div class="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
			{#each calculations as calculation (calculation.id)}
				<div class="card bg-base-100 relative shadow-md transition-shadow hover:shadow-lg">
					<div class="card-body">
						<!-- Calculation Name -->
						<h3 class="card-title text-base">{calculation.calculationName}</h3>

						<!-- Calculation Details -->
						<div class="space-y-2 text-sm">
							<!-- HS Code and Product Category -->
							<div class="flex justify-between">
								<span class="text-gray-500">HS Code:</span>
								<span class="font-medium">{calculation.productCategoryCode}</span>
							</div>

							<!-- Countries -->
							<div class="flex justify-between">
								<span class="text-gray-500">Route:</span>
								<span class="font-medium"
									>{calculation.exportingCountryCode} → {calculation.importingCountryCode}</span
								>
							</div>

							<!-- Product Value -->
							<div class="flex justify-between">
								<span class="text-gray-500">Product Value:</span>
								<span class="font-medium"
									>{calculation.currencyCode}
									{parseFloat(calculation.productValue).toFixed(2)}</span
								>
							</div>

							<!-- Product Quantity -->
							<div class="flex justify-between">
								<span class="text-gray-500">Product Quantity:</span>
								<span class="font-medium"> {calculation.productQuantity}</span>
							</div>

							<!-- Tariff Rate -->
							<div class="flex justify-between">
								<span class="text-gray-500">Tariff Rate:</span>
								<span class="font-medium text-blue-600"
									>{calculation.tariffType == 'specific' ? '$' : ''}{parseFloat(
										calculation.tariffRate
									).toFixed(2)}{calculation.tariffType == 'specific'
										? '/' + parseFloat(calculation.unitQuantity).toFixed(2) + calculation.rateUnit
										: '%'}</span
								>
							</div>

							<!-- Tariff Type -->
							<div class="flex justify-between">
								<span class="text-gray-500">Type:</span>
								<span class="badge badge-info badge-sm"
									>{calculation.tariffType == 'ad_valorem' ? 'Ad Valorem' : 'Specific'}</span
								>
							</div>

							<!-- Tariff Cost -->
							<div class="flex justify-between">
								<span class="text-gray-500">Tariff Cost:</span>
								<span class="font-medium text-red-600"
									>{calculation.currencyCode}
									{parseFloat(calculation.calculatedTariffCost).toFixed(2)}</span
								>
							</div>

							<!-- Total Cost -->
							<div class="border-base-300 flex justify-between border-t pt-2">
								<span class="font-semibold">Total Cost:</span>
								<span class="text-primary font-bold"
									>{calculation.currencyCode} {parseFloat(calculation.totalCost).toFixed(2)}</span
								>
							</div>

							<!-- Notes -->
							{#if calculation.notes}
								<div class="border-base-300 border-t pt-2">
									<p class="text-xs text-gray-500">Notes:</p>
									<p class="text-xs">{calculation.notes}</p>
								</div>
							{/if}

							<!-- Date -->
							<div class="text-xs text-gray-400">
								Saved: {new Date(calculation.createdAt).toLocaleDateString()}
							</div>
						</div>
					</div>
				</div>
			{/each}
		</div>
	{/if}
</div>
