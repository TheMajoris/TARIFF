// Calculate tariff cost
export async function calculateTariffCost({ product, exportFrom, importTo, calculationDate, goodsValue }) {
  try {
    // 🚨 Replace with real API when ready
    // const res = await fetch("/api/calculate-cost", {
    //   method: "POST",
    //   headers: { "Content-Type": "application/json" },
    //   body: JSON.stringify({ product, exportFrom, importTo, calculationDate, goodsValue })
    // });
    // if (!res.ok) throw new Error("Failed to calculate");
    // return await res.json();

    // Dummy calculation for now
    const baseValue = parseFloat(goodsValue).toFixed(2);
    const tariff = (35).toFixed(2);
    const customsDuty = (20).toFixed(2);
    const totalCost = (parseFloat(baseValue) + 35 + 20).toFixed(2);

    return {
      baseValue,
      tariff,
      customsDuty,
      totalCost
    };
  } catch (err) {
    console.error("calculateTariffCost error:", err);
    return null;
  }
}


// Get all tariff
export async function getAllTariff() {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Getting all tariffs at:', `${API_BASE_URL}/tariff-rate`);

    const res = await fetch(`${API_BASE_URL}/tariff-rate`, {
      method: "GET",
      credentials: "include",
      headers: {
        "Accept": "application/json"
      }
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Getting all tariffs failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    console.log('Getting all tariffs result:', result);

    return result;

  } catch (err) {
    console.error("getAllTariff error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}

// Create tariff
/**
 * Create a new tariff rate.
 *
 * Body must conform to CreateTariffRateDto with nested CreateProductCategoriesDto:
 * - tariffRate (number) required: decimal 0.0 - 999.9999
 * - tariffType (string) required: max 50 chars
 * - rateUnit (string) optional: max 20 chars
 * - effectiveDate (string, ISO yyyy-mm-dd) required
 * - expiryDate (string, ISO yyyy-mm-dd) optional
 * - preferentialTariff (boolean) optional
 * - importingCountryCode (string) required
 * - exportingCountryCode (string) required
 * - productCategory (object) required:
 *   - categoryCode (number) required: 2-6 digits (10..999999)
 *   - categoryName (string) required: max 100 chars
 *   - description (string) optional: max 500 chars
 *
 * @param {Object} payload
 * @param {number} payload.tariffRate
 * @param {string} payload.tariffType
 * @param {string=} payload.rateUnit
 * @param {string} payload.effectiveDate
 * @param {string=} payload.expiryDate
 * @param {boolean=} payload.preferentialTariff
 * @param {string} payload.importingCountryCode
 * @param {string} payload.exportingCountryCode
 * @param {{categoryCode:number, categoryName:string, description?:string}} payload.productCategory
 */
export async function createTariff(payload) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Creating tariff at:', `${API_BASE_URL}/tariff-rate/create`);

    const res = await fetch(`${API_BASE_URL}/tariff-rate`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Accept": "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Creating tariff failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    console.log('Creating tariff result:', result);
    return result;
  } catch (err) {
    console.error("createTariff error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}

// Delete specific tariff
/**
 * @param {number} id
 */
export async function deleteSpecificTariff(id) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Deleting tariff at:', `${API_BASE_URL}/tariff-rate/` + id);

    const res = await fetch(`${API_BASE_URL}/tariff-rate/` + id, {
      method: "DELETE",
      credentials: "include",
      headers: {
        "Accept": "application/json"
      }
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Deleting tariff failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    console.log('Deleting tariff result:', result);

    return result;

  } catch (err) {
    console.error("deleteSpecificTariff error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}
