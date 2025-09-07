<script>
	let selectedTariff = null;
	function blankTariff() {
		selectedTariff = {
			tariff_id: '',
			importing_country: '',
			exporting_country: '',
			hs_code: '',
			tariff_rate: '',
			tariff_type: '',
			rate_unit: '',
			effective_date: '',
			expiry_date: '',
			preferential_tariff: '',
			created_at: '',
			updated_at: ''
		};
	}
	blankTariff();

	let edit = false;
	let create = false;
	// Dummy news
	let tariff = [
		{
			tariff_id: 'TAR001',
			importing_country: 'United States',
			exporting_country: 'China',
			hs_code: '847130',
			tariff_rate: '10',
			tariff_type: 'Ad Valorem',
			rate_unit: '%',
			effective_date: '2023-01-01',
			expiry_date: '2025-12-31',
			preferential_tariff: 'No',
			created_at: '2023-01-01T10:00:00Z',
			updated_at: '2023-06-01T15:30:00Z'
		},
		{
			tariff_id: 'TAR002',
			importing_country: 'Germany',
			exporting_country: 'Brazil',
			hs_code: '020130',
			tariff_rate: '5',
			tariff_type: 'Ad Valorem',
			rate_unit: '%',
			effective_date: '2022-07-15',
			expiry_date: '2026-07-14',
			preferential_tariff: 'Yes',
			created_at: '2022-07-15T08:00:00Z',
			updated_at: '2024-01-20T11:45:00Z'
		},
		{
			tariff_id: 'TAR003',
			importing_country: 'India',
			exporting_country: 'Australia',
			hs_code: '100630',
			tariff_rate: '15',
			tariff_type: 'Specific',
			rate_unit: 'USD/ton',
			effective_date: '2021-04-01',
			expiry_date: '2025-03-31',
			preferential_tariff: 'No',
			created_at: '2021-04-01T12:00:00Z',
			updated_at: '2023-09-10T09:20:00Z'
		},
		{
			tariff_id: 'TAR004',
			importing_country: 'Japan',
			exporting_country: 'United States',
			hs_code: '870323',
			tariff_rate: '7.5',
			tariff_type: 'Ad Valorem',
			rate_unit: '%',
			effective_date: '2023-05-01',
			expiry_date: '2027-04-30',
			preferential_tariff: 'Yes',
			created_at: '2023-05-01T14:10:00Z',
			updated_at: '2024-02-18T17:40:00Z'
		},
		{
			tariff_id: 'TAR005',
			importing_country: 'Canada',
			exporting_country: 'Mexico',
			hs_code: '220421',
			tariff_rate: '0',
			tariff_type: 'Preferential',
			rate_unit: '%',
			effective_date: '2020-07-01',
			expiry_date: '2030-06-30',
			preferential_tariff: 'Yes',
			created_at: '2020-07-01T09:30:00Z',
			updated_at: '2023-11-22T10:50:00Z'
		}
	];

	// function to validate & submit tariff
	function submitTariff() {
		if (
			selectedTariff.tariff_id &&
			selectedTariff.importing_country &&
			selectedTariff.exporting_country &&
			selectedTariff.hs_code &&
			selectedTariff.tariff_rate &&
			selectedTariff.tariff_type &&
			selectedTariff.rate_unit &&
			selectedTariff.effective_date &&
			selectedTariff.expiry_date &&
			selectedTariff.preferential_tariff
		) {
			if (create) {
				createTariff();
			} else if (edit) {
				editTariff();
			}
		} else {
			alert('Please fill in all fields.');
		}
	}

	// function to create tariff
	function createTariff() {
		alert('Create');
	}

	// function to edit tariff
	function editTariff() {
		alert('edit');
	}

	// function to edit tariff
	function deleteTariff() {
		alert('delete');
	}
</script>

<div class="space-y-6 p-6">
	<!-- Page Title -->
	<h1 class="text-primary text-2xl font-semibold">Admin</h1>

	<!-- Two-column layout -->
	<div class="grid grid-cols-1">
		<!-- Tariffs Card -->
		<div class="card bg-base-100 p-6 shadow-md">
			<div class="flex items-center justify-between py-6">
				<h2 class="mb-1 text-lg font-semibold">Tariffs</h2>
				<button class="btn btn-primary" on:click={() => (create = true)}>Create</button>
			</div>
			<div class="overflow-x-auto">
				<table class="table-zebra table">
					<thead class="bg-base-300 text-base font-semibold">
						<tr>
							<th>Tariff ID</th>
							<th>Importing Country</th>
							<th>Exporting Country</th>
							<th>HS Code</th>
							<th>Rate</th>
							<th>Effective Date</th>
							<th>Expiry Date</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<!-- <ul class="space-y-4"> -->
						{#each tariff as line}
							<tr>
								<td>{line.tariff_id}</td>
								<td>{line.importing_country}</td>
								<td>{line.exporting_country}</td>
								<td>{line.hs_code}</td>
								<td>{line.tariff_rate}{line.rate_unit}</td>
								<td>{line.effective_date}</td>
								<td>{line.expiry_date}</td>
								<td
									class="cursor-pointer"
									on:click={() => {
										selectedTariff = line;
										edit = true;
									}}>✎</td
								>
							</tr>
						{/each}
						<!-- </ul> -->
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<!-- Modal -->
{#if edit || create}
	<div class="modal modal-open">
		<div class="modal-box max-w-2xl">
			<h3 class="mb-4 text-lg font-bold">{create ? 'Create' : 'Edit'} Tariff</h3>
			<form class="grid grid-cols-1 gap-4" on:submit|preventDefault={submitTariff}>
				<div>
					<label class="label" for="tariff_id">
						<span class="label-text font-semibold">Tariff ID</span>
					</label>
					<input
						type="text"
						id="tariff_id"
						bind:value={selectedTariff.tariff_id}
						class="input input-bordered w-full"
					/>
				</div>

				<div>
					<label class="label" for="importing_country">
						<span class="label-text font-semibold">Importing Country</span>
					</label>
					<input
						type="text"
						id="importing_country"
						bind:value={selectedTariff.importing_country}
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
						bind:value={selectedTariff.exporting_country}
						class="input input-bordered w-full"
					/>
				</div>

				<div>
					<label class="label" for="hs_code">
						<span class="label-text font-semibold">HS Code</span>
					</label>
					<input
						type="text"
						id="hs_code"
						bind:value={selectedTariff.hs_code}
						class="input input-bordered w-full"
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
							bind:value={selectedTariff.tariff_rate}
							class="input input-bordered w-full"
						/>
					</div>
					<div>
						<label class="label" for="rate_unit">
							<span class="label-text font-semibold">Rate Unit</span>
						</label>
						<select
							id="rate_unit"
							bind:value={selectedTariff.rate_unit}
							class="select select-bordered w-full"
						>
							<option>%</option>
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
						bind:value={selectedTariff.tariff_type}
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
							bind:value={selectedTariff.effective_date}
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
							bind:value={selectedTariff.expiry_date}
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
						bind:value={selectedTariff.preferential_tariff}
						class="select select-bordered w-full"
					>
						<option>Yes</option>
						<option>No</option>
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
							bind:value={selectedTariff.created_at}
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
							bind:value={selectedTariff.updated_at}
							class="input input-bordered w-full"
							disabled
						/>
					</div>
				</div>

				<div class="modal-action">
					<button
						type="button"
						class="btn"
						on:click={() => {
							edit = false;
							create = false;
							blankTariff();
						}}>Cancel</button
					>
					<button type="submit" class="btn btn-primary">Submit</button>
					<button
						type="button"
						class="btn btn-error"
						on:click={() => {
							deleteTariff();
						}}>Delete</button
					>
				</div>
			</form>
		</div>
	</div>
{/if}
