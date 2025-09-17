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
					<label class="label text-sm font-medium">Product</label>
					<input
						type="text"
						placeholder="Search product..."
						bind:value={productSearch}
						class="input input-bordered w-full text-sm mb-2"
						on:focus={() => (showProductDropdown = true)}
						required
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

				<!-- Exporting From -->
				<div class="form-control">
					<label class="label text-sm font-medium">Export From</label>
					<select bind:value={exportFrom} class="select select-bordered w-full text-sm" required>
						<option disabled value="">Select country</option>
						{#each countries as country}
						<option>{country}</option>
						{/each}
					</select>
				</div>
				<!-- Importing To -->
				<div class="form-control mt-4">
					<label class="label text-sm font-medium">Importing To</label>
					<select bind:value={importTo} class="select select-bordered w-full text-sm" required>
						<option disabled value="">Select country</option>
						{#each countries as country}
						<option>{country}</option>
						{/each}
					</select>
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
	import { onMount } from "svelte";

	let product = '';
	let exportFrom = '';
	let importTo = '';
	let calculationDate = new Date().toISOString().split("T")[0]; // Set the Calculation Date
	let goodsValue = '';

	// Search state for product
  	let productSearch = "";
  	let showProductDropdown = false;

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
		try {
			/* Jiajun - 14/9/2025
			This is a placeholder as the APIs have not been done yet. 
			This will be replaced in the next sprint when the 
			necessary dependancies are finished.
			*/

			// const res = await fetch("/api/countries");
			// if (!res.ok) throw new Error("Failed to fetch");
			// countries = await res.json();

			const res = {
				ok: true,
				json: async () => [
					"Afghanistan","Albania","Algeria","Andorra","Angola","Antigua and Barbuda",
					"Argentina","Armenia","Australia","Austria","Azerbaijan",
					"Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bhutan","Bolivia",
					"Bosnia and Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina Faso","Burundi",
					"Cabo Verde","Cambodia","Cameroon","Canada","Central African Republic","Chad","Chile","China","Colombia","Comoros","Congo","Costa Rica","Croatia","Cuba","Cyprus","Czechia",
					"Denmark","Djibouti","Dominica","Dominican Republic",
					"Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Eswatini","Ethiopia",
					"Fiji","Finland","France",
					"Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada","Guatemala","Guinea","Guinea-Bissau","Guyana",
					"Haiti","Honduras","Hungary",
					"Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy",
					"Jamaica","Japan","Jordan",
					"Kazakhstan","Kenya","Kiribati","Kuwait","Kyrgyzstan",
					"Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg",
					"Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Mauritania","Mauritius","Mexico","Micronesia","Moldova","Monaco","Mongolia","Montenegro","Morocco","Mozambique","Myanmar",
					"Namibia","Nauru","Nepal","Netherlands","New Zealand","Nicaragua","Niger","Nigeria","North Korea","North Macedonia","Norway",
					"Oman",
					"Pakistan","Palau","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal",
					"Qatar",
					"Romania","Russia","Rwanda",
					"Saint Kitts and Nevis","Saint Lucia","Saint Vincent and the Grenadines","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Korea","South Sudan","Spain","Sri Lanka","Sudan","Suriname","Sweden","Switzerland","Syria",
					"Taiwan","Tajikistan","Tanzania","Thailand","Timor-Leste","Togo","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu",
					"Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","Uruguay","Uzbekistan",
					"Vanuatu","Vatican City","Venezuela","Vietnam",
					"Yemen",
					"Zambia","Zimbabwe"
				]
			};

			if (res.ok) {
				countries = await res.json();
			}
		} catch (err) {
			console.error("Failed to fetch countries:", err);
		}
  	});

	$: filteredProducts = products.filter((p) =>
    	p.toLowerCase().includes(productSearch.toLowerCase())
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
		if (product && exportFrom && importTo && calculationDate && goodsValue) {
		try {
			/* Jiajun - 14/9/2025
			This is a placeholder as the APIs have not been done yet. 
			This will be replaced in the next sprint when the 
			necessary dependancies are finished.
			*/
			/*
			const res = await fetch("/api/calculate-cost", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				product,
				exportFrom,
				importTo,
				calculationDate,
				goodsValue
			})
			});
			if (!res.ok) throw new Error("Failed to calculate");
			const data = await res.json();
			calculationResult = data; // Assume backend return { baseValue, tariff, customsDuty, totalCost }
			*/

			// Dummy Logic;
			const baseValue = parseFloat(goodsValue).toFixed(2);
			const tariff = (35).toFixed(2);        // dummy
			const customsDuty = (20).toFixed(2);   // dummy
			const totalCost = (parseFloat(baseValue) + 35 + 20).toFixed(2);

			calculationResult = {
				baseValue,
				tariff,
				customsDuty,
				totalCost
			};

		} catch (err) {
			console.error("Error calculating cost: ", err);
		}
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

