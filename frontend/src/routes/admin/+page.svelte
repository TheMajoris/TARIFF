<script lang="ts">
	import { createTariff, deleteSpecificTariff, getAllTariff } from '$lib/api/tariff';
	import { beforeNavigate } from '$app/navigation';
	import { onMount } from 'svelte';

	let success = '';
	let error = '';

	type ProductCategory = {
		categoryCode: number;
		categoryName: string;
		description: string;
		id: number;
		isActive: boolean;
		tariffBaseRate: number | null;
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
				categoryCode: 0,
				categoryName: '',
				description: '',
				id: 0,
				isActive: false,
				tariffBaseRate: 0
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
		}
	}

	// Update when any page -> admin
	beforeNavigate(() => {
		fetchTariffs();
	});

	// Update on page load/refresh
	onMount(() => {
		fetchTariffs();
	});

	let view = false;
	let edit = false;
	let create = false;

	// Function to validate & submit tariff
	function submitTariff() {
		// Check for blank values
		console.log(selectedTariff);
		if (
			selectedTariff.id !== undefined &&
			selectedTariff.importingCountryCode !== undefined &&
			selectedTariff.exportingCountryCode !== undefined &&
			selectedTariff.productCategory &&
			selectedTariff.productCategory.categoryCode !== undefined &&
			selectedTariff.productCategory.categoryName !== undefined &&
			selectedTariff.productCategory.description !== undefined &&
			selectedTariff.productCategory.isActive !== undefined &&
			selectedTariff.productCategory.tariffBaseRate !== undefined &&
			selectedTariff.productCategory.id !== undefined &&
			selectedTariff.tariffRate !== undefined &&
			selectedTariff.rateUnit !== undefined &&
			selectedTariff.tariffType !== undefined &&
			selectedTariff.effectiveDate !== undefined &&
			selectedTariff.expiryDate !== undefined &&
			selectedTariff.preferentialTariff !== undefined
		) {
			if (create) {
				createTariffMethod();
			} else if (edit) {
				editTariffMethod();
			}
		} else {
			alert('Please fill in all fields.');
		}
	}

	// Function to create tariff
	async function createTariffMethod() {
		let payload = {
			tariffRate: selectedTariff.tariffRate,
			tariffType: selectedTariff.tariffType,
			rateUnit: selectedTariff.rateUnit,
			effectiveDate: selectedTariff.effectiveDate,
			expiryDate: selectedTariff.expiryDate,
			preferentialTariff: selectedTariff.preferentialTariff,
			importingCountryCode: selectedTariff.importingCountryCode,
			exportingCountryCode: selectedTariff.exportingCountryCode,
			productCategory: {
				categoryCode: selectedTariff.productCategory.categoryCode,
				categoryName: selectedTariff.productCategory.categoryName,
				description: selectedTariff.productCategory.description
			}
		};

		try {
			const result = await createTariff(payload);

			success = result.message;
			close();
			fetchTariffs();
			error = '';
		} catch (err) {
			error = err instanceof Error ? err.message : 'Deleting tariff failed. Please try again.';
			console.error('Deleting tariff error:', err);
		}
	}

	// Function to edit tariff
	function editTariffMethod() {
		alert('edit');
	}

	// Function to edit tariff
	async function deleteTariffMethod(id: number) {
		try {
			const result = await deleteSpecificTariff(id);

			success = result.message;
			fetchTariffs();
			error = '';
		} catch (err) {
			error = err instanceof Error ? err.message : 'Deleting tariff failed. Please try again.';
			console.error('Deleting tariff error:', err);
		}
	}

	// Function used to close the popup and reset the tariff value
	function close() {
		edit = false;
		create = false;
		view = false;
		selectedTariff = blankTariff();
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
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Admin</h1>

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
	<!-- Two-column layout -->
	<div class="grid grid-cols-1">
		<!-- Tariffs Card -->
		<div class="card bg-base-100 p-6 shadow-md">
			<div class="flex items-center justify-between py-6">
				<h2 class="mb-1 text-lg font-semibold">Tariffs</h2>
				<button class="btn btn-primary" on:click={() => (create = true)}>Create</button>
			</div>
			<div class="overflow-x-auto">
				<table class="table-zebra static table">
					<thead class="bg-base-300 text-base font-semibold">
						<tr>
							<th on:click={() => sortBy('id')} class="cursor-pointer"
								>Tariff Id {sortKey === 'id' ? (sortAsc ? '▲' : '▼') : ''}</th
							>
							<th on:click={() => sortBy('importingCountryCode')} class="cursor-pointer"
								>Importing Country {sortKey === 'importingCountryCode'
									? sortAsc
										? '▲'
										: '▼'
									: ''}</th
							>
							<th on:click={() => sortBy('exportingCountryCode')} class="cursor-pointer"
								>Exporting Country {sortKey === 'exportingCountryCode'
									? sortAsc
										? '▲'
										: '▼'
									: ''}</th
							>
							<th on:click={() => sortBy('preferentialTariff')} class="cursor-pointer"
								>Preferential Tariff {sortKey === 'preferentialTariff'
									? sortAsc
										? '▲'
										: '▼'
									: ''}</th
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
								<td>{line.preferentialTariff}</td>
								<td>{line.tariffType}</td>
								<td>{line.tariffRate} {line.rateUnit}</td>
								<td>{line.effectiveDate}</td>
								<td>{line.expiryDate}</td>
								<td class="p-0">
									<div class="dropdown dropdown-end static">
										<button class=" btn btn-ghost btn-circle text-xl">⋮</button>
										<ul
											class="menu menu-sm dropdown-content bg-base-100 rounded-box w-40 p-2 shadow"
										>
											<li>
												<button
													class="text-sm"
													on:click={() => {
														selectedTariff = line;
														view = true;
													}}>View</button
												>
											</li>
											<li>
												<button
													class="text-sm"
													on:click={() => {
														selectedTariff = line;
														edit = true;
													}}>Edit</button
												>
											</li>
											<li>
												<button
													class="text-error text-sm font-semibold"
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
		</div>
	</div>
</div>

<!-- Modal -->
{#if view || edit || create}
	<div class="modal modal-open">
		<!-- Background which will close the modal -->
		<button class="modal-backdrop cursor-pointer" on:click={close}>close</button>

		<div class="modal-box max-w-2xl">
			<h3 class="mb-4 text-lg font-bold">{create ? 'Create' : edit ? 'Edit' : 'View'} Tariff</h3>

			{#if edit || create}
				<form class="grid grid-cols-1 gap-4" on:submit|preventDefault={submitTariff}>
					<div>
						<label class="label" for="tariff_id">
							<span class="label-text font-semibold">Tariff ID</span>
						</label>
						<input
							type="text"
							id="tariff_id"
							bind:value={selectedTariff.id}
							class="input input-bordered w-full"
							disabled
						/>
					</div>

					<div>
						<label class="label" for="importing_country">
							<span class="label-text font-semibold">Importing Country</span>
						</label>
						<input
							type="text"
							id="importing_country"
							bind:value={selectedTariff.importingCountryCode}
							class="input input-bordered w-full"
						/>
					</div>

					<div>
						<label class="label" for="exporting_country">
							<span class="label-text font-semibold">Exporting Country</span>
						</label>
						<input
							type="text"
							id="exporting_country"
							bind:value={selectedTariff.exportingCountryCode}
							class="input input-bordered w-full"
						/>
					</div>

					<div>
						<label class="label" for="product_category_code">
							<span class="label-text font-semibold">Product Category Code</span>
						</label>
						<input
							type="text"
							id="product_category_code"
							bind:value={selectedTariff.productCategory.categoryCode}
							class="input input-bordered w-full"
						/>
					</div>

					<div>
						<label class="label" for="product_category_name">
							<span class="label-text font-semibold">Product Category Name</span>
						</label>
						<input
							type="text"
							id="product_category_name"
							bind:value={selectedTariff.productCategory.categoryName}
							class="input input-bordered w-full"
						/>
					</div>

					<div>
						<label class="label" for="product_category_description">
							<span class="label-text font-semibold">Product Category Description</span>
						</label>
						<textarea
							id="product_category_description"
							bind:value={selectedTariff.productCategory.description}
							class="textarea textarea-bordered w-full"
						></textarea>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="product_category_active">
								<span class="label-text font-semibold">Product Category Active</span>
							</label>
							<select
								id="product_category_active"
								bind:value={selectedTariff.productCategory.isActive}
								class="select select-bordered w-full"
							>
								<option value={true}>Yes</option>
								<option value={false}>No</option>
							</select>
						</div>
						<div>
							<label class="label" for="product_category_base_rate">
								<span class="label-text font-semibold">Product Category Base Rate</span>
							</label>
							<input
								type="number"
								id="product_category_base_rate"
								bind:value={selectedTariff.productCategory.tariffBaseRate}
								class="input input-bordered w-full"
							/>
						</div>
					</div>

					<div>
						<label class="label" for="product_category_id">
							<span class="label-text font-semibold">Product Category ID</span>
						</label>
						<input
							type="text"
							id="product_category_id"
							bind:value={selectedTariff.productCategory.id}
							class="input input-bordered w-full"
							disabled
						/>
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
								class="input input-bordered w-full"
							/>
						</div>
						<div>
							<label class="label" for="rate_unit">
								<span class="label-text font-semibold">Rate Unit</span>
							</label>
							<select
								id="rate_unit"
								bind:value={selectedTariff.rateUnit}
								class="select select-bordered w-full"
							>
								<option selected>ad valorem</option>
								<option>$</option>
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
							class="input input-bordered w-full"
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
								class="input input-bordered w-full"
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
								class="input input-bordered w-full"
							/>
						</div>
					</div>

					<div>
						<label class="label" for="preferential_tariff">
							<span class="label-text font-semibold">Preferential Tariff</span>
						</label>
						<select
							id="preferential_tariff"
							bind:value={selectedTariff.preferentialTariff}
							class="select select-bordered w-full"
						>
							<option value={true}>Yes</option>
							<option value={false}>No</option>
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
								bind:value={selectedTariff.createdAt}
								class="input input-bordered w-full"
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
								bind:value={selectedTariff.updatedAt}
								class="input input-bordered w-full"
								disabled
							/>
						</div>
					</div>

					<div class="modal-action">
						<button type="button" class="btn" on:click={close}>Close</button>
						<button type="submit" class="btn btn-primary">Submit</button>
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
						<label class="label" for="product_category_code">
							<span class="label-text font-semibold">Product Category Code</span>
						</label>
						<p class="w-full">{selectedTariff.productCategory.categoryCode}</p>
					</div>

					<div>
						<label class="label" for="product_category_name">
							<span class="label-text font-semibold">Product Category Name</span>
						</label>
						<p class="w-full">{selectedTariff.productCategory.categoryName}</p>
					</div>

					<div>
						<label class="label" for="product_category_description">
							<span class="label-text font-semibold">Product Category Description</span>
						</label>
						<p class="w-full">{selectedTariff.productCategory.description}</p>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="product_category_active">
								<span class="label-text font-semibold">Product Category Active</span>
							</label>
							<p class="w-full">{selectedTariff.productCategory.isActive ? 'Yes' : 'No'}</p>
						</div>
						<div>
							<label class="label" for="product_category_base_rate">
								<span class="label-text font-semibold">Product Category Base Rate</span>
							</label>
							<p class="w-full">{selectedTariff.productCategory.tariffBaseRate}</p>
						</div>
					</div>

					<div>
						<label class="label" for="product_category_id">
							<span class="label-text font-semibold">Product Category ID</span>
						</label>
						<p class="w-full">{selectedTariff.productCategory.id}</p>
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
						<p class="w-full">{selectedTariff.preferentialTariff}</p>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="created_at">
								<span class="label-text font-semibold">Created At</span>
							</label>
							<p class="w-full">{selectedTariff.createdAt}</p>
						</div>
						<div>
							<label class="label" for="updated_at">
								<span class="label-text font-semibold">Updated At</span>
							</label>
							<p class="w-full">{selectedTariff.updatedAt}</p>
						</div>
					</div>

					<div class="modal-action">
						<button
							type="button"
							class="btn"
							on:click={() => {
								close();
							}}>Close</button
						>
					</div>
				</div>
			{/if}
		</div>
	</div>
{/if}
