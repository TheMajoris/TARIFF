<script lang="ts">
	import { createTariff, deleteSpecificTariff, editTariff, getAllTariff } from '$lib/api/tariff';
	import { beforeNavigate } from '$app/navigation';
	import { onMount } from 'svelte';
	import { fetchCountries } from '$lib/api/countries.js';

	let success = '';
	let error = '';

	let view = false;
	let edit = false;
	export let createCategoryBoolean = false;
	export let isBusy = false; // page-level busy indicator for create/edit flows

	type ProductCategory = {
		categoryCode: string;
		categoryName: string;
		description: string;
		id: number;
		isActive: boolean;
	};

	// test data
	let allCategory: ProductCategory[] = [
		{
			categoryCode: '850110',
			categoryName: 'Electric Motors ≤ 37.5W',
			description: 'Electric motors of an output not exceeding 37.5 W',
			id: 1,
			isActive: true
		},
		{
			categoryCode: '850120',
			categoryName: 'Universal AC/DC Motors > 37.5W',
			description: 'Universal AC/DC motors of an output exceeding 37.5 W',
			id: 2,
			isActive: true
		},
		{
			categoryCode: '850131',
			categoryName: 'DC Motors > 37.5W ≤ 750W',
			description: 'DC motors of an output exceeding 37.5 W but not exceeding 750 W',
			id: 3,
			isActive: true
		},
		{
			categoryCode: '850132',
			categoryName: 'DC Motors > 750W ≤ 75kW',
			description: 'DC motors of an output exceeding 750 W but not exceeding 75 kW',
			id: 4,
			isActive: true
		}
	];
	let selectedCategory: ProductCategory = blankCategory();
	function blankCategory() {
		return {
			categoryCode: '',
			categoryName: '',
			description: '',
			id: 0,
			isActive: false
		};
	}

	// type TariffRecord = {
	// 	id: number;
	// 	createdAt: string; // ISO date string
	// 	updatedAt: string; // ISO date string
	// 	effectiveDate: string; // ISO date string
	// 	expiryDate: string; // ISO date string
	// 	exportingCountryCode: string;
	// 	importingCountryCode: string;
	// 	preferentialTariff: boolean;
	// 	productCategory: ProductCategory;
	// 	rateUnit: string;
	// 	tariffRate: number;
	// 	tariffType: string;
	// };
	// let allTariff: TariffRecord[] = [];
	// let selectedCategory: TariffRecord = blankTariff();
	// function blankTariff() {
	// 	return {
	// 		id: 0,
	// 		createdAt: '',
	// 		updatedAt: '',
	// 		effectiveDate: '',
	// 		expiryDate: '',
	// 		exportingCountryCode: '',
	// 		importingCountryCode: '',
	// 		preferentialTariff: false,
	// 		productCategory: {
	// 			categoryCode: '',
	// 			categoryName: '',
	// 			description: '',
	// 			id: 0,
	// 			isActive: false
	// 		},
	// 		rateUnit: '',
	// 		tariffRate: 0,
	// 		tariffType: ''
	// 	};
	// }

	// async function fetchTariffs() {
	// isBusy = true;
	// 	try {
	// 		const result = await getAllTariff();
	// 		allTariff = result;
	// 	} catch (err) {
	// 		console.error('Getting all tariff error:', err);
	// 		error = err instanceof Error ? err.message : 'Viewing tariff failed. Please try again.';
	// 	} finally {
	// 	isBusy = false;
	// }
	// }

	// // Update when any page -> admin
	// beforeNavigate(() => {
	// 	fetchTariffs();
	// });

	// // Update on page load/refresh
	// onMount(() => {
	// 	fetchTariffs();
	// });

	// // Function to validate & submit tariff
	// function submitTariff() {
	// 	if (CategoryValidation()) {
	// 		if (createCategoryBoolean) {
	// 			createTariffMethod();
	// 		} else if (edit) {
	// 			editTariffMethod();
	// 		}
	// 	}
	// }

	// // Function to validate Category
	// function CategoryValidation() {
	// 	if (
	// 		selectedCategory.productCategory.categoryCode != null &&
	// 		/^\d{6}$/.test(selectedCategory.productCategory.categoryCode)
	// 	) {
	// 		if (
	// 			selectedCategory.productCategory.categoryName != '' &&
	// 			selectedCategory.productCategory.categoryName.length <= 100
	// 		) {
	// 			if (selectedCategory.productCategory.description.length <= 500) {
	// 				return true;
	// 			} else {
	// 				error = 'Category Description can only have up to 500 characters';
	// 			}
	// 		} else {
	// 			error = 'Category Name can only have up to 100 characters';
	// 		}
	// 	} else {
	// 		error = 'Category Code can only be from 100000 to 999999';
	// 	}

	// 	return false;
	// }

	// // Function to create tariff
	// async function createTariffMethod() {
	// isBusy = true;
	// 	let payload = {
	// 		tariffRate: selectedCategory.tariffRate,
	// 		tariffType: selectedCategory.tariffType,
	// 		rateUnit: selectedCategory.rateUnit,
	// 		effectiveDate: selectedCategory.effectiveDate,
	// 		expiryDate: selectedCategory.expiryDate,
	// 		preferentialTariff: selectedCategory.preferentialTariff,
	// 		importingCountryCode: selectedCategory.importingCountryCode,
	// 		exportingCountryCode: selectedCategory.exportingCountryCode,
	// 		productCategory: {
	// 			categoryCode: selectedCategory.productCategory.categoryCode,
	// 			categoryName: selectedCategory.productCategory.categoryName,
	// 			description: selectedCategory.productCategory.description,
	// 			isActive: selectedCategory.productCategory.isActive
	// 		}
	// 	};

	// 	try {
	// 		const result = await createTariff(payload);

	// 		success = 'Created Tariff id: ' + result.id;
	// 		close();
	// 		fetchTariffs();
	// 		error = '';
	// 	} catch (err) {
	// 		error = err instanceof Error ? err.message : 'Creating tariff failed. Please try again.';
	// 		console.error('Creating tariff error:', err);
	// 	} finally{
	// isBusy = false;
	// }
	// }

	// // Function to edit tariff
	// async function editTariffMethod() {
	// isBusy = true;
	// 	let payload = {
	// 		id: selectedCategory.id,
	// 		tariffRate: selectedCategory.tariffRate,
	// 		tariffType: selectedCategory.tariffType,
	// 		rateUnit: selectedCategory.rateUnit,
	// 		effectiveDate: selectedCategory.effectiveDate,
	// 		expiryDate: selectedCategory.expiryDate,
	// 		preferentialTariff: selectedCategory.preferentialTariff,
	// 		importingCountryCode: selectedCategory.importingCountryCode,
	// 		exportingCountryCode: selectedCategory.exportingCountryCode,
	// 		productCategory: {
	// 			id: selectedCategory.productCategory.id,
	// 			categoryCode: selectedCategory.productCategory.categoryCode,
	// 			categoryName: selectedCategory.productCategory.categoryName,
	// 			description: selectedCategory.productCategory.description,
	// 			isActive: selectedCategory.productCategory.isActive
	// 		}
	// 	};

	// 	try {
	// 		const result = await editTariff(payload);

	// 		success = result.message;
	// 		close();
	// 		fetchTariffs();
	// 		error = '';
	// 	} catch (err) {
	// 		error = err instanceof Error ? err.message : 'Editing tariff failed. Please try again.';
	// 		console.error('Editing tariff error:', err);
	// 	} finally {
	// isBusy = false;
	// }
	// }

	// // Function to edit tariff
	// async function deleteTariffMethod(id: number) {
	// isBusy = true;
	// 	try {
	// 		const result = await deleteSpecificTariff(id);

	// 		success = result.message;
	// 		fetchTariffs();
	// 		error = '';
	// 	} catch (err) {
	// 		error = err instanceof Error ? err.message : 'Deleting tariff failed. Please try again.';
	// 		console.error('Deleting tariff error:', err);
	// 	}finally{
	// isBusy = false;
	// }
	// }

	// Function used to close the popup and reset the tariff value
	function close() {
		edit = false;
		createCategoryBoolean = false;
		view = false;
		selectedCategory = blankCategory();
		// fetchTariffs();
	}

	// Function that will return a date time in a readable format
	function readableDateTime(datetime) {
		const date = new Date(datetime);
		return date.toLocaleString();
	}

	// Restrict CategoryKey to only contain header values (ProductCategory)
	type CategoryKey = keyof ProductCategory;
	let sortKey: CategoryKey | null = null;
	let sortAsc = true;

	function sortBy(key: CategoryKey) {
		if (sortKey === key) {
			sortAsc = !sortAsc;
		} else {
			sortKey = key;
			sortAsc = true;
		}
	}

	// Need to give a new array
	$: sortedCategories =
		// If not sorted then use default data, else sort
		sortKey === null
			? allCategory
			: [...allCategory].sort((a, b) => {
					const key = sortKey as CategoryKey;
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
	<table class="table-zebra static table">
		<thead class="bg-base-300 text-base font-semibold">
			<tr>
				<th on:click={() => sortBy('id')} class="cursor-pointer"
					>Product Category Id {sortKey === 'id' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('categoryCode')} class="cursor-pointer"
					>HSCode {sortKey === 'categoryCode' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('categoryName')} class="cursor-pointer"
					>Name {sortKey === 'categoryName' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th on:click={() => sortBy('isActive')} class="cursor-pointer"
					>Is Active? {sortKey === 'isActive' ? (sortAsc ? '▲' : '▼') : ''}</th
				>
				<th></th>
			</tr>
		</thead>
		<tbody>
			{#each sortedCategories as line}
				<tr>
					<td>{line.id}</td>
					<td>{line.categoryCode}</td>
					<td>{line.categoryName}</td>
					<td>{line.isActive ? 'Yes' : 'No'}</td>
					<td class="p-0">
						<div class="dropdown dropdown-end static">
							<button class=" btn btn-ghost btn-circle text-xl">⋮</button>
							<ul class="menu menu-sm dropdown-content bg-base-100 rounded-box w-40 p-2 shadow">
								<li>
									<button
										class="text-sm"
										on:click={() => {
											selectedCategory = line;
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
											selectedCategory = line;
											edit = true;
											success = '';
											error = '';
										}}>Edit</button
									>
								</li>
								<li>
									<button class="text-error text-sm font-semibold">Delete</button>
									<!-- <button
										class="text-error text-sm font-semibold"
										on:click={deleteTariffMethod(line.id)}>Delete</button
									> -->
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
{#if view || edit || createCategoryBoolean}
	<div class="modal modal-open">
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
				{createCategoryBoolean ? 'Create' : edit ? 'Edit' : 'View'} Product Category
			</h3>
			{#if edit || createCategoryBoolean}
				<form class="grid grid-cols-1 gap-4">
					<!-- <form class="grid grid-cols-1 gap-4" on:submit|preventDefault={submitTariff}> -->
					<div>
						<label class="label" for="product_category_id">
							<span class="label-text font-semibold">Product Category ID</span>
						</label>
						<input
							type="text"
							id="product_category_id"
							bind:value={selectedCategory.id}
							class="input input-bordered w-full"
							disabled
						/>
					</div>

					<div>
						<label class="label" for="product_category_code">
							<span class="label-text font-semibold">HSCode</span>
						</label>
						<input
							type="text"
							id="product_category_code"
							bind:value={selectedCategory.categoryCode}
							class="input input-bordered w-full"
						/>
					</div>

					<div>
						<label class="label" for="product_category_name">
							<span class="label-text font-semibold">Name</span>
						</label>
						<input
							type="text"
							id="product_category_name"
							bind:value={selectedCategory.categoryName}
							class="input input-bordered w-full"
						/>
					</div>

					<div>
						<label class="label" for="product_category_description">
							<span class="label-text font-semibold">Description</span>
						</label>
						<textarea
							id="product_category_description"
							bind:value={selectedCategory.description}
							class="textarea textarea-bordered w-full"
						></textarea>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="product_category_active">
								<span class="label-text font-semibold">Is Active?</span>
							</label>
							<select
								id="product_category_active"
								value={selectedCategory.isActive + ''}
								on:change={(e) =>
									(selectedCategory.isActive =
										(e.currentTarget as HTMLSelectElement).value === 'true')}
								class="select select-bordered w-full"
							>
								<option value="true">Yes</option>
								<option value="false">No</option>
							</select>
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
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</form>
			{:else}
				<!-- view -->
				<div class="grid grid-cols-1 gap-4">
					<div>
						<label class="label" for="product_category_id">
							<span class="label-text font-semibold">Product Category ID</span>
						</label>
						<p class="w-full">{selectedCategory.id}</p>
					</div>

					<div>
						<label class="label" for="product_category_code">
							<span class="label-text font-semibold">HSCode</span>
						</label>
						<p class="w-full">{selectedCategory.categoryCode}</p>
					</div>

					<div>
						<label class="label" for="product_category_name">
							<span class="label-text font-semibold">Name</span>
						</label>
						<p class="w-full">{selectedCategory.categoryName}</p>
					</div>

					<div>
						<label class="label" for="product_category_description">
							<span class="label-text font-semibold">Description</span>
						</label>
						<p class="w-full">{selectedCategory.description}</p>
					</div>

					<div class="grid grid-cols-2 gap-4">
						<div>
							<label class="label" for="product_category_active">
								<span class="label-text font-semibold">Is Active?</span>
							</label>
							<p class="w-full">{selectedCategory.isActive ? 'Yes' : 'No'}</p>
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
