<script>
	let product = '';
	let exportFrom = '';
	let importTo = '';
	let calculationDate = '';
	let goodsValue = '';
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

	function calculateCost() {
		if (product && exportFrom && importTo && calculationDate && goodsValue) {
			alert(`Calculation Result:
  Product: ${product}
  From: ${exportFrom}
  To: ${importTo}
  Date: ${calculationDate}
  Goods Value: $${goodsValue}`);
		} else {
			alert('Please fill in all fields before calculating.');
		}
	}
</script>

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
				<!-- Product -->
				<div class="form-control">
					<label class="label text-sm font-medium">Product</label>
					<select bind:value={product} class="select select-bordered w-full text-sm" required>
						<option disabled value="">Select product</option>
						<option>Electronics</option>
						<option>Textiles</option>
						<option>Automobiles</option>
						<option>Agricultural Goods</option>
						<option>Chemicals</option>
						<option>Machinery</option>
					</select>
				</div>

				<!-- Export From -->
				<div class="form-control">
					<label class="label text-sm font-medium">Export From</label>
					<select bind:value={exportFrom} class="select select-bordered w-full text-sm" required>
						<option disabled value="">Select country</option>
						<option>Singapore</option>
						<option>Malaysia</option>
						<option>China</option>
						<option>USA</option>
						<option>Japan</option>
					</select>
				</div>

				<!-- Importing To -->
				<div class="form-control">
					<label class="label text-sm font-medium">Importing To</label>
					<select bind:value={importTo} class="select select-bordered w-full text-sm" required>
						<option disabled value="">Select country</option>
						<option>Singapore</option>
						<option>Malaysia</option>
						<option>China</option>
						<option>USA</option>
						<option>Japan</option>
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
						min="1"
						class="input input-bordered w-full text-sm"
						placeholder="Enter value in USD"
						required
					/>
				</div>

				<!-- Submit -->
				<div class="form-control">
					<button type="submit" class="btn btn-primary btn-sm w-full">Calculate Cost</button>
				</div>
			</form>
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
