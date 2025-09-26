// Calculate tariff cost
export async function calculateTariffCost({ hsCode, exportFrom, importTo, calculationDate, goodsValue }) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Calculating tariff cost:', { hsCode, exportFrom, importTo, calculationDate, goodsValue });

    // Convert HS Code from string format (e.g., "8501.10.10") to integer format (e.g., 85011010)
    const hsCodeInteger = parseInt(hsCode.replace(/\./g, ''));
    
    const requestBody = {
      importingCountryId: parseInt(importTo),
      exportingCountryId: parseInt(exportFrom),
      hsCode: hsCodeInteger,
      initialPrice: parseFloat(goodsValue),
      date: calculationDate
    };

    console.log('Request body:', requestBody);

    const res = await fetch(`${API_BASE_URL}/tariff-rate/calculate`, {
      method: "POST",
      headers: { 
        "Content-Type": "application/json"
      },
      body: JSON.stringify(requestBody)
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error('API Error:', res.status, errorText);
      throw new Error(`Failed to calculate tariff: ${res.status} ${res.statusText}`);
    }

    const result = await res.json();
    console.log('API Response:', result);

    // Map the response to match frontend expectations
    return {
      baseValue: parseFloat(goodsValue).toFixed(2),
      tariffRate: result.tariffRate ? parseFloat(result.tariffRate).toFixed(2) : "0.00",
      tariff: result.tariffCost ? parseFloat(result.tariffCost).toFixed(2) : "0.00",
      totalCost: result.finalPrice ? parseFloat(result.finalPrice).toFixed(2) : goodsValue
    };
  } catch (err) {
    console.error("calculateTariffCost error:", err);
    return null;
  }
}
