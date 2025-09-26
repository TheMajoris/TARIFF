// Calculate tariff cost
export async function calculateTariffCost({ hsCode, exportFrom, importTo, calculationDate, goodsValue }) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Calculating tariff cost:', { hsCode, exportFrom, importTo, calculationDate, goodsValue });

    const hsCodeInteger = parseInt(hsCode.replace('.', ''));
    
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
