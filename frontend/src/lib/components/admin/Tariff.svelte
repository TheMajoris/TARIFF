<script lang="ts">
	import { createTariff, deleteSpecificTariff, editTariff, getAllTariff } from '$lib/api/tariff';
	import { getAllProductCategories } from '$lib/api/productCategory';
	import { beforeNavigate } from '$app/navigation';
	import { onMount } from 'svelte';
	import { fetchCountries } from '$lib/api/countries.js';

	let success = '';
	let error = '';
	export let isBusy = false; // page-level busy indicator for create/edit flows

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
	let allProductCategories: ProductCategory[] = [];
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

	async function fetchTariffs() {
		try {
			const result = await getAllTariff();
			allTariff = result;
		} catch (err) {
			console.error('Getting all tariff error:', err);
			error = err instanceof Error ? err.message : 'Viewing tariff failed. Please try again.';
		}
	}

	async function fetchProductCategories() {
		try {
			const result = await getAllProductCategories();
			allProductCategories = result;
		} catch (err) {
			console.error('Getting all product category error:', err);
			error =
				err instanceof Error ? err.message : 'Viewing product category failed. Please try again.';
		}
	}

	// Update when any page -> admin
	beforeNavigate(() => {
		fetchTariffs();
		fetchProductCategories();
	});

	// Update on page load/refresh
	onMount(() => {
		fetchTariffs();
		fetchProductCategories();
	});

	let view = false;
	let edit = false;
	export let createTariffBoolean = false;

	// Function to validate & submit tariff
	function submitTariff() {
		if (TariffValidation()) {
			if (createTariffBoolean) {
				createTariffMethod();
			} else if (edit) {
				editTariffMethod();
			}
		}
	}

	// Function to validate Tariff
	function TariffValidation() {
		if (
			selectedTariff.tariffRate != null &&
			selectedTariff.tariffRate >= 0 &&
			selectedTariff.tariffRate <= 999.9999
		) {
			if (selectedTariff.tariffType != '' && selectedTariff.tariffType.length <= 50) {
				if (selectedTariff.rateUnit.length <= 20) {
					if (selectedTariff.effectiveDate != null) {
						if (selectedTariff.importingCountryCode != null) {
							if (selectedTariff.exportingCountryCode != null) {
								if (selectedTariff.productCategory != null) {
									return true;
								} else {
									error = 'Product Category cannot be null';
								}
							} else {
								error = 'Exporting Country Code cannot be null';
							}
						} else {
							error = 'Importing Country Code cannot be null';
						}
					} else {
						error = 'Effective Date cannot be null';
					}
				} else {
					error = 'Rate Unit can only be up to 20 characters';
				}
			} else {
				error = 'Tariff Type must not be blank and can only be up to 50 characters';
			}
		} else {
			error = 'Tariff Type can only be from 0 to 999.9999';
		}

		return false;
	}

	// Function to create tariff
	async function createTariffMethod() {
		isBusy = true;
		let payload = {
			tariffRate: selectedTariff.tariffRate,
			tariffType: selectedTariff.tariffType,
			rateUnit: selectedTariff.rateUnit,
			effectiveDate: selectedTariff.effectiveDate,
			expiryDate: selectedTariff.expiryDate,
			preferentialTariff: selectedTariff.preferentialTariff,
			importingCountryCode: selectedTariff.importingCountryCode,
			exportingCountryCode: selectedTariff.exportingCountryCode,
			hsCode: selectedTariff.productCategory.categoryCode
		};

		try {
			const result = await createTariff(payload);

			success = 'Created Tariff id: ' + result.data.id;
			close();
			fetchTariffs();
			fetchProductCategories();
			error = '';
		} catch (err) {
			error = err instanceof Error ? err.message : 'Creating tariff failed. Please try again.';
			console.error('Creating tariff error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function to edit tariff
	async function editTariffMethod() {
		isBusy = true;
		let payload = {
			id: selectedTariff.id,
			tariffRate: selectedTariff.tariffRate,
			tariffType: selectedTariff.tariffType,
			rateUnit: selectedTariff.rateUnit,
			effectiveDate: selectedTariff.effectiveDate,
			expiryDate: selectedTariff.expiryDate,
			preferentialTariff: selectedTariff.preferentialTariff,
			importingCountryCode: selectedTariff.importingCountryCode,
			exportingCountryCode: selectedTariff.exportingCountryCode,
			productCategory: {
				id: selectedTariff.productCategory.id,
				categoryCode: selectedTariff.productCategory.categoryCode,
				categoryName: selectedTariff.productCategory.categoryName,
				description: selectedTariff.productCategory.description,
				isActive: selectedTariff.productCategory.isActive
			}
		};

		try {
			const result = await editTariff(payload);

			success = result.message;
			close();
			fetchTariffs();
			fetchProductCategories();
			error = '';
		} catch (err) {
			error = err instanceof Error ? err.message : 'Editing tariff failed. Please try again.';
			console.error('Editing tariff error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function to delete tariff
	async function deleteTariffMethod(id: number) {
		isBusy = true;
		try {
			const result = await deleteSpecificTariff(id);

			success = result.message;
			fetchTariffs();
			fetchProductCategories();
			error = '';
		} catch (err) {
			error = err instanceof Error ? err.message : 'Deleting tariff failed. Please try again.';
			console.error('Deleting tariff error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function used to close the popup and reset the tariff value
	function close() {
		edit = false;
		createTariffBoolean = false;
		view = false;
		selectedTariff = blankTariff();
		fetchTariffs();
		fetchProductCategories();
	}

	// Function that will return a date time in a readable format
	function readableDateTime(datetime) {
		const date = new Date(datetime);
		return date.toLocaleString();
	}

	// Restrict TariffKey to only contain header values (TariffRecord)
	type TariffKey = keyof TariffRecord;
	let sortKey: TariffKey | null = null;
	let sortAsc = true;

	function sortBy(key: TariffKey) {
		if (sortKey === key) {
			sortAsc = !sortAsc;
		} else {
			sortKey = key;
			sortAsc = true;
		}
	}

	// Need to give a new array
	$: sortedTariffs =
		// If not sorted then use default data, else sort
		sortKey === null
			? allTariff
			: [...allTariff].sort((a, b) => {
					const key = sortKey as TariffKey;
					let valA = a[key];
					let valB = b[key];

					const numA = Number(valA);
					const numB = Number(valB);

					// If the data is a number
					if (!isNaN(numA) && !isNaN(numB)) {
						return sortAsc ? numA - numB : numB - numA;
					}

					// If it is not a number
					return sortAsc
						? String(valA).localeCompare(String(valB))
						: String(valB).localeCompare(String(valA));
				});

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
</script>

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

<div class="overflow-x-auto">
	<table class="table static table-zebra">
		<thead class="bg-base-300 text-base font-semibold">
			<tr>
				<th on:click={() => sortBy('id')} class="cursor-pointer"
					>Tariff Id {sortKey === 'id' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('importingCountryCode')} class="cursor-pointer"
					>Importing Country {sortKey === 'importingCountryCode' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('exportingCountryCode')} class="cursor-pointer"
					>Exporting Country {sortKey === 'exportingCountryCode' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('preferentialTariff')} class="cursor-pointer"
					>Preferential Tariff {sortKey === 'preferentialTariff' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('tariffType')} class="cursor-pointer"
					>Tariff Type {sortKey === 'tariffType' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('tariffRate')} class="cursor-pointer"
					>Tariff Rate {sortKey === 'tariffRate' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('effectiveDate')} class="cursor-pointer"
					>Effective Date {sortKey === 'effectiveDate' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('expiryDate')} class="cursor-pointer"
					>Expiry Date {sortKey === 'expiryDate' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th></th>
			</tr>
		</thead>
		<tbody>
			{#each sortedTariffs as line}
				<tr>
					<td>{line.id}</td>
					<td>{line.importingCountryCode}</td>
					<td>{line.exportingCountryCode}</td>
					<td>{line.preferentialTariff ? 'Yes' : 'No'}</td>
					<td>{line.tariffType}</td>
					<td>{line.tariffRate} {line.rateUnit}</td>
					<td>{line.effectiveDate}</td>
					<td>{line.expiryDate}</td>
					<td class="p-0">
						<div class="dropdown static dropdown-end">
							<button class=" btn btn-circle text-xl btn-ghost">⋮</button>
							<ul class="dropdown-content menu w-40 menu-sm rounded-box bg-base-100 p-2 shadow">
								<li>
									<button
										class="text-sm"
										on:click={() => {
											selectedTariff = line;
											view = true;
											success = '';
											error = '';
										}}>View</button
									>
								</li>
								<li>
									<button
										class="text-sm"
										on:click={() => {
											selectedTariff = line;
											edit = true;
											success = '';
											error = '';
										}}>Edit</button
									>
								</li>
								<li>
									<button
										class="text-sm font-semibold text-error"
										on:click={deleteTariffMethod(line.id)}>Delete</button
									>
								</li>
							</ul>
						</div>
					</td>
				</tr>
			{/each}
		</tbody>
	</table>
</div>

<!-- Modal -->
{#if view || edit || createTariffBoolean}
	<div class="modal-open modal">
		<!-- Background which will close the modal -->
		<button
			class="modal-backdrop cursor-pointer"
			on:click={() => {
				close();
				success = '';
				error = '';
			}}>close</button
		>

		<div class="modal-box max-w-2xl">
			<h3 class="mb-4 text-lg font-bold">
				{createTariffBoolean ? 'Create' : edit ? 'Edit' : 'View'} Tariff
			</h3>
			{#if edit || createTariffBoolean}
				<form class="grid grid-cols-1 gap-4" on:submit|preventDefault={submitTariff}>
					<div>
						<label class="label" for="tariff_id">
							<span class="label-text font-semibold">Tariff ID</span>
						</label>
						<input
							type="text"
							id="tariff_id"
							bind:value={selectedTariff.id}
							class="input-bordered input w-full"
							disabled
						/>
					</div>

					<div>
						<label class="label text-sm font-medium">Importing Country</label>
						<div class="relative">
							<div
								class="select-bordered select flex w-full cursor-pointer items-center justify-between text-sm"
								on:click={() => (showImportToDropdown = !showImportToDropdown)}
								on:blur={(e) => {
									if (!e.relatedTarget || !e.relatedTarget.closest('.dropdown-panel')) {
										setTimeout(() => (showImportToDropdown = false), 200);
									}
								}}
								tabindex="0"
							>
								<span class="truncate">
									{#if selectedTariff.importingCountryCode}
										{(() => {
											const selected = countries.find(
												(c) => c.code == selectedTariff.importingCountryCode
											);
											return selected ? `(${selected.code}) ${selected.name}` : 'Select country';
										})()}
									{:else}
										Select country
									{/if}
								</span>
							</div>

							{#if showImportToDropdown}
								<div
									class="dropdown-panel absolute top-full right-0 left-0 z-20 mt-1 rounded-md border border-base-300 bg-base-100 shadow-lg"
									on:click={(e) => {
										e.stopPropagation();
										console.log(selectedTariff.importingCountryCode);
									}}
									on:mousedown={(e) => e.stopPropagation()}
								>
									<div class="border-b border-base-300 p-2">
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
												class="flex cursor-pointer items-center justify-between px-3 py-2 text-sm hover:bg-base-200 {selectedTariff.importingCountryCode ==
												country.code
													? 'bg-primary text-primary-content'
													: ''}"
												on:click={() => {
													selectedTariff.importingCountryCode = country.code;
													importToSearch = '';
													showImportToDropdown = false;
												}}
											>
												<span>({country.code}) {country.name}</span>
												{#if selectedTariff.importingCountryCode == country.code}
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
											<div class="px-3 py-2 text-sm text-base-content/60">No countries found</div>
										{/if}
									</div>
								</div>
							{/if}
						</div>
					</div>

					<div>
						<label class="label text-sm font-medium">Exporting Country</label>
						<div class="relative">
							<div
								class="select-bordered select flex w-full cursor-pointer items-center justify-between text-sm"
								on:click={() => (showExportFromDropdown = !showExportFromDropdown)}
								on:blur={(e) => {
									if (!e.relatedTarget || !e.relatedTarget.closest('.dropdown-panel')) {
										setTimeout(() => (showExportFromDropdown = false), 200);
									}
								}}
								tabindex="0"
							>
								<span class="truncate">
									{#if selectedTariff.exportingCountryCode}
										{(() => {
											const selected = countries.find(
												(c) => c.code == selectedTariff.exportingCountryCode
											);
											return selected ? `(${selected.code}) ${selected.name}` : 'Select country';
										})()}
									{:else}
										Select country
									{/if}
								</span>
							</div>

							{#if showExportFromDropdown}
								<div
									class="dropdown-panel absolute top-full right-0 left-0 z-20 mt-1 rounded-md border border-base-300 bg-base-100 shadow-lg"
									on:click={(e) => {
										e.stopPropagation();
										console.log(selectedTariff.exportingCountryCode);
									}}
									on:mousedown={(e) => e.stopPropagation()}
								>
									<div class="border-b border-base-300 p-2">
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
												class="flex cursor-pointer items-center justify-between px-3 py-2 text-sm hover:bg-base-200 {selectedTariff.exportingCountryCode ==
												country.code
													? 'bg-primary text-primary-content'
													: ''}"
												on:click={() => {
													selectedTariff.exportingCountryCode = country.code;
													exportFromSearch = '';
													showExportFromDropdown = false;
												}}
											>
												<span>({country.code}) {country.name}</span>
												{#if selectedTariff.exportingCountryCode == country.code}
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
											<div class="px-3 py-2 text-sm text-base-content/60">No countries found</div>
										{/if}
									</div>
								</div>
							{/if}
						</div>
					</div>

					<div>
						<label class="label" for="product_category_code">
							<span class="label-text font-semibold">(HSCode) Product Category Name</span>
						</label>

						<select
							id="product_category_code"
							bind:value={selectedTariff.productCategory.categoryCode}
							class="select-bordered select w-full"
						>
							{#each allProductCategories as line}
								{#if selectedTariff.productCategory.categoryCode == line.categoryCode}
									<option value={line.categoryCode} selected
										>({line.categoryCode}) {line.categoryName}</option
									>
								{:else}
									<option value={line.categoryCode}
										>({line.categoryCode}) {line.categoryName}</option
									>
								{/if}
							{/each}
						</select>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="tariff_rate">
								<span class="label-text font-semibold">Tariff Rate</span>
							</label>
							<input
								type="number"
								id="tariff_rate"
								bind:value={selectedTariff.tariffRate}
								class="input-bordered input w-full"
							/>
						</div>
						<div>
							<label class="label" for="rate_unit">
								<span class="label-text font-semibold">Rate Unit</span>
							</label>
							<select
								id="rate_unit"
								bind:value={selectedTariff.rateUnit}
								class="select-bordered select w-full"
							>
								<option></option>
								<option>ad valorem</option>
							</select>
						</div>
					</div>

					<div>
						<label class="label" for="tariff_type">
							<span class="label-text font-semibold">Tariff Type</span>
						</label>
						<input
							type="text"
							id="tariff_type"
							bind:value={selectedTariff.tariffType}
							class="input-bordered input w-full"
						/>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="effective_date">
								<span class="label-text font-semibold">Effective Date</span>
							</label>
							<input
								type="date"
								id="effective_date"
								bind:value={selectedTariff.effectiveDate}
								class="input-bordered input w-full"
							/>
						</div>
						<div>
							<label class="label" for="expiry_date">
								<span class="label-text font-semibold">Expiry Date</span>
							</label>
							<input
								type="date"
								id="expiry_date"
								bind:value={selectedTariff.expiryDate}
								class="input-bordered input w-full"
							/>
						</div>
					</div>

					<div>
						<label class="label" for="preferential_tariff">
							<span class="label-text font-semibold">Preferential Tariff</span>
						</label>
						<select
							id="preferential_tariff"
							value={selectedTariff.preferentialTariff + ''}
							on:change={(e) =>
								(selectedTariff.preferentialTariff =
									(e.currentTarget as HTMLSelectElement).value === 'true')}
							class="select-bordered select w-full"
						>
							<option value="true">Yes</option>
							<option value="false">No</option>
						</select>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="created_at">
								<span class="label-text font-semibold">Created At</span>
							</label>
							<input
								type="text"
								id="created_at"
								value={readableDateTime(selectedTariff.createdAt)}
								class="input-bordered input w-full"
								disabled
							/>
						</div>
						<div>
							<label class="label" for="updated_at">
								<span class="label-text font-semibold">Updated At</span>
							</label>
							<input
								type="text"
								id="updated_at"
								value={readableDateTime(selectedTariff.updatedAt)}
								class="input-bordered input w-full"
								disabled
							/>
						</div>
					</div>

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

					<div class="modal-action">
						<button
							type="button"
							class="btn"
							on:click={() => {
								close();
								success = '';
								error = '';
							}}>Close</button
						>
						<button type="submit" class="btn btn-primary" disabled={isBusy}>Submit</button>
					</div>
				</form>
			{:else}
				<!-- view -->
				<div class="grid grid-cols-1 gap-4">
					<div>
						<label class="label" for="tariff_id">
							<span class="label-text font-semibold">Tariff ID</span>
						</label>
						<p class="w-full">{selectedTariff.id}</p>
					</div>

					<div>
						<label class="label" for="importing_country">
							<span class="label-text font-semibold">Importing Country</span>
						</label>
						<p class="w-full">{selectedTariff.importingCountryCode}</p>
					</div>

					<div>
						<label class="label" for="exporting_country">
							<span class="label-text font-semibold">Exporting Country</span>
						</label>
						<p class="w-full">{selectedTariff.exportingCountryCode}</p>
					</div>

					<div>
						<label class="label" for="product_category_name">
							<span class="label-text font-semibold">(HSCode) Product Category Name</span>
						</label>
						<p class="w-full">
							({selectedTariff.productCategory.categoryCode}) {selectedTariff.productCategory
								.categoryName}
						</p>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="tariff_rate">
								<span class="label-text font-semibold">Tariff Rate</span>
							</label>
							<p class="w-full">{selectedTariff.tariffRate}</p>
						</div>
						<div>
							<label class="label" for="rate_unit">
								<span class="label-text font-semibold">Rate Unit</span>
							</label>
							<p class="w-full">{selectedTariff.rateUnit}</p>
						</div>
					</div>

					<div>
						<label class="label" for="tariff_type">
							<span class="label-text font-semibold">Tariff Type</span>
						</label>
						<p class="w-full">{selectedTariff.tariffType}</p>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="effective_date">
								<span class="label-text font-semibold">Effective Date</span>
							</label>
							<p class="w-full">{selectedTariff.effectiveDate}</p>
						</div>
						<div>
							<label class="label" for="expiry_date">
								<span class="label-text font-semibold">Expiry Date</span>
							</label>
							<p class="w-full">{selectedTariff.expiryDate}</p>
						</div>
					</div>

					<div>
						<label class="label" for="preferential_tariff">
							<span class="label-text font-semibold">Preferential Tariff</span>
						</label>
						<p class="w-full">{selectedTariff.preferentialTariff ? 'Yes' : 'No'}</p>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="created_at">
								<span class="label-text font-semibold">Created At</span>
							</label>
							<p class="w-full">{readableDateTime(selectedTariff.createdAt)}</p>
						</div>
						<div>
							<label class="label" for="updated_at">
								<span class="label-text font-semibold">Updated At</span>
							</label>
							<p class="w-full">{readableDateTime(selectedTariff.updatedAt)}</p>
						</div>
					</div>
					<div class="modal-action">
						<button
							type="button"
							class="btn"
							on:click={() => {
								close();
								success = '';
								error = '';
							}}>Close</button
						>
					</div>
				</div>
			{/if}
		</div>
	</div>
{/if}
