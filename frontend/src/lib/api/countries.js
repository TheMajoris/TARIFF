// Fetch country list
export async function fetchCountries() {
    try {
        const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ||
            "http://localhost:8081/api/v1";
        console.log("Fetching countries from:", `${API_BASE_URL}/countries`);

        const res = await fetch(`${API_BASE_URL}/countries`);

        if (!res.ok) {
            throw new Error(
                `Failed to fetch countries: ${res.status} ${res.statusText}`,
            );
        }

        const countries = await res.json();
        console.log("Raw countries data:", countries);

        // Convert to the format expected by the frontend
        const mappedCountries = countries.map((country) => ({
            id: country.id,
            name: country.countryName,
            code: country.countryCode,
        }));

        console.log("Mapped countries:", mappedCountries);
        return mappedCountries;
    } catch (err) {
        console.error("fetchCountries error:", err);
        return []; // Return empty array if error occurs
    }
}
