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
    throw err; // Re-throw to let the calling code handle the error
  }
}

/**
 * Create a new product category.
 *
 * @param {Object} payload
 * @param {string} payload.categoryCode
 * @param {string} payload.categoryName
 * @param {string=} payload.description
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
    // console.log('Creating tariff result:', result);
    return result;
  } catch (err) {
    console.error("createProductCategory error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}

/**
 * Edit a product category.
 *
 * @param {Object} payload
 * @param {number} payload.id
 * @param {string} payload.categoryCode
 * @param {string} payload.categoryName
 * @param {string=} payload.description
 * @param {boolean} payload.isActive
 */
export async function editProductCategory(payload) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

    const res = await fetch(`${API_BASE_URL}/product-categories/` + payload.id, {
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
    throw err; // Re-throw to let the calling code handle the error
  }
}

// Delete specific product category
/**
 * @param {number} id
 */
export async function deleteSpecificProductCategory(id) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

    const res = await fetch(`${API_BASE_URL}/product-categories/` + id, {
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
    console.error("deleteSpecificProductCategory error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}

// FORCE Delete specific product categories and related tariffs
/**
 * @param {number} id
 */
export async function forceDeleteSpecificProductCategory(id) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

    const params = new URLSearchParams({ forceDelete: "true" });

    const res = await fetch(`${API_BASE_URL}/product-categories/` + id + "?" + params.toString(), {
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
    console.error("deleteSpecificProductCategory error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}