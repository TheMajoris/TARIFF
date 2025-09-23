// User registration API
export async function registerUser(userData) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Registering user at:', `${API_BASE_URL}/users`);
    
    const res = await fetch(`${API_BASE_URL}/users`, {
      method: "POST",
      headers: { 
        "Content-Type": "application/json",
        "Accept": "application/json"
      },
      body: JSON.stringify({
        firstName: userData.firstName,
        lastName: userData.lastName,
        email: userData.email,
        password: userData.password,
        username: userData.username,
        isAdmin: false // Default to regular user
      })
    });
    
    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Registration failed: ${res.status} ${res.statusText}`);
    }
    
    const result = await res.json();
    console.log('Registration result:', result);
    
    return result;
    
  } catch (err) {
    console.error("registerUser error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}
