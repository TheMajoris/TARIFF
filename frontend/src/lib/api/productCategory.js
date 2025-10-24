// Get all product categories
export async function getAllProductCategories() {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

    const res = await fetch(`${API_BASE_URL}/product-categories/all`, {
      method: "GET",
      credentials: "include",
      headers: {
        "Accept": "application/json"
      }
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Getting all product categories failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    return result;

  } catch (err) {
    console.error("getAllProductCategories error:", err);
    throw err;
  }
}

/**
 * Create a new product category
 * @param {Object} payload
 * @param {number} payload.categoryCode - 6-digit HS code
 * @param {string} payload.categoryName
 * @param {string} payload.description
 * @param {boolean} payload.isActive
 */
export async function createProductCategory(payload) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

    const res = await fetch(`${API_BASE_URL}/product-categories`, {
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
      throw new Error(errorData.message || `Creating product category failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    return result;
  } catch (err) {
    console.error("createProductCategory error:", err);
    throw err;
  }
}

/**
 * Edit an existing product category
 * @param {number} id
 * @param {Object} payload
 * @param {number} payload.categoryCode - 6-digit HS code
 * @param {string} payload.categoryName
 * @param {string} payload.description
 * @param {boolean} payload.isActive
 */
export async function editProductCategory(id, payload) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

    const res = await fetch(`${API_BASE_URL}/product-categories/${id}`, {
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
      throw new Error(errorData.message || `Updating product category failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    return result;
  } catch (err) {
    console.error("editProductCategory error:", err);
    throw err;
  }
}

/**
 * Delete a product category
 * @param {number} id
 * @param {boolean} forceDelete - If true, cascade delete related tariff rates
 */
export async function deleteProductCategory(id, forceDelete = false) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

    const url = new URL(`${API_BASE_URL}/product-categories/${id}`);
    if (forceDelete) {
      url.searchParams.append('forceDelete', 'true');
    }

    const res = await fetch(url.toString(), {
      method: "DELETE",
      credentials: "include",
      headers: {
        "Accept": "application/json"
      }
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Deleting product category failed: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    return result;
  } catch (err) {
    console.error("deleteProductCategory error:", err);
    throw err;
  }
}