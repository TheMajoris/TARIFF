<!-- Calculator Logic-->
<script>
	import { fetchCountries } from '$lib/api/countries.js';
	import { calculateTariffCost } from '$lib/api/tariff.js';
	import { onMount } from 'svelte';

	let hsCode = '';
	let exportFrom = '';
	let importTo = '';
	let calculationDate = new Date().toISOString().split('T')[0]; // Set the Calculation Date
	let goodsValue = '';
	let quantity = '';

	// Search state for countries
	let exportFromSearch = '';
	let importToSearch = '';
	let showExportFromDropdown = false;
	let showImportToDropdown = false;

	let countries = [];
	onMount(async () => {
		console.log('Fetching countries...');
		countries = await fetchCountries();
		console.log('Countries loaded:', countries);
	});

	// Filter countries for search
	$: filteredExportFromCountries = countries.filter(
		(country) =>
			country.name.toLowerCase().includes(exportFromSearch.toLowerCase()) ||
			country.code.toLowerCase().includes(exportFromSearch.toLowerCase())
	);

	$: filteredImportToCountries = countries.filter(
		(country) =>
			country.name.toLowerCase().includes(importToSearch.toLowerCase()) ||
			country.code.toLowerCase().includes(importToSearch.toLowerCase())
	);

	// Format Currency
	function formatCurrency() {
		if (goodsValue) {
			goodsValue = parseFloat(goodsValue).toFixed(2);
		}
	}

	// Format Quantity
	function formatQuantity() {
		if (quantity) {
			quantity = parseFloat(quantity).toFixed(2);
		}
	}

	// Start: Tariff Calculation Section
	let calculationResult = null;
	let calculationError = null;
	let showErrorAlert = false;
	let isCalculating = false;

	async function calculateCost() {
		// Clear previous results and errors
		calculationResult = null;
		calculationError = null;
		showErrorAlert = false;
		isCalculating = true;

		if (hsCode && exportFrom && importTo && calculationDate && goodsValue && quantity) {
			// Validate HS Code format (basic validation)
			if (!/^\d{6}$/.test(hsCode)) {
				calculationError = 'Please enter a valid HS Code format (6 digits, e.g., 850110)';
				showErrorAlert = true;
				isCalculating = false;
				return;
			}

			try {
				const result = await calculateTariffCost({
					hsCode,
					exportFrom,
					importTo,
					calculationDate,
					goodsValue,
					quantity
				});

				console.log('Calculation result:', result);

				if (result === null) {
					calculationError =
						'No tariff data found for the specified countries and product. Please check your selection or contact support.';
					showErrorAlert = true;
				} else {
					// Check if this is a "no data" case (tariff rate is -1)
					const tariffRate = parseFloat(result.tariffRate);

					if (tariffRate === -1) {
						// No tariff data found in database
						calculationError =
							'No tariff data found for the specified countries and product. Please check your selection or contact support.';
						showErrorAlert = true;
					} else {
						// Valid tariff data (including 0% tariff)
						calculationResult = result;
					}
				}
			} catch (error) {
				console.error('Calculation error:', error);
				calculationError =
					error.message || 'An error occurred while calculating the tariff. Please try again.';
				showErrorAlert = true;
			}
		} else {
			calculationError = 'Please fill in all fields before calculating.';
			showErrorAlert = true;
		}

		isCalculating = false;
	}
	// End: Tariff Calculation Section

	// Start: Related News Section
	let selectedArticle = null;

	// Dummy news
	let news = [
		{
			id: 1,
			title: 'Singapore updates tariff on electronics',
			date: '2025-09-01',
			summary: 'New tariff rates apply to imported electronic devices.',
			details:
				'The Ministry of Trade has announced that tariffs on electronic imports will increase by 5% starting October 2025. This aims to protect local manufacturers.',
			link: 'https://example.com/news/singapore-electronics'
		},
		{
			id: 2,
			title: 'USA-China trade agreement reduces textile tariffs',
			date: '2025-08-28',
			summary: 'Major tariff cuts expected in textile trade.',
			details:
				'The USA and China signed a trade agreement lowering tariffs on textiles. This benefits small and medium traders.',
			link: 'https://example.com/news/usa-china-textiles'
		},
		{
			id: 3,
			title: 'Japan introduces environmental tariff policies',
			date: '2025-08-20',
			summary: 'Green taxes applied to high-carbon industries.',
			details:
				'Japan announced new tariffs on high-carbon imports to align with climate commitments.',
			link: 'https://example.com/news/japan-green-tariffs'
		}
	];
	// End: Related News Section
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Calculator</h1>
	<p class="text-sm text-gray-500">
		Enter HS Code, select countries, and calculate the cost of importing goods.
	</p>

	<!-- Two-column layout -->
	<div class="grid grid-cols-1 gap-8 lg:grid-cols-2">
		<!-- Tariff Calculator Card -->
		<div class="card bg-base-100 relative p-6 shadow-md">
			{#if isCalculating}
				<div class="bg-base-100/70 absolute inset-0 z-10 flex items-center justify-center">
					<span class="loading loading-spinner loading-lg text-primary"></span>
				</div>
			{/if}
			<h2 class="mb-1 text-lg font-semibold">Tariff Calculator</h2>
			<p class="mb-4 text-xs text-gray-500">Calculate the cost of importing goods</p>

			<form class="space-y-4" on:submit|preventDefault={calculateCost}>
				<!-- HS Code Input -->
				<div class="form-control">
					<label class="label text-sm font-medium">HS Code</label>
					<input
						type="text"
						placeholder="Enter HS Code (e.g., 850110)"
						bind:value={hsCode}
						class="input input-bordered w-full text-sm"
						disabled={isCalculating}
						required
					/>
					<label class="label">
						<span class="label-text-alt text-xs text-gray-500">
							Harmonized System Code for product classification
						</span>
					</label>
				</div>

				<!-- Importing To -->
				<div class="form-control mt-4">
					<label class="label text-sm font-medium">Importing To</label>
					<div class="relative">
						<div
							class="select select-bordered flex w-full cursor-pointer items-center justify-between text-sm {isCalculating
								? 'cursor-not-allowed opacity-50'
								: ''}"
							on:click={() => !isCalculating && (showImportToDropdown = !showImportToDropdown)}
							on:blur={(e) => {
								if (!e.relatedTarget || !e.relatedTarget.closest('.dropdown-panel')) {
									setTimeout(() => (showImportToDropdown = false), 200);
								}
							}}
							tabindex="0"
						>
							<span class="truncate">
								{#if importTo}
									{(() => {
										const selected = countries.find((c) => c.id == importTo);
										return selected ? `(${selected.code}) ${selected.name}` : 'Select country';
									})()}
								{:else}
									Select country
								{/if}
							</span>
						</div>

						{#if showImportToDropdown}
							<div
								class="dropdown-panel bg-base-100 border-base-300 absolute left-0 right-0 top-full z-20 mt-1 rounded-md border shadow-lg"
								on:click={(e) => e.stopPropagation()}
								on:mousedown={(e) => e.stopPropagation()}
							>
								<div class="border-base-300 border-b p-2">
									<input
										type="text"
										placeholder="Type to search..."
										bind:value={importToSearch}
										class="input input-sm w-full"
										on:input={() => (showImportToDropdown = true)}
										on:keydown={(e) => e.stopPropagation()}
										on:click={(e) => e.stopPropagation()}
										on:mousedown={(e) => e.stopPropagation()}
										autofocus
									/>
								</div>
								<div class="max-h-60 overflow-y-auto">
									{#each filteredImportToCountries as country}
										<div
											class="hover:bg-base-200 flex cursor-pointer items-center justify-between px-3 py-2 text-sm {importTo ==
											country.id
												? 'bg-primary text-primary-content'
												: ''}"
											on:click={() => {
												importTo = country.id;
												importToSearch = '';
												showImportToDropdown = false;
											}}
										>
											<span>({country.code}) {country.name}</span>
											{#if importTo == country.id}
												<svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
													<path
														fill-rule="evenodd"
														d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
														clip-rule="evenodd"
													></path>
												</svg>
											{/if}
										</div>
									{/each}
									{#if filteredImportToCountries.length === 0}
										<div class="text-base-content/60 px-3 py-2 text-sm">No countries found</div>
									{/if}
								</div>
							</div>
						{/if}
					</div>
				</div>

				<!-- Exporting From -->
				<div class="form-control">
					<label class="label text-sm font-medium">Exporting From</label>
					<div class="relative">
						<div
							class="select select-bordered flex w-full cursor-pointer items-center justify-between text-sm"
							on:click={() => (showExportFromDropdown = !showExportFromDropdown)}
							on:blur={(e) => {
								if (!e.relatedTarget || !e.relatedTarget.closest('.dropdown-panel')) {
									setTimeout(() => (showExportFromDropdown = false), 200);
								}
							}}
							tabindex="0"
						>
							<span class="truncate">
								{#if exportFrom}
									{(() => {
										const selected = countries.find((c) => c.id == exportFrom);
										return selected ? `(${selected.code}) ${selected.name}` : 'Select country';
									})()}
								{:else}
									Select country
								{/if}
							</span>
						</div>

						{#if showExportFromDropdown}
							<div
								class="dropdown-panel bg-base-100 border-base-300 absolute left-0 right-0 top-full z-20 mt-1 rounded-md border shadow-lg"
								on:click={(e) => e.stopPropagation()}
								on:mousedown={(e) => e.stopPropagation()}
							>
								<div class="border-base-300 border-b p-2">
									<input
										type="text"
										placeholder="Type to search..."
										bind:value={exportFromSearch}
										class="input input-sm w-full"
										on:input={() => (showExportFromDropdown = true)}
										on:keydown={(e) => e.stopPropagation()}
										on:click={(e) => e.stopPropagation()}
										on:mousedown={(e) => e.stopPropagation()}
										autofocus
									/>
								</div>
								<div class="max-h-60 overflow-y-auto">
									{#each filteredExportFromCountries as country}
										<div
											class="hover:bg-base-200 flex cursor-pointer items-center justify-between px-3 py-2 text-sm {exportFrom ==
											country.id
												? 'bg-primary text-primary-content'
												: ''}"
											on:click={() => {
												exportFrom = country.id;
												exportFromSearch = '';
												showExportFromDropdown = false;
											}}
										>
											<span>({country.code}) {country.name}</span>
											{#if exportFrom == country.id}
												<svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
													<path
														fill-rule="evenodd"
														d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
														clip-rule="evenodd"
													></path>
												</svg>
											{/if}
										</div>
									{/each}
									{#if filteredExportFromCountries.length === 0}
										<div class="text-base-content/60 px-3 py-2 text-sm">No countries found</div>
									{/if}
								</div>
							</div>
						{/if}
					</div>
				</div>

				<!-- Calculation Date -->
				<div class="form-control">
					<label class="label text-sm font-medium">Calculation Date</label>
					<input
						type="date"
						bind:value={calculationDate}
						class="input input-bordered w-full text-sm"
						required
					/>
				</div>

				<!-- Goods Value -->
				<div class="form-control">
					<label class="label text-sm font-medium">Value of Goods (USD)</label>
					<input
						type="number"
						bind:value={goodsValue}
						min="0"
						step="0.01"
						class="input input-bordered w-full text-sm"
						placeholder="Enter value"
						on:blur={formatCurrency}
						required
					/>
				</div>

				<!-- Goods Quantity -->
				<div class="form-control">
					<label class="label text-sm font-medium">Quantity of Goods (KG)</label>
					<input
						type="number"
						bind:value={quantity}
						min="0"
						step="0.01"
						class="input input-bordered w-full text-sm"
						placeholder="Enter value"
						on:blur={formatQuantity}
						required
					/>
				</div>

				<!-- Submit -->
				<div class="form-control">
					<button type="submit" class="btn btn-primary btn-sm w-full" disabled={isCalculating}>
						{#if isCalculating}
							<span class="loading loading-spinner loading-sm text-primary-content"></span>
							Calculating...
						{:else}
							Calculate Cost
						{/if}
					</button>
				</div>
			</form>

			<!-- Error Alert -->
			{#if showErrorAlert && calculationError}
				<div class="alert alert-error mt-6">
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
					<span>{calculationError}</span>
				</div>
			{/if}

			<!-- Calculation Result -->
			{#if calculationResult && !showErrorAlert}
				<div class="card bg-base-100 mt-6 p-6 shadow-md">
					<h2 class="mb-4 text-lg font-semibold">Calculation Result</h2>

					<div class="mb-2 flex justify-between text-sm">
						<span>Base Value:</span>
						<span class="font-medium">${calculationResult.baseValue}</span>
					</div>

					<div class="mb-2 flex justify-between text-sm">
						<span>Tariff Type:</span>
						<span class="font-medium text-blue-600">{calculationResult.tariffType}</span>
					</div>

					<div class="mb-2 flex justify-between text-sm">
						<span>Tariff Rate:</span>
						<span class="font-medium text-blue-600"
							>{calculationResult.tariffType == 'specific' ? '$' : ''}{parseFloat(
								calculationResult.tariffRate
							).toFixed(1)}{calculationResult.tariffType == 'specific'
								? '/' + calculationResult.quantity + calculationResult.rateUnit
								: '%'}</span
						>
					</div>

					<div class="mb-4 flex justify-between text-sm">
						<span>Tariff Amount:</span>
						<span class="text-red-600">+ ${calculationResult.tariffCost}</span>
					</div>

					<div class="border-base-300 flex justify-between border-t pt-3">
						<span class="font-semibold">Total Cost:</span>
						<span class="text-primary font-bold">${calculationResult.totalCost}</span>
					</div>
				</div>
			{/if}
		</div>

		<!-- Related News Card -->
		<div class="card bg-base-100 p-6 shadow-md">
			<h2 class="mb-1 text-lg font-semibold">Related News & Updates</h2>
			<p class="mb-4 text-xs text-gray-500">Stay informed about policy changes and trade updates</p>

			<ul class="space-y-4">
				{#each news as article}
					<li
						class="border-base-300 hover:text-primary cursor-pointer border-b pb-3"
						on:click={() => (selectedArticle = article)}
					>
						<h3 class="text-base font-medium">{article.title}</h3>
						<p class="text-xs text-gray-500">{article.date}</p>
						<p class="mt-1 text-sm">{article.summary}</p>
					</li>
				{/each}
			</ul>
		</div>
	</div>
</div>

<!-- Modal -->
{#if selectedArticle}
	<div class="modal modal-open">
		<div class="modal-box max-w-2xl">
			<h3 class="mb-2 text-lg font-semibold">{selectedArticle.title}</h3>
			<p class="mb-4 text-xs text-gray-500">{selectedArticle.date}</p>
			<p class="mb-4 text-sm">{selectedArticle.details}</p>
			<a href={selectedArticle.link} target="_blank" class="link link-primary text-sm">
				Read full article →
			</a>
			<div class="modal-action">
				<button class="btn btn-sm" on:click={() => (selectedArticle = null)}>Close</button>
			</div>
		</div>
	</div>
{/if}
