<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Calculator</h1>
	<p class="text-sm text-gray-500">
		Enter HS Code, select countries, and calculate the cost of importing goods.
	</p>
	
	<!-- Global Alerts - Below page title -->
	{#if showErrorAlert && calculationError}
		<Alert 
			type="error" 
			message={calculationError} 
			show={showErrorAlert}
			autoDismiss={true}
		/>
	{/if}

  	<!-- Two-column layout -->
  	<div class="grid grid-cols-1 gap-8 lg:grid-cols-2">
    	<!-- Tariff Calculator Card -->
		<div class="card bg-base-100 p-6 shadow-md relative">
			{#if isCalculating}
				<div class="absolute inset-0 z-10 flex items-center justify-center bg-base-100/70">
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
							class="select select-bordered w-full text-sm cursor-pointer flex items-center justify-between {isCalculating ? 'opacity-50 cursor-not-allowed' : ''}"
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
										const selected = countries.find(c => c.id == importTo);
										return selected ? `(${selected.code}) ${selected.name}` : 'Select country';
									})()}
								{:else}
									Select country
								{/if}
							</span>
						</div>
						
						{#if showImportToDropdown}
							<div 
								class="dropdown-panel absolute top-full left-0 right-0 bg-base-100 border border-base-300 rounded-md shadow-lg z-20 mt-1"
								on:click={(e) => e.stopPropagation()}
								on:mousedown={(e) => e.stopPropagation()}
							>
								<div class="p-2 border-b border-base-300">
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
											class="px-3 py-2 text-sm hover:bg-base-200 cursor-pointer flex items-center justify-between {importTo == country.id ? 'bg-primary text-primary-content' : ''}"
											on:click={() => {
												importTo = country.id;
												importToSearch = '';
												showImportToDropdown = false;
											}}
										>
											<span>({country.code}) {country.name}</span>
											{#if importTo == country.id}
												<svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
													<path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
												</svg>
											{/if}
										</div>
									{/each}
									{#if filteredImportToCountries.length === 0}
										<div class="px-3 py-2 text-sm text-base-content/60">No countries found</div>
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
							class="select select-bordered w-full text-sm cursor-pointer flex items-center justify-between"
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
										const selected = countries.find(c => c.id == exportFrom);
										return selected ? `(${selected.code}) ${selected.name}` : 'Select country';
									})()}
								{:else}
									Select country
								{/if}
							</span>
						</div>
						
						{#if showExportFromDropdown}
							<div 
								class="dropdown-panel absolute top-full left-0 right-0 bg-base-100 border border-base-300 rounded-md shadow-lg z-20 mt-1"
								on:click={(e) => e.stopPropagation()}
								on:mousedown={(e) => e.stopPropagation()}
							>
								<div class="p-2 border-b border-base-300">
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
											class="px-3 py-2 text-sm hover:bg-base-200 cursor-pointer flex items-center justify-between {exportFrom == country.id ? 'bg-primary text-primary-content' : ''}"
											on:click={() => {
												exportFrom = country.id;
												exportFromSearch = '';
												showExportFromDropdown = false;
											}}
										>
											<span>({country.code}) {country.name}</span>
											{#if exportFrom == country.id}
												<svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
													<path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
												</svg>
											{/if}
										</div>
									{/each}
									{#if filteredExportFromCountries.length === 0}
										<div class="px-3 py-2 text-sm text-base-content/60">No countries found</div>
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
				<div class="card bg-base-100 shadow-md mt-6 p-6">
					<h2 class="text-lg font-semibold mb-4">Calculation Result</h2>

					<div class="flex justify-between text-sm mb-2">
						<span>Base Value:</span>
						<span class="font-medium">${calculationResult.baseValue}</span>
					</div>

					<div class="flex justify-between text-sm mb-2">
						<span>Tariff Rate:</span>
						<span class="text-blue-600 font-medium">{(parseFloat(calculationResult.tariffRate)).toFixed(1)}%</span>
					</div>

					<div class="flex justify-between text-sm mb-4">
						<span>Tariff Amount:</span>
						<span class="text-red-600">+ ${calculationResult.tariffCost}</span>
					</div>

					<div class="flex justify-between border-t border-base-300 pt-3">
						<span class="font-semibold">Total Cost:</span>
						<span class="font-bold text-primary">${calculationResult.totalCost}</span>
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
				<Alert 
					type="warning" 
					message={newsError} 
					show={true}
					autoDismiss={false}
				/>
				<div class="mt-2">
					<button class="btn btn-sm btn-outline" on:click={loadNews}>Retry</button>
				</div>
			{:else if news.length === 0}
				<div class="text-center py-8">
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
							<p class="text-xs text-gray-500">{article.date || 'Date unavailable'}</p>
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
					<div class="flex items-center justify-between mt-4 pt-4 border-t border-base-300">
						<div class="text-sm text-gray-500">
							Showing {((currentPage - 1) * pageSize) + 1}-{Math.min(currentPage * pageSize, news.length)} of {news.length} articles
						</div>
						<div class="flex gap-2">
							<button 
								class="btn btn-sm btn-outline" 
								disabled={currentPage === 1}
								on:click={() => currentPage = Math.max(1, currentPage - 1)}
							>
								Previous
							</button>
							<button 
								class="btn btn-sm btn-outline" 
								disabled={currentPage * pageSize >= news.length}
								on:click={() => currentPage = currentPage + 1}
							>
								Next
							</button>
						</div>
					</div>
				{/if}
			{/if}
		</div>
  </div>
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
			<p class="mb-4 text-xs text-gray-500">{selectedArticle.date}</p>
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

<!-- Calculator Logic-->
<script>
	import { fetchCountries } from "$lib/api/countries.js";
	import { fetchNews } from "$lib/api/news.js";
	import { calculateTariffCost } from "$lib/api/tariff.js";
	import Alert from "$lib/components/Alert.svelte";
	import { onMount } from "svelte";

	let hsCode = '';
	let exportFrom = '';
	let importTo = '';
	let calculationDate = new Date().toISOString().split("T")[0]; // Set the Calculation Date
	let goodsValue = '';

	// Search state for countries
	let exportFromSearch = "";
	let importToSearch = "";
	let showExportFromDropdown = false;
	let showImportToDropdown = false;

	

	let countries = [];
	onMount(async () => {
		console.log('Fetching countries...');
		countries = await fetchCountries();
		console.log('Countries loaded:', countries);
  	});

	
	// Filter countries for search
	$: filteredExportFromCountries = countries.filter((country) =>
		country.name.toLowerCase().includes(exportFromSearch.toLowerCase()) ||
		country.code.toLowerCase().includes(exportFromSearch.toLowerCase())
	);

	$: filteredImportToCountries = countries.filter((country) =>
		country.name.toLowerCase().includes(importToSearch.toLowerCase()) ||
		country.code.toLowerCase().includes(importToSearch.toLowerCase())
	);

	// Format Currency
	function formatCurrency() {
		if (goodsValue) {
			goodsValue = parseFloat(goodsValue).toFixed(2);
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
		
		if (hsCode && exportFrom && importTo && calculationDate && goodsValue) {
			// Validate HS Code format (basic validation)
			if (!/^\d{6}$/.test(hsCode)) {
				calculationError = "Please enter a valid HS Code format (6 digits, e.g., 850110)";
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
					goodsValue
				});
				
				console.log('Calculation result:', result);
				
				if (result === null) {
					calculationError = "No tariff data found for the specified countries and product. Please check your selection or contact support.";
					showErrorAlert = true;
				} else {
					// Check if this is a "no data" case (tariff rate is -1)
					const tariffRate = parseFloat(result.tariffRate);
					
					if (tariffRate === -1) {
						// No tariff data found in database
						calculationError = "No tariff data found for the specified countries and product. Please check your selection or contact support.";
						showErrorAlert = true;
					} else {
						// Valid tariff data (including 0% tariff)
						calculationResult = result;
					}
				}
			} catch (error) {
				console.error('Calculation error:', error);
				calculationError = error.message || "An error occurred while calculating the tariff. Please try again.";
				showErrorAlert = true;
			}
		} else {
			calculationError = "Please fill in all fields before calculating.";
			showErrorAlert = true;
		}
		
		isCalculating = false;
	}
	// End: Tariff Calculation Section 


	// Start: Related News Section
	let selectedArticle = null;
	let news = [];
	let newsLoading = false;
	let newsError = null;
	
	// Pagination state
	let currentPage = 1;
	let pageSize = 2; // Changed to 2 for testing
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

