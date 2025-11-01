const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

/**
 * Save a calculation to the user's history
 * @param {Object} calculationData - The calculation data to save
 * @returns {Promise<Object>} The saved calculation
 */
export async function saveCalculation(calculationData) {
  try {
    const res = await fetch(`${API_BASE_URL}/calculation-history`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(calculationData)
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error('Save calculation error:', res.status, errorText);
      
      if (res.status === 401) {
        throw new Error('Please log in to save calculations');
      } else if (res.status === 400) {
        throw new Error('Invalid calculation data');
      } else {
        throw new Error('Failed to save calculation');
      }
    }

    const result = await res.json();
    return result;
  } catch (error) {
    console.error('Error saving calculation:', error);
    throw error;
  }
}

/**
 * Get all saved calculations for the current user
 * @returns {Promise<Array>} Array of saved calculations
 */
export async function getCalculations() {
  try {
    const res = await fetch(`${API_BASE_URL}/calculation-history`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error('Get calculations error:', res.status, errorText);
      
      if (res.status === 401) {
        throw new Error('Please log in to view calculations');
      } else {
        throw new Error('Failed to fetch calculations');
      }
    }

    const result = await res.json();
    return result;
  } catch (error) {
    console.error('Error fetching calculations:', error);
    throw error;
  }
}


