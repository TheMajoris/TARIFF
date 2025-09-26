<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Tariff Calculator</h1>
	<p class="text-sm text-gray-500">
		Enter product details, select countries, and calculate the cost of importing goods.
	</p>

  	<!-- Two-column layout -->
  	<div class="grid grid-cols-1 gap-8 lg:grid-cols-2">
    	<!-- Tariff Calculator Card -->
		<div class="card bg-base-100 p-6 shadow-md">
			<h2 class="mb-1 text-lg font-semibold">Tariff Calculator</h2>
			<p class="mb-4 text-xs text-gray-500">Calculate the cost of importing a product</p>

			<form class="space-y-4" on:submit|preventDefault={calculateCost}>
				<!-- Product with Search -->
				<div class="form-control relative">
					<label class="label text-sm font-medium hidden">Product</label>
					<input
						type="text"
						placeholder="Search product..."
						bind:value={productSearch}
						class="input input-bordered w-full text-sm mb-2"
						on:focus={() => (showProductDropdown = true)}
						hidden
					/>
					{#if showProductDropdown && filteredProducts.length > 0}
						<ul class="menu bg-base-100 border border-base-300 rounded-md shadow max-h-40 overflow-y-auto absolute w-full z-10">
						{#each filteredProducts as item}
							<li>
							<a
								class="text-sm"
								on:click={() => {
								product = item;
								productSearch = item;
								showProductDropdown = false;
								}}
							>
								{item}
							</a>
							</li>
						{/each}
						</ul>
					{/if}
				</div>

				<!-- HS Code Input -->
				<div class="form-control">
					<label class="label text-sm font-medium">HS Code</label>
					<input
						type="text"
						placeholder="Enter HS Code (e.g., 8501.10.10)"
						bind:value={hsCode}
						class="input input-bordered w-full text-sm"
						required
					/>
					<label class="label">
						<span class="label-text-alt text-xs text-gray-500">
							Harmonized System Code for product classification
						</span>
					</label>
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
							<svg class="w-4 h-4 transition-transform {showExportFromDropdown ? 'rotate-180' : ''}" fill="none" stroke="currentColor" viewBox="0 0 24 24">
								<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
							</svg>
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
				<!-- Importing To -->
				<div class="form-control mt-4">
					<label class="label text-sm font-medium">Importing To</label>
					<div class="relative">
						<div 
							class="select select-bordered w-full text-sm cursor-pointer flex items-center justify-between"
							on:click={() => (showImportToDropdown = !showImportToDropdown)}
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
							<svg class="w-4 h-4 transition-transform {showImportToDropdown ? 'rotate-180' : ''}" fill="none" stroke="currentColor" viewBox="0 0 24 24">
								<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
							</svg>
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
					<button type="submit" class="btn btn-primary btn-sm w-full">Calculate Cost</button>
				</div>
			</form>
			<!-- Calculation Result -->
			{#if calculationResult}
				<div class="card bg-base-100 shadow-md mt-6 p-6">
					<h2 class="text-lg font-semibold mb-4">Calculation Result</h2>

					<div class="flex justify-between text-sm mb-2">
						<span>Base Value:</span>
						<span class="font-medium">${calculationResult.baseValue}</span>
					</div>

					<div class="flex justify-between text-sm mb-2">
						<span>Tariff:</span>
						<span class="text-green-600">+ ${calculationResult.tariff}</span>
					</div>

					<div class="flex justify-between text-sm mb-4">
						<span>Customs Duty:</span>
						<span class="text-green-600">+ ${calculationResult.customsDuty}</span>
					</div>

					<div class="flex justify-between border-t border-base-300 pt-3">
						<span class="font-semibold">Total Import Cost:</span>
						<span class="font-bold text-primary">${calculationResult.totalCost}</span>
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

<!-- Product and Calculator Logic-->
<script>
	import { fetchCountries } from "$lib/api/countries.js";
	import { calculateTariffCost } from "$lib/api/tariff.js";
	import { onMount } from "svelte";

	let product = '';
	let hsCode = '';
	let exportFrom = '';
	let importTo = '';
	let calculationDate = new Date().toISOString().split("T")[0]; // Set the Calculation Date
	let goodsValue = '';

	// Search state for product
  	let productSearch = "";
  	let showProductDropdown = false;

	// Search state for countries
	let exportFromSearch = "";
	let importToSearch = "";
	let showExportFromDropdown = false;
	let showImportToDropdown = false;

	/* Jiajun - 14/9/2025
	This is a placeholder as data have not been done yet. 
	This will be replaced with a function to fetch the real 
	product data in the next sprint when the necessary 
	dependancies are finished.
	*/
	const products = [
		"Electronics",
		"Textiles",
		"Automobiles",
		"Agricultural Goods",
		"Chemicals",
		"Machinery",
		"Pharmaceuticals",
		"Food Products",
		"Metals",
		"Plastics"
	];

	let countries = [];
	onMount(async () => {
		console.log('Fetching countries...');
		countries = await fetchCountries();
		console.log('Countries loaded:', countries);
  	});

	$: filteredProducts = products.filter((p) =>
    	p.toLowerCase().includes(productSearch.toLowerCase())
  	);

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
	async function calculateCost() {
		if (product && hsCode && exportFrom && importTo && calculationDate && goodsValue) {
			// Validate HS Code format (basic validation)
			if (!/^\d{4}\.\d{2}\.\d{2}$/.test(hsCode)) {
				alert("Please enter a valid HS Code format (e.g., 8501.10.10)");
				return;
			}
			
			calculationResult = await calculateTariffCost({
				product,
				hsCode,
				exportFrom,
				importTo,
				calculationDate,
				goodsValue
			});
		} else {
			alert("Please fill in all fields before calculating.");
		}
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

