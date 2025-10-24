<script lang="ts">
	import { beforeNavigate } from '$app/navigation';
	import { createProductCategory, deleteProductCategory, editProductCategory, getAllProductCategories } from '$lib/api/productCategory.js';
	import { onMount } from 'svelte';

	let success = '';
	let error = '';

	let view = false;
	let edit = false;
	export let createCategoryBoolean = false;
	export let isBusy = false;

	type ProductCategory = {
		categoryCode: number;
		categoryName: string;
		description: string;
		id: number;
		isActive: boolean;
	};

	let allCategory: ProductCategory[] = [];
	let selectedCategory: ProductCategory = blankCategory();
	
	function blankCategory() {
		return {
			categoryCode: 0,
			categoryName: '',
			description: '',
			id: 0,
			isActive: true
		};
	}

	async function fetchProductCategories() {
		try {
			const result = await getAllProductCategories();
			allCategory = result;
		} catch (err) {
			console.error('Getting all product categories error:', err);
			error = err instanceof Error ? err.message : 'Viewing product categories failed. Please try again.';
		}
	}

	// Update when navigating to admin page
	beforeNavigate(() => {
		fetchProductCategories();
	});

	// Update on page load/refresh
	onMount(() => {
		fetchProductCategories();
	});

	// Function to validate & submit category
	function submitCategory() {
		if (validateCategory()) {
			if (createCategoryBoolean) {
				createCategoryMethod();
			} else if (edit) {
				editCategoryMethod();
			}
		}
	}

	// Function to validate Category
	function validateCategory() {
		// Reset error
		error = '';

		// Validate category code (6 digits)
		if (!selectedCategory.categoryCode || !/^\d{6}$/.test(selectedCategory.categoryCode.toString())) {
			error = '⚠️ Category Code must be a 6-digit number (e.g., 851713)';
			return false;
		}

		// Validate category name
		if (!selectedCategory.categoryName || selectedCategory.categoryName.trim() === '') {
			error = '⚠️ Category Name is required';
			return false;
		}

		if (selectedCategory.categoryName.length > 100) {
			error = '⚠️ Category Name cannot exceed 100 characters';
			return false;
		}

		// Validate description
		if (selectedCategory.description && selectedCategory.description.length > 500) {
			error = '⚠️ Description cannot exceed 500 characters';
			return false;
		}

		return true;
	}

	// Function to create category
	async function createCategoryMethod() {
		isBusy = true;
		let payload = {
			categoryCode: parseInt(selectedCategory.categoryCode.toString()),
			categoryName: selectedCategory.categoryName,
			description: selectedCategory.description,
			isActive: selectedCategory.isActive
		};

		try {
			const result = await createProductCategory(payload);

			success = 'Product Category created successfully! (HS Code: ' + result.data.categoryCode + ')';
			close();
			fetchProductCategories();
			error = '';
		} catch (err) {
			error = err instanceof Error ? err.message : '❌ Failed to create product category. Please check your data and try again.';
			console.error('Creating product category error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function to edit category
	async function editCategoryMethod() {
		isBusy = true;
		let payload = {
			categoryCode: parseInt(selectedCategory.categoryCode.toString()),
			categoryName: selectedCategory.categoryName,
			description: selectedCategory.description,
			isActive: selectedCategory.isActive
		};

		try {
			const result = await editProductCategory(selectedCategory.id, payload);

			success = result.message;
			close();
			fetchProductCategories();
			error = '';
		} catch (err) {
			error = err instanceof Error ? err.message : '❌ Failed to update product category. Please check your data and try again.';
			console.error('Editing product category error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function to delete category
	async function deleteCategoryMethod(id: number) {
		// Ask for confirmation
		if (!confirm('Are you sure you want to delete this product category? This may affect related tariff rates.')) {
			return;
		}

		isBusy = true;
		try {
			const result = await deleteProductCategory(id, false);
			console.log('Delete result:', result);

			success = '🗑️ ' + (result.message || 'Product category deleted successfully');
			fetchProductCategories();
			error = '';
		} catch (err) {
			const errorMsg = err instanceof Error ? err.message : '';
			
			// Check if error is about existing relations
			if (errorMsg.includes('related tariff rates')) {
				// Ask if they want to force delete
				if (confirm(errorMsg + '\n\nDo you want to delete the category and all related tariff rates?')) {
					try {
						const forceResult = await deleteProductCategory(id, true);
						success = '🗑️ ' + (forceResult.message || 'Product category and related tariff rates deleted successfully');
						fetchProductCategories();
						error = '';
					} catch (forceErr) {
						error = forceErr instanceof Error ? forceErr.message : '❌ Failed to delete product category. Please try again.';
						console.error('Force deleting product category error:', forceErr);
					}
				}
			} else {
				error = errorMsg || '❌ Failed to delete product category. Please try again.';
				console.error('Deleting product category error:', err);
			}
		} finally {
			isBusy = false;
		}
	}

	// Function used to close the popup and reset the category value
	function close() {
		edit = false;
		createCategoryBoolean = false;
		view = false;
		selectedCategory = blankCategory();
		fetchProductCategories();
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

<!-- Global Alerts -->
{#if error}
	<div class="alert alert-error mb-4">
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
	<div class="alert alert-success mb-4">
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
											selectedCategory = { ...line };
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
											selectedCategory = { ...line };
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
											deleteCategoryMethod(line.id);
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
				<form class="grid grid-cols-1 gap-4" on:submit|preventDefault={submitCategory}>
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
							<span class="label-text font-semibold">HSCode <span class="text-error">*</span></span>
						</label>
						<input
							type="number"
							id="product_category_code"
							bind:value={selectedCategory.categoryCode}
							class="input input-bordered w-full"
							placeholder="e.g., 851713"
							required
						/>
						<label class="label">
							<span class="label-text-alt">Must be a 6-digit number</span>
						</label>
					</div>

					<div>
						<label class="label" for="product_category_name">
							<span class="label-text font-semibold">Name <span class="text-error">*</span></span>
						</label>
						<input
							type="text"
							id="product_category_name"
							bind:value={selectedCategory.categoryName}
							class="input input-bordered w-full"
							placeholder="e.g., Smartphones"
							required
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
							placeholder="Optional description"
							rows="3"
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
						<button type="submit" class="btn btn-primary" disabled={isBusy}>
							{isBusy ? 'Processing...' : 'Submit'}
						</button>
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
						<p class="w-full">{selectedCategory.description || '(No description)'}</p>
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
