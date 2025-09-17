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
