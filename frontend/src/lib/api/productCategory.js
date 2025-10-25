// Get all tariff
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