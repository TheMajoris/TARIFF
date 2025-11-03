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
	
	{#if saveSuccessMessage}
		<Alert 
			type="success" 
			message={saveSuccessMessage} 
			show={true}
			autoDismiss={true}
		/>
	{/if}
	
	{#if saveErrorMessage}
		<Alert 
			type="error" 
			message={saveErrorMessage} 
			show={true}
			autoDismiss={true}
		/>
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
						<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
							<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
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

	<!-- Optimized Route Card -->
	{#if showAlternativeRoutes && optimizedRoute}
	<div class="card bg-base-100 mt-6 p-6 shadow-md border-2 border-success">
	<div class="flex items-center gap-2 mb-4">
		<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-success" fill="none" viewBox="0 0 24 24" stroke="currentColor">
			<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
		</svg>
		<h2 class="text-lg font-semibold">Optimized Route Found</h2>
	</div>
	<p class="mb-4 text-sm text-gray-500">We found a better route to save you money</p>

	<div class="rounded-lg bg-success/5 p-4">
		<!-- Route Path -->
		<div class="mb-4">
			<div class="text-xs text-gray-500 mb-2">Trade Route</div>
			<div class="flex flex-wrap items-center gap-1">
				{#each optimizedRoute.path as country, i}
					<span class="font-semibold text-base">{country}</span>
					{#if i < optimizedRoute.path.length - 1}
						<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-success" fill="none" viewBox="0 0 24 24" stroke="currentColor">
							<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
						</svg>
					{/if}
				{/each}
			</div>
		</div>
	</div>

	<!-- Cost Comparison -->
	<div class="grid grid-cols-2 gap-4 mb-4">
		<div class="text-center p-3 bg-base-100 rounded-lg">
			<div class="text-xs text-gray-500">Original Cost</div>
			<div class="text-lg font-bold text-gray-400 line-through">
				${calculationResult.totalCost}
			</div>
		</div>
		<div class="text-center p-3 bg-success/10 rounded-lg">
			<div class="text-xs text-gray-500">Optimized Cost</div>
			<div class="text-2xl font-bold text-success">
				${optimizedRoute.totalCost.toFixed(2)}
			</div>
		</div>
	</div>

	<!-- Savings Badge -->
	{#if optimizedRoute.savingsPercent > 0}
		<div class="alert alert-success mb-4">
			<svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
				<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
			</svg>
			<span class="font-semibold">
				You save ${optimizedRoute.savings.toFixed(2)} ({optimizedRoute.savingsPercent}% off)
			</span>
		</div>
	{/if}

	<!-- Route Details -->
	<div class="grid grid-cols-2 gap-4 text-sm">
		<div class="flex items-center gap-2">
			<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
				<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
			</svg>
			<div>
				<span class="text-gray-500">Delivery:</span>
				<span class="ml-1 font-medium">{optimizedRoute.estimatedDays} days</span>
			</div>
		</div>
		<div class="flex items-center gap-2">
			<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
				<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
				<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
			</svg>
			<div>
				<span class="text-gray-500">Stops:</span>
				<span class="ml-1 font-medium">{optimizedRoute.path.length - 2}</span>
			</div>
		</div>
	</div>

	<!-- View Details Button -->
	<div class="mt-4">
		<button 
			class="btn btn-ghost btn-sm w-full"
			on:click={toggleRouteDetails}
			type="button"
		>
			{#if showRouteDetails}
				Hide Details
				<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7" />
				</svg>
			{:else}
				View Detailed Breakdown
				<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
				</svg>
			{/if}
		</button>
	</div>

	{#if showRouteDetails}
		<div class="card bg-base-100 mt-4 p-6 shadow-md">
			<h3 class="mb-4 text-lg font-semibold">Detailed Route Breakdown</h3>
			
			<!-- Route Path Summary -->
			<div class="mb-6">
				<div class="text-sm text-gray-500 mb-2">Complete Trade Route</div>
				<div class="flex flex-wrap items-center gap-2 text-base font-medium">
					{#each optimizedRoute.path as country, i}
						<span>{country}</span>
						{#if i < optimizedRoute.path.length - 1}
							<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
								<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
							</svg>
						{/if}
					{/each}
				</div>
			</div>

			<!-- Cost Summary -->
			<div class="bg-success/10 rounded-lg p-4 mb-6">
				<div class="grid grid-cols-3 gap-4 text-center">
					<div>
						<div class="text-xs text-gray-500">Total Cost</div>
						<div class="text-xl font-bold text-success">${optimizedRoute.totalCost.toFixed(2)}</div>
					</div>
					<div>
						<div class="text-xs text-gray-500">You Save</div>
						<div class="text-xl font-bold text-success">${optimizedRoute.savings.toFixed(2)}</div>
					</div>
					<div>
						<div class="text-xs text-gray-500">Delivery Time</div>
						<div class="text-xl font-bold">{optimizedRoute.estimatedDays} days</div>
					</div>
				</div>
			</div>

			<!-- Tariff Breakdown by Leg -->
			<div class="mb-6">
				<h4 class="text-sm font-semibold mb-3">Tariff Breakdown</h4>
				<div class="space-y-3">
					{#each optimizedRoute.tariffBreakdown as leg, index}
						<div class="border border-base-300 rounded-lg p-3 hover:bg-base-200 transition-colors">
							<div class="flex justify-between items-center mb-2">
								<div class="flex items-center gap-2">
									<span class="badge badge-sm">{index + 1}</span>
									<span class="text-sm font-medium">{leg.from} → {leg.to}</span>
								</div>
								<span class="text-sm font-bold">${leg.tariffCost.toFixed(2)}</span>
							</div>
							<div class="text-xs text-gray-500">
								Tariff Rate: {leg.tariffRate}%
							</div>
						</div>
					{/each}
				</div>
			</div>

			<!-- Additional Info -->
			<div class="alert alert-info">
				<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="stroke-current shrink-0 w-6 h-6">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
				</svg>
				<div>
					<div class="text-sm">This route takes advantage of trade agreements and preferential tariff rates.</div>
				</div>
			</div>
		</div>
	{/if}
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

<!-- Save Calculation Modal -->
{#if showSaveModal}
	<div class="modal modal-open">
		<!-- Background which will close the modal -->
		<button
			class="modal-backdrop cursor-pointer"
			on:click={closeSaveModal}>close</button
		>

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

<!-- Calculator Logic-->
<script>
	import { saveCalculation } from "$lib/api/calculationHistory.js";
	import { fetchCountries } from "$lib/api/countries.js";
	import { fetchNews } from "$lib/api/news.js";
	import { calculateTariffCost } from "$lib/api/tariff.js";
	import Alert from "$lib/components/Alert.svelte";
	import { onMount } from "svelte";

	let hsCode = '';
	let exportFrom = '';
	let importTo = '';
	let calculationDate = new Date().toISOString().split('T')[0]; // Set the Calculation Date
	let goodsValue = '';
	let quantity = '';

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
		showAlternativeRoutes = false; // Clear optimized route when recalculating
		optimizedRoute = null;
		isCalculating = true;
		
		if (hsCode && exportFrom && importTo && calculationDate && goodsValue && quantity) {
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
					goodsValue,
					quantity
				});
				
			console.log('Calculation result:', result);
			
			if (result === null) {
				calculationError = "No tariff data found for the specified countries and product. Please check your selection or contact support.";
				showErrorAlert = true;
			} else {
				// Valid tariff data (including 0% tariff)
				calculationResult = result;
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

	// Start: Optimized Route Section
	let showAlternativeRoutes = false;
	let isLoadingRoutes = false;
	let optimizedRoutes = [];
	let expandedRouteIndex = null;

	async function findOptimizedRoute() {
		isLoadingRoutes = true;
		showAlternativeRoutes = false;
		expandedRouteIndex = null; // Reset details view
		
		try {
			// Simulate API call - replace with actual API later
			await new Promise(resolve => setTimeout(resolve, 1500));
			
			// Get country names for display
			const importingCountry = countries.find(c => c.id == importTo);
			const exportingCountry = countries.find(c => c.id == exportFrom);
			
			// Mock 1-3 optimized routes
			const originalCost = parseFloat(calculationResult.totalCost);
			const candidates = [
				{
					path: [exportingCountry.name, "Malaysia", importingCountry.name],
					totalCost: originalCost * 0.85,
					estimatedDays: 12,
					tariffBreakdown: [
						{ from: exportingCountry.name, to: "Malaysia", tariffCost: originalCost * 0.85 * 0.3, tariffRate: 5.0 },
						{ from: "Malaysia", to: importingCountry.name, tariffCost: originalCost * 0.85 * 0.2, tariffRate: 3.5 }
					]
				},
				{
					path: [exportingCountry.name, "Vietnam", importingCountry.name],
					totalCost: originalCost * 0.88,
					estimatedDays: 10,
					tariffBreakdown: [
						{ from: exportingCountry.name, to: "Vietnam", tariffCost: originalCost * 0.88 * 0.28, tariffRate: 4.2 },
						{ from: "Vietnam", to: importingCountry.name, tariffCost: originalCost * 0.88 * 0.22, tariffRate: 3.0 }
					]
				},
				{
					path: [exportingCountry.name, "Thailand", importingCountry.name],
					totalCost: originalCost * 0.9,
					estimatedDays: 11,
					tariffBreakdown: [
						{ from: exportingCountry.name, to: "Thailand", tariffCost: originalCost * 0.9 * 0.3, tariffRate: 5.1 },
						{ from: "Thailand", to: importingCountry.name, tariffCost: originalCost * 0.9 * 0.2, tariffRate: 3.4 }
					]
				}
			];
			// Randomly pick 1-3 options to mimic backend variability
			const count = Math.floor(Math.random() * 3) + 1;
			optimizedRoutes = candidates.slice(0, count)
				.map(r => ({
					...r,
					savings: originalCost - r.totalCost,
					savingsPercent: Math.round(((originalCost - r.totalCost) / originalCost) * 100)
				}))
				.sort((a, b) => a.totalCost - b.totalCost); // ensure cheapest first
			showAlternativeRoutes = true;
		} catch (error) {
			console.error('Error finding optimized route:', error);
			calculationError = 'Failed to find optimized routes. Please try again.';
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
			const importingCountry = countries.find(c => c.id == importTo);
			const exportingCountry = countries.find(c => c.id == exportFrom);
			
			if (!importingCountry || !exportingCountry) {
				throw new Error('Country information not found');
			}
			
			// Prepare calculation data according to SaveCalculationRequestDTO
			const calculationData = {
				calculationName: saveCalculationName,
				productValue: parseFloat(goodsValue),
				currencyCode: 'USD', // Default currency, can be made configurable
				tariffRate: parseFloat(calculationResult.tariffRate),
				tariffType: calculationResult.tariffType,
				calculatedTariffCost: parseFloat(calculationResult.tariffCost),
				totalCost: parseFloat(calculationResult.totalCost),
				notes: saveCalculationNotes || null,
				importingCountryCode: importingCountry.code,
				exportingCountryCode: exportingCountry.code,
				productCategoryCode: parseInt(hsCode)
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

