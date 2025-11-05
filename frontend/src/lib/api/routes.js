// Fetch alternative trade routes from backend
/**
 * @param {{
 *  exportingCountryCode: string,
 *  importingCountryCode: string,
 *  hsCode: number|string,
 *  initialPrice: number|string,
 *  quantity: number|string,
 *  date: string
 * }} params
 */
export async function getAlternativeRoutes(params) {
  const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

  // Normalize payload to backend contract
  const payload = {
    exportingCountryCode: String(params.exportingCountryCode).toUpperCase(),
    importingCountryCode: String(params.importingCountryCode).toUpperCase(),
    hsCode: typeof params.hsCode === 'string' ? parseInt(params.hsCode) : params.hsCode,
    initialPrice: typeof params.initialPrice === 'string' ? parseFloat(params.initialPrice) : params.initialPrice,
    quantity: typeof params.quantity === 'string' ? parseFloat(params.quantity) : params.quantity,
    date: params.date
  };

  const res = await fetch(`${API_BASE_URL}/route/alternative`, {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!res.ok) {
    let message = `Failed to fetch alternative routes: ${res.status} ${res.statusText}`;
    try {
      const text = await res.text();
      if (text) message = message + ` - ${text}`;
    } catch (_) { /* noop */ }
    throw new Error(message);
  }

  /** @type {{status:string,message:string,data:any[]}} */
  const body = await res.json();
  return body;
}


