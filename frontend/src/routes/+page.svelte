<!-- Calculator Logic-->
<script>
	import { saveCalculation } from '$lib/api/calculationHistory.js';
	import { fetchCountries } from '$lib/api/countries.js';
	import { fetchNews } from '$lib/api/news.js';
	import { getAlternativeRoutes } from '$lib/api/routes.js';
	import { calculateTariffCost } from '$lib/api/tariff.js';
	import Alert from '$lib/components/Alert.svelte';
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

	// Save Calculation State
	let showSaveModal = false;
	let saveCalculationName = '';
	let saveCalculationNotes = '';
	let isSaving = false;
	let saveSuccessMessage = '';
	let saveErrorMessage = '';

	async function calculateCost() {
		// Clear previous results and errors
		calculationResult = null;
		calculationError = null;
		showErrorAlert = false;
		showAlternativeRoutes = false; // Clear optimized routes when recalculating
		optimizedRoutes = [];
		expandedRouteIndex = null;
		routeInfoMessage = ''; // Clear route info message
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
					// Valid tariff data (including 0% tariff)
					calculationResult = result;
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

	// Start: Optimized Route Section
	let showAlternativeRoutes = false;
	let isLoadingRoutes = false;
	let optimizedRoutes = [];
	let expandedRouteIndex = null;
	let routeInfoMessage = '';

	async function findOptimizedRoute() {
		isLoadingRoutes = true;
		showAlternativeRoutes = false;
		expandedRouteIndex = null; // Reset details view
		routeInfoMessage = ''; // Clear previous info message

		try {
			// Ensure prerequisites
			if (!calculationResult) {
				throw new Error('Please calculate the base tariff first.');
			}
			const importingCountry = countries.find((c) => c.id == importTo);
			const exportingCountry = countries.find((c) => c.id == exportFrom);
			if (!importingCountry || !exportingCountry) {
				throw new Error('Country information not found');
			}

			const apiResponse = await getAlternativeRoutes({
				exportingCountryCode: exportingCountry.code,
				importingCountryCode: importingCountry.code,
				hsCode: hsCode,
				initialPrice: goodsValue,
				quantity: quantity,
				date: calculationDate
			});

			const originalCost = parseFloat(calculationResult.totalCost);
			const routes = /** @type {any[]} */ (
				Array.isArray(apiResponse?.data) ? apiResponse.data : []
			);

			// Check if no routes found
			if (routes.length === 0) {
				routeInfoMessage = 'No alternative routes found. The direct route is already optimal.';
				showAlternativeRoutes = false;
				// Scroll to top to surface the flash message
				if (typeof window !== 'undefined') {
					window.scrollTo({ top: 0, behavior: 'smooth' });
				}
				return;
			}

			// Map backend response to UI model
			optimizedRoutes = routes
				.map((route) => {
					const pathCodes = (route.routes || [])
						.map((r) => [r.sourceCountryCode, r.destinationCountryCode])
						.flat();
					// dedupe sequential duplicates without introducing implicit any types
					const codes = [];
					for (let i = 0; i < pathCodes.length; i++) {
						if (i === 0 || pathCodes[i] !== pathCodes[i - 1]) codes.push(pathCodes[i]);
					}
					const pathNames = codes.map(
						(code) => countries.find((c) => c.code === code)?.name || code
					);

					const tariffBreakdown = (route.routes || []).map((r) => ({
						from:
							countries.find((c) => c.code === r.sourceCountryCode)?.name || r.sourceCountryCode,
						to:
							countries.find((c) => c.code === r.destinationCountryCode)?.name ||
							r.destinationCountryCode,
						tariffCost: parseFloat(String(r.cost)),
						tariffRate: r.tariffRate?.tariffRate ?? null
					}));

					const baseValue = parseFloat(String(goodsValue || '0'));
					const routeCost = route.totalCost ?? route.tariffTotalCost ?? 0;
					const totalCost = parseFloat(String(routeCost)) || Number(routeCost) + baseValue;
					return {
						path: pathNames,
						totalCost,
						tariffBreakdown,
						savings: originalCost - totalCost,
						savingsPercent: Math.round(((originalCost - totalCost) / originalCost) * 100)
					};
				})
				.sort((a, b) => a.totalCost - b.totalCost);

			showAlternativeRoutes = true;
		} catch (error) {
			console.error('Error finding optimized route:', error);
			calculationError =
				error && /** @type {any} */ (error).message
					? /** @type {any} */ (error).message
					: 'Failed to find optimized routes. Please try again.';
			showErrorAlert = true;
		} finally {
			isLoadingRoutes = false;
		}
	}

	function toggleRouteDetails(index) {
		expandedRouteIndex = expandedRouteIndex === index ? null : index;
	}
	// End: Optimized Route Section

	// Start: Save Calculation Section
	function openSaveModal() {
		// Clear previous values
		saveCalculationName = '';
		saveCalculationNotes = '';
		saveSuccessMessage = '';
		saveErrorMessage = '';
		showSaveModal = true;
	}

	function closeSaveModal() {
		showSaveModal = false;
		saveCalculationName = '';
		saveCalculationNotes = '';
	}

	async function performSave() {
		if (!calculationResult) {
			saveErrorMessage = 'No calculation result to save';
			return;
		}

		isSaving = true;
		saveErrorMessage = '';
		saveSuccessMessage = '';

		try {
			// Get country codes from the selected countries
			const importingCountry = countries.find((c) => c.id == importTo);
			const exportingCountry = countries.find((c) => c.id == exportFrom);

			if (!importingCountry || !exportingCountry) {
				throw new Error('Country information not found');
			}
			
			// Prepare calculation data according to SaveCalculationRequestDTO
			const calculationData = {
				calculationName: saveCalculationName,
				productValue: parseFloat(goodsValue),
				productQuantity: parseFloat(quantity),
				currencyCode: 'USD', // Default currency, can be made configurable
				tariffRate: parseFloat(calculationResult.tariffRate),
				tariffType: calculationResult.tariffType,
				calculatedTariffCost: parseFloat(calculationResult.tariffCost),
				totalCost: parseFloat(calculationResult.totalCost),
				notes: saveCalculationNotes || null,
				importingCountryCode: importingCountry.code,
				exportingCountryCode: exportingCountry.code,
				productCategoryCode: parseInt(hsCode),
				unitQuantity: calculationResult.quantity,
				rateUnit: calculationResult.rateUnit,
			};

			console.log('Saving calculation:', calculationData);

			const result = await saveCalculation(calculationData);

			console.log('Save result:', result);

			// Close modal and show success message
			closeSaveModal();
			saveSuccessMessage = 'Calculation saved successfully! View it in your calculation history.';

			// Scroll to top to show success message
			window.scrollTo({ top: 0, behavior: 'smooth' });

			// Clear success message after 5 seconds
			setTimeout(() => {
				saveSuccessMessage = '';
			}, 5000);
		} catch (error) {
			console.error('Error saving calculation:', error);
			saveErrorMessage = error.message || 'Failed to save calculation. Please try again.';

			// Scroll to top to show error message
			window.scrollTo({ top: 0, behavior: 'smooth' });

			// Clear error message after 5 seconds
			setTimeout(() => {
				saveErrorMessage = '';
			}, 5000);
		} finally {
			isSaving = false;
		}
	}
	// End: Save Calculation Section

	// Start: Related News Section
	let selectedArticle = null;
	let news = [];
	let newsLoading = false;
	let newsError = null;

	// Pagination state
	let currentPage = 1;
	let pageSize = 3;
	let displayNews = [];

	// Calculate displayed news based on pagination
	$: {
		const startIndex = (currentPage - 1) * pageSize;
		const endIndex = startIndex + pageSize;
		displayNews = news.slice(startIndex, endIndex);
	}

	// Reset to first page when news changes
	$: if (news.length > 0) {
		currentPage = 1;
	}

	// Fetch news from API
	async function loadNews() {
		newsLoading = true;
		newsError = null;

		try {
			news = await fetchNews();
			console.log('News loaded:', news);
		} catch (error) {
			console.error('Failed to load news:', error);
			newsError = 'Failed to load news. Please try again later.';
			// Fallback to empty array
			news = [];
		} finally {
			newsLoading = false;
		}
	}

	// Load news on component mount
	onMount(() => {
		loadNews();
	});
	// End: Related News Section
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Calculator</h1>
	<p class="text-sm text-gray-500">
		Enter HS Code, select countries, and calculate the cost of importing goods.
	</p>

	<!-- Global Alerts - Below page title -->
	{#if showErrorAlert && calculationError}
		<Alert type="error" message={calculationError} show={showErrorAlert} autoDismiss={true} />
	{/if}

	{#if saveSuccessMessage}
		<Alert type="success" message={saveSuccessMessage} show={true} autoDismiss={true} />
	{/if}

	{#if saveErrorMessage}
		<Alert type="error" message={saveErrorMessage} show={true} autoDismiss={true} />
	{/if}

	{#if routeInfoMessage}
		<Alert type="info" message={routeInfoMessage} show={true} autoDismiss={true} />
	{/if}

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
						<span class="font-medium text-blue-600"
							>{calculationResult.tariffType == 'ad_valorem' ? 'Ad Valorem' : 'Specific'}</span
						>
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
						<span>Tariff Cost:</span>
						<span class="text-red-600">+ ${calculationResult.tariffCost}</span>
					</div>

					<div class="border-base-300 flex justify-between border-t pt-3">
						<span class="font-semibold">Total Cost:</span>
						<span class="text-primary font-bold">${calculationResult.totalCost}</span>
					</div>

					<!-- Action Buttons -->
					<div class="mt-4 flex gap-2">
						<button
							class="btn btn-outline btn-primary btn-sm flex-1"
							on:click={findOptimizedRoute}
							disabled={isLoadingRoutes}
							type="button"
						>
							{#if isLoadingRoutes}
								<span class="loading loading-spinner loading-sm"></span>
								Finding...
							{:else}
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
										d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7"
									/>
								</svg>
								Find Optimized Route
							{/if}
						</button>
						<button class="btn btn-primary btn-sm" on:click={openSaveModal} type="button">
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
									d="M8 7H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-3m-1 4l-3 3m0 0l-3-3m3 3V4"
								/>
							</svg>
							Save
						</button>
					</div>
				</div>
			{/if}
		</div>

		<!-- Related News Card -->
		<div class="card bg-base-100 p-6 shadow-md">
			<h2 class="mb-1 text-lg font-semibold">Related News & Updates</h2>
			<p class="mb-4 text-xs text-gray-500">Stay informed about policy changes and trade updates</p>

			{#if newsLoading}
				<div class="flex items-center justify-center py-8">
					<span class="loading loading-spinner loading-md text-primary"></span>
					<span class="ml-2 text-sm text-gray-500">Loading latest news...</span>
				</div>
			{:else if newsError}
				<div class="alert alert-warning">
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
							d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"
						></path>
					</svg>
					<span>{newsError}</span>
					<button class="btn btn-sm btn-outline" on:click={loadNews}>Retry</button>
				</div>
			{:else if news.length === 0}
				<div class="py-8 text-center">
					<p class="text-gray-500">No news articles available at the moment.</p>
					<button class="btn btn-sm btn-outline mt-2" on:click={loadNews}>Refresh</button>
				</div>
			{:else}
				<ul class="space-y-4">
					{#each displayNews as article}
						<li
							class="border-base-300 hover:text-primary cursor-pointer border-b pb-3"
							on:click={() => (selectedArticle = article)}
						>
							<h3 class="text-base font-medium">{article.title}</h3>
							<p class="mt-1 text-sm">{article.summary}</p>
							{#if article.tags && article.tags.length > 0}
								<div class="mt-2 flex flex-wrap gap-1">
									{#each article.tags as tag}
										<span class="badge badge-outline badge-sm">{tag}</span>
									{/each}
								</div>
							{/if}
						</li>
					{/each}
				</ul>

				<!-- Pagination Controls -->
				{#if news.length > pageSize}
					<div class="border-base-300 mt-4 flex items-center justify-between border-t pt-4">
						<div class="text-sm text-gray-500">
							Showing {(currentPage - 1) * pageSize + 1}-{Math.min(
								currentPage * pageSize,
								news.length
							)} of {news.length} articles
						</div>
						<div class="flex gap-2">
							<button
								class="btn btn-sm btn-outline"
								disabled={currentPage === 1}
								on:click={() => (currentPage = Math.max(1, currentPage - 1))}
							>
								Previous
							</button>
							<button
								class="btn btn-sm btn-outline"
								disabled={currentPage * pageSize >= news.length}
								on:click={() => (currentPage = currentPage + 1)}
							>
								Next
							</button>
						</div>
					</div>
				{/if}
			{/if}
		</div>
	</div>

	<!-- Optimized Routes Section -->
	{#if showAlternativeRoutes && optimizedRoutes && optimizedRoutes.length}
		<div class="card bg-base-100 border-success mt-6 border-2 p-6 shadow-md">
			<div class="mb-2 flex items-center gap-2">
				<svg
					xmlns="http://www.w3.org/2000/svg"
					class="text-success h-6 w-6"
					fill="none"
					viewBox="0 0 24 24"
					stroke="currentColor"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
					/>
				</svg>
				<h2 class="text-lg font-semibold">Alternative Routes & Cost Comparison</h2>
			</div>
			<p class="mb-4 text-sm text-gray-500">
				We found {optimizedRoutes.length} optimized {optimizedRoutes.length === 1
					? 'route'
					: 'routes'} for you
			</p>

			<div class="grid grid-cols-1 gap-4">
				{#each optimizedRoutes as route, idx}
					<div
						class="card bg-base-100 border p-4 shadow {idx === 0
							? 'border-success'
							: 'border-base-300'}"
					>
						<div class="mb-2 flex items-center justify-between">
							<div class="flex items-center gap-2">
								<h3 class="text-base font-semibold">Route {idx + 1}</h3>
								{#if idx === 0}
									<span class="badge badge-success badge-sm">Cheapest</span>
								{/if}
							</div>
							<!-- Removed estimated days since backend does not provide it -->
						</div>

						<!-- Path -->
						<div class="bg-success/5 mb-3 rounded-lg p-3">
							<div class="mb-1 text-xs text-gray-500">Trade Route</div>
							<div class="flex flex-wrap items-center gap-1">
								{#each route.path as country, i}
									<span class="text-sm font-semibold">{country}</span>
									{#if i < route.path.length - 1}
										<svg
											xmlns="http://www.w3.org/2000/svg"
											class="text-success h-4 w-4"
											fill="none"
											viewBox="0 0 24 24"
											stroke="currentColor"
										>
											<path
												stroke-linecap="round"
												stroke-linejoin="round"
												stroke-width="2"
												d="M13 7l5 5m0 0l-5 5m5-5H6"
											/>
										</svg>
									{/if}
								{/each}
							</div>
						</div>

						<!-- Cost Comparison -->
						<div class="mb-3 grid grid-cols-2 gap-3">
							<div class="bg-base-100 rounded-lg p-3 text-center">
								<div class="text-xs text-gray-500">Original Cost</div>
								<div class="text-lg font-bold text-gray-400 line-through">
									${calculationResult.totalCost}
								</div>
							</div>
							<div class="bg-success/10 rounded-lg p-3 text-center">
								<div class="text-xs text-gray-500">Optimized Cost</div>
								<div class="text-success text-2xl font-bold">${route.totalCost.toFixed(2)}</div>
							</div>
						</div>

						{#if route.savingsPercent > 0}
							<div class="alert alert-success mb-3 min-h-0 py-2">
								<span class="text-sm font-semibold"
									>You save ${route.savings.toFixed(2)} ({route.savingsPercent}% off)</span
								>
							</div>
						{/if}

						<!-- Toggle Details -->
						<div>
							<button
								class="btn btn-ghost btn-sm w-full"
								on:click={() => toggleRouteDetails(idx)}
								type="button"
							>
								{#if expandedRouteIndex === idx}
									Hide Details
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
											d="M5 15l7-7 7 7"
										/>
									</svg>
								{:else}
									View Detailed Breakdown
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
											d="M19 9l-7 7-7-7"
										/>
									</svg>
								{/if}
							</button>
						</div>

						{#if expandedRouteIndex === idx}
							<div class="card bg-base-100 mt-3 p-4 shadow">
								<h4 class="mb-3 text-sm font-semibold">Tariff Breakdown</h4>
								<div class="space-y-2">
									{#each route.tariffBreakdown as leg, index}
										<div class="border-base-300 rounded border p-2">
											<div class="mb-1 flex items-center justify-between">
												<div class="flex items-center gap-2">
													<span class="badge badge-sm">{index + 1}</span>
													<span class="text-sm font-medium">{leg.from} → {leg.to}</span>
												</div>
												<span class="text-sm font-bold">${leg.tariffCost.toFixed(2)}</span>
											</div>
											<div class="text-xs text-gray-500">
												Tariff Rate: {leg.tariffType == 'specific' ? '$' : ''}{parseFloat(
													leg.tariffRate
												).toFixed(1)}{leg.tariffType == 'specific'
													? '/' + leg.quantity + leg.rateUnit
													: '%'}
											</div>
										</div>
									{/each}
								</div>
								<div class="alert alert-info mt-3 min-h-0 py-2">
									<div class="text-xs">
										This route takes advantage of trade agreements and preferential tariff rates.
									</div>
								</div>
							</div>
						{/if}
					</div>
				{/each}
			</div>
		</div>
	{/if}
</div>

<!-- Modal -->
{#if selectedArticle}
	<div class="modal modal-open">
		<!-- Background which will close the modal -->
		<button
			class="modal-backdrop cursor-pointer"
			on:click={() => {
				selectedArticle = null;
			}}>close</button
		>

		<div class="modal-box max-w-2xl">
			<h3 class="mb-2 text-lg font-semibold">{selectedArticle.title}</h3>
			<p class="mb-4 text-sm">{selectedArticle.summary}</p>
			{#if selectedArticle.tags && selectedArticle.tags.length > 0}
				<div class="mb-4 flex flex-wrap gap-1">
					{#each selectedArticle.tags as tag}
						<span class="badge badge-outline badge-sm">{tag}</span>
					{/each}
				</div>
			{/if}
			<a href={selectedArticle.link} target="_blank" class="link link-primary text-sm">
				Read full article →
			</a>
			<div class="modal-action">
				<button class="btn btn-sm" on:click={() => (selectedArticle = null)}>Close</button>
			</div>
		</div>
	</div>
{/if}

<!-- Save Calculation Modal -->
{#if showSaveModal}
	<div class="modal modal-open">
		<!-- Background which will close the modal -->
		<button class="modal-backdrop cursor-pointer" on:click={closeSaveModal}>close</button>

		<div class="modal-box">
			<h3 class="text-lg font-bold">Save Calculation</h3>
			<p class="py-2 text-sm text-gray-500">
				Give your calculation a name and optionally add notes for future reference.
			</p>

			<form on:submit|preventDefault={performSave}>
				<div class="form-control mt-4">
					<label class="label">
						<span class="label-text">Calculation Name *</span>
					</label>
					<input
						type="text"
						placeholder="e.g., Laptop Import US-CN"
						bind:value={saveCalculationName}
						class="input input-bordered w-full"
						maxlength="100"
						required
					/>
				</div>

				<div class="form-control mt-4">
					<label class="label">
						<span class="label-text">Notes (Optional)</span>
					</label>
					<textarea
						placeholder="Add any additional notes..."
						bind:value={saveCalculationNotes}
						class="textarea textarea-bordered h-24"
						maxlength="500"
					></textarea>
					<label class="label">
						<span class="label-text-alt">{saveCalculationNotes.length}/500 characters</span>
					</label>
				</div>

				<div class="modal-action">
					<button type="button" class="btn" on:click={closeSaveModal}>Cancel</button>
					<button type="submit" class="btn btn-primary" disabled={isSaving}>
						{#if isSaving}
							<span class="loading loading-spinner loading-sm"></span>
							Saving...
						{:else}
							Save
						{/if}
					</button>
				</div>
			</form>
		</div>
	</div>
{/if}
