<script lang="ts">
	import { beforeNavigate } from '$app/navigation';
	import { onMount } from 'svelte';
	import {
		createProductCategory,
		deleteSpecificProductCategory,
		editProductCategory,
		forceDeleteSpecificProductCategory,
		getAllProductCategories,
		getProductCategoriesPage
	} from '$lib/api/productCategory';
	import { page } from '$app/state';
	import Alert from '$lib/components/Alert.svelte';

	let success = '';
	let error = '';

	let view = false;
	let edit = false;
	export let createCategoryBoolean = false;
	export let isBusy = false; // page-level busy indicator for create/edit flows
	let confirmDelete = -1;

	// variables for pagination
	let pageNo = 0;
	let totalPage = 0;
	let totalProductCategories = 0;
	let size = 5;

	type ProductCategory = {
		categoryCode: string;
		categoryName: string;
		description: string;
		id: number;
		isActive: boolean;
	};

	// test data
	let allCategory: ProductCategory[] = [];
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

	async function fetchProductCategories(sortKey: string, sortAsc: boolean) {
		isBusy = true;
		try {
			let sortDirection;
			if (sortAsc) {
				sortDirection = 'ascending';
			} else {
				sortDirection = 'descending';
			}

			const result = await getProductCategoriesPage(pageNo, size, sortKey, sortDirection);
			allCategory = result.content;
			totalPage = result.totalPages;
			totalProductCategories = result.totalElements;

			if (pageNo + 1 > totalPage) {
				pageNo = totalPage - 1;
				fetchProductCategories(sortKey, sortAsc);
			}
		} catch (err) {
			console.error('Getting all product categories:', err);
			error =
				err instanceof Error ? err.message : 'Viewing product categories failed. Please try again.';
		} finally {
			isBusy = false;
		}
	}

	// Update when any page -> admin
	beforeNavigate(() => {
		fetchProductCategories(sortKey, sortAsc);
	});

	// Update on page load/refresh
	onMount(() => {
		fetchProductCategories(sortKey, sortAsc);
	});

	// Function to validate & submit Category
	function submitProductCategory() {
		if (CategoryValidation()) {
			if (createCategoryBoolean) {
				createProductCategoryMethod();
			} else if (edit) {
				editProductCategoryMethod();
			}
		}
	}

	// Function to validate Category
	function CategoryValidation() {
		if (selectedCategory.categoryCode != null && /^\d{6}$/.test(selectedCategory.categoryCode)) {
			if (selectedCategory.categoryName != '' && selectedCategory.categoryName.length <= 100) {
				if (selectedCategory.description.length <= 500) {
					return true;
				} else {
					error = 'Category Description can only have up to 500 characters';
				}
			} else {
				error = 'Category Name can only have up to 100 characters';
			}
		} else {
			error = 'Category Code can only be from 100000 to 999999';
		}

		return false;
	}

	// Function to create product category
	async function createProductCategoryMethod() {
		isBusy = true;
		let payload = {
			categoryCode: selectedCategory.categoryCode,
			categoryName: selectedCategory.categoryName,
			description: selectedCategory.description,
			isActive: selectedCategory.isActive
		};

		try {
			const result = await createProductCategory(payload);

			success = 'Created product category id: ' + result.data.id;
			close();
			fetchProductCategories(sortKey, sortAsc);
			error = '';
		} catch (err) {
			error =
				err instanceof Error ? err.message : 'Creating product category failed. Please try again.';
			console.error('Creating product category error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function to edit product category
	async function editProductCategoryMethod() {
		isBusy = true;
		let payload = {
			id: selectedCategory.id,
			categoryCode: selectedCategory.categoryCode,
			categoryName: selectedCategory.categoryName,
			description: selectedCategory.description,
			isActive: selectedCategory.isActive
		};

		try {
			const result = await editProductCategory(payload);

			success = result.message;
			close();
			fetchProductCategories(sortKey, sortAsc);
			error = '';
		} catch (err) {
			error =
				err instanceof Error ? err.message : 'Editing product category failed. Please try again.';
			console.error('Editing product category error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function to delete product category
	async function deleteProductCategoryMethod(id: number) {
		isBusy = true;
		try {
			let result;

			// force delete
			if (confirmDelete == id) {
				result = await forceDeleteSpecificProductCategory(id);
			} else {
				result = await deleteSpecificProductCategory(id);
			}

			success = result.message;
			fetchProductCategories(sortKey, sortAsc);
			error = '';
		} catch (err) {
			// request for force delete (there are tariffs using that HSCode)
			console.log(err);
			if (
				err &&
				err instanceof Error &&
				err.message.endsWith('Set request params flag to true to cascade delete')
			) {
				error =
					err.message.split(".")[0] +
					'. Please click delete again to confirm deletion (inclusive of tariffs using it)';
				console.log(error);
				confirmDelete = id;
			} else {
				error =
					err instanceof Error
						? err.message
						: 'Deleting product category failed. Please try again.';
			}
			console.error('Deleting product category error:', err);
		} finally {
			isBusy = false;
		}
	}

	// Function used to close the popup and reset the product categories value
	function close() {
		edit = false;
		createCategoryBoolean = false;
		view = false;
		selectedCategory = blankCategory();
		fetchProductCategories(sortKey, sortAsc);
	}

	// Restrict CategoryKey to only contain header values (ProductCategory)
	type CategoryKey = keyof ProductCategory;
	let sortKey: CategoryKey = 'id';
	let sortAsc = true;

	function sortBy(key: CategoryKey) {
		if (sortKey === key) {
			sortAsc = !sortAsc;
		} else {
			sortKey = key;
			sortAsc = true;
		}

		fetchProductCategories(sortKey, sortAsc);
	}

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
			{#each allCategory as line}
				<tr>
					<td>{line.id}</td>
					<td>{line.categoryCode}</td>
					<td>{line.categoryName}</td>
					<td>{line.isActive ? 'Yes' : 'No'}</td>
					<td class="p-0">
						<div class="dropdown dropdown-end static">
							<button class=" btn btn-circle btn-ghost text-xl">⋮</button>
							<ul class="dropdown-content menu menu-sm rounded-box bg-base-100 w-40 p-2 shadow">
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
									<button
										class="text-error text-sm font-semibold"
										on:click={() => deleteProductCategoryMethod(line.id)}>Delete</button
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
			Showing {size * pageNo + 1}-{size * pageNo + allCategory.length} of
			{totalProductCategories} articles
		</div>
		<div class="flex gap-2">
			<button
				class="btn btn-sm btn-outline"
				disabled={pageNo === 0}
				on:click={() => {
					pageNo--;
					fetchProductCategories(sortKey, sortAsc);
				}}
			>
				Previous
			</button>
			<button
				class="btn btn-sm btn-outline"
				disabled={pageNo + 1 >= totalPage}
				on:click={() => {
					pageNo++;
					fetchProductCategories(sortKey, sortAsc);
				}}
			>
				Next
			</button>
		</div>
	</div>
{/if}

<!-- Modal -->
{#if view || edit || createCategoryBoolean}
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
				{createCategoryBoolean ? 'Create' : edit ? 'Edit' : 'View'} Product Category
			</h3>
			{#if edit || createCategoryBoolean}
				<form class="grid grid-cols-1 gap-4" on:submit|preventDefault={submitProductCategory}>
					<div>
						<label class="label" for="product_category_id">
							<span class="label-text font-semibold">Product Category ID</span>
						</label>
						<input
							type="text"
							id="product_category_id"
							bind:value={selectedCategory.id}
							class="input-bordered input w-full"
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
							class="input-bordered input w-full"
							disabled={edit && !createCategoryBoolean}
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
							class="input-bordered input w-full"
						/>
					</div>

					<div>
						<label class="label" for="product_category_description">
							<span class="label-text font-semibold">Description</span>
						</label>
						<textarea
							id="product_category_description"
							bind:value={selectedCategory.description}
							class="textarea-bordered textarea w-full"
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
								class="select-bordered select w-full"
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
