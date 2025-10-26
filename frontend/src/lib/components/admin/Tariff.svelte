<script lang="ts">
	import { beforeNavigate } from '$app/navigation';
	import { fetchCountries } from '$lib/api/countries.js';
	import { getAllProductCategories } from '$lib/api/productCategory';
	import { createTariff, deleteSpecificTariff, editTariff, getTariffPage } from '$lib/api/tariff';
	import Alert from '$lib/components/Alert.svelte';
	import { onMount } from 'svelte';

	let success = '';
	let error = '';
	export let isBusy = false; // page-level busy indicator for create/edit flows

	// variables for pagination
	let pageNo = 0;
	let totalPage = 0;
	let totalTariffs = 0;
	let size = 5;

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

	async function fetchTariffs(sortKey: string, sortAsc: boolean) {
		isBusy = true;
		try {
			let sortDirection;
			if (sortAsc) {
				sortDirection = 'ascending';
			} else {
				sortDirection = 'descending';
			}

			const result = await getTariffPage(pageNo, size, sortKey, sortDirection);
			
			allTariff = result.data.content;
			totalPage = result.data.totalPages;
			totalTariffs = result.data.totalElements;

			if (pageNo + 1 > totalPage) {
				pageNo = totalPage - 1;
				fetchTariffs(sortKey, sortAsc);
			}
		} catch (err) {
			console.error('Getting all tariff error:', err);
			error = err instanceof Error ? err.message : 'Viewing tariff failed. Please try again.';
		} finally {
			isBusy = false;
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
		fetchTariffs(sortKey, sortAsc);
		fetchProductCategories();
	});

	// Update on page load/refresh
	onMount(() => {
		fetchTariffs(sortKey, sortAsc);
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
									error = '⚠️ Please select a product category';
								}
							} else {
								error = '🌍 Please select an exporting country';
							}
						} else {
							error = '🌍 Please select an importing country';
						}
					} else {
						error = '📅 Please select an effective date';
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

			success = 'Tariff rate created successfully! (ID: ' + result.data.id + ')';
			close();
			fetchTariffs(sortKey, sortAsc);
			fetchProductCategories();
			error = '';
		} catch (err) {
			error =
				err instanceof Error
					? err.message
					: 'Failed to create tariff rate. Please check your data and try again.';
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
			fetchTariffs(sortKey, sortAsc);
			fetchProductCategories();
			error = '';
		} catch (err) {
			error =
				err instanceof Error
					? err.message
					: 'Failed to update tariff rate. Please check your data and try again.';
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
			console.log('Delete result:', result);

			success = '🗑️ ' + (result.message || 'Tariff rate deleted successfully');
			fetchTariffs(sortKey, sortAsc);
			fetchProductCategories();
			error = '';
		} catch (err) {
			error =
				err instanceof Error ? err.message : 'Failed to delete tariff rate. Please try again.';
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
		fetchTariffs(sortKey, sortAsc);
		fetchProductCategories();
	}

	// Function that will return a date time in a readable format
	function readableDateTime(datetime) {
		const date = new Date(datetime);
		return date.toLocaleString();
	}

	// Restrict TariffKey to only contain header values (TariffRecord)
	type TariffKey = keyof TariffRecord;
	let sortKey: TariffKey = "id";
	let sortAsc = true;

	function sortBy(key: TariffKey) {
		if (sortKey === key) {
			sortAsc = !sortAsc;
		} else {
			sortKey = key;
			sortAsc = true;
		}

		fetchTariffs(sortKey, sortAsc);
	}

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

<!-- Global Alerts - Below component title -->
{#if error}
	<Alert type="error" message={error} show={true} autoDismiss={true} />
{/if}

{#if success}
	<Alert type="success" message={success} show={true} autoDismiss={true} />
{/if}

<div class="overflow-x-auto">
	<table class="table-zebra static table">
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
			{#each allTariff as line}
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
						<div class="dropdown dropdown-end static">
							<button class=" btn btn-circle btn-ghost text-xl">⋮</button>
							<ul class="dropdown-content menu menu-sm rounded-box bg-base-100 w-40 p-2 shadow">
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
										class="text-error text-sm font-semibold"
										on:click={() => {
											success = '';
											error = '';
											deleteTariffMethod(line.id);
										}}>Delete</button
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

{#if totalPage > 1}
	<div class="border-base-300 mt-4 flex items-center justify-between border-t pt-4">
		<div class="text-sm text-gray-500">
			Showing {size * pageNo + 1}-{size * pageNo + allTariff.length} of
			{totalTariffs} articles
		</div>
		<div class="flex gap-2">
			<button
				class="btn btn-sm btn-outline"
				disabled={pageNo === 0}
				on:click={() => {
					pageNo--;
					fetchTariffs(sortKey, sortAsc);
				}}
			>
				Previous
			</button>
			<button
				class="btn btn-sm btn-outline"
				disabled={pageNo + 1 >= totalPage}
				on:click={() => {
					pageNo++;
					fetchTariffs(sortKey, sortAsc);
				}}
			>
				Next
			</button>
		</div>
	</div>
{/if}

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
									class="dropdown-panel border-base-300 bg-base-100 absolute left-0 right-0 top-full z-20 mt-1 rounded-md border shadow-lg"
									on:click={(e) => {
										e.stopPropagation();
										console.log(selectedTariff.importingCountryCode);
									}}
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
												class="hover:bg-base-200 flex cursor-pointer items-center justify-between px-3 py-2 text-sm {selectedTariff.importingCountryCode ==
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
											<div class="text-base-content/60 px-3 py-2 text-sm">No countries found</div>
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
									class="dropdown-panel border-base-300 bg-base-100 absolute left-0 right-0 top-full z-20 mt-1 rounded-md border shadow-lg"
									on:click={(e) => {
										e.stopPropagation();
										console.log(selectedTariff.exportingCountryCode);
									}}
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
												class="hover:bg-base-200 flex cursor-pointer items-center justify-between px-3 py-2 text-sm {selectedTariff.exportingCountryCode ==
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
											<div class="text-base-content/60 px-3 py-2 text-sm">No countries found</div>
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
