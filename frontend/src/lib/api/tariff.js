// Calculate tariff cost
export async function calculateTariffCost({ hsCode, exportFrom, importTo, calculationDate, goodsValue }) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Calculating tariff cost:', { hsCode, exportFrom, importTo, calculationDate, goodsValue });

    const hsCodeInteger = parseInt(hsCode);
    
    const requestBody = {
      importingCountryId: parseInt(importTo),
      exportingCountryId: parseInt(exportFrom),
      hsCode: hsCodeInteger,
      initialPrice: parseFloat(goodsValue),
      date: calculationDate
    };

    console.log('Request body:', requestBody);

    const res = await fetch(`${API_BASE_URL}/tariff-rate/calculation`, {
      method: "POST",
      credentials: "include",
      headers: { 
        "Content-Type": "application/json"
      },
      body: JSON.stringify(requestBody)
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error('API Error:', res.status, errorText);
      
      // Handle specific error cases
      if (res.status === 404) {
        throw new Error('No tariff data found for the specified countries and product');
      } else if (res.status === 400) {
        throw new Error('Invalid request parameters');
      } else {
        throw new Error(`Failed to calculate tariff: ${res.status} ${res.statusText}`);
      }
    }

    const result = await res.json();
    console.log('API Response:', result);

    // Map the response to match frontend expectations
    const tariffRate = result.tariffRate ? parseFloat(result.tariffRate) : 0;
    const tariffCost = result.tariffCost ? parseFloat(result.tariffCost) : 0;
    
    // Check if this is a "no data found" case (tariff rate is -1)
    if (tariffRate === -1) {
      return {
        baseValue: parseFloat(goodsValue).toFixed(2),
        tariffRate: "-1.00",
        tariffCost: "0.00",
        totalCost: parseFloat(goodsValue).toFixed(2)
      };
    }
    
    return {
      baseValue: parseFloat(goodsValue).toFixed(2),
      tariffRate: tariffRate.toFixed(2),
      tariffCost: tariffCost.toFixed(2),
      totalCost: result.finalPrice ? parseFloat(result.finalPrice).toFixed(2) : goodsValue
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
    // console.log('Getting all tariffs at:', `${API_BASE_URL}/tariff-rate`);

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
    // console.log('Getting all tariffs result:', result);

    return result;

  } catch (err) {
    console.error("getAllTariff error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}

/**
 * Create a new tariff rate.
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
 * @param {string} payload.hsCode
 */
export async function createTariff(payload) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    // console.log('Creating tariff at:', `${API_BASE_URL}/tariff-rate`);

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
    // console.log('Creating tariff result:', result);
    return result;
  } catch (err) {
    console.error("createTariff error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}

/**
 * Edit a tariff rate.
 *
 * @param {Object} payload
 * @param {number} payload.id
 * @param {number} payload.tariffRate
 * @param {string} payload.tariffType
 * @param {string=} payload.rateUnit
 * @param {string} payload.effectiveDate
 * @param {string=} payload.expiryDate
 * @param {boolean=} payload.preferentialTariff
 * @param {string} payload.importingCountryCode
 * @param {string} payload.exportingCountryCode
 * @param {{id:number, categoryCode:string, categoryName:string, description?:string}} payload.productCategory
 */
export async function editTariff(payload) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    // console.log('Creating tariff at:', `${API_BASE_URL}/tariff-rate/` + payload.id);

    const res = await fetch(`${API_BASE_URL}/tariff-rate/` + payload.id, {
      method: "PUT",
      credentials: "include",
      headers: {
        "Accept": "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Updating tariff failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    // console.log('Updating tariff result:', result);
    return result;
  } catch (err) {
    console.error("editTariff error:", err);
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
    // console.log('Deleting tariff at:', `${API_BASE_URL}/tariff-rate/` + id);

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
    // console.log('Deleting tariff result:', result);

    return result;

  } catch (err) {
    console.error("deleteSpecificTariff error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}
