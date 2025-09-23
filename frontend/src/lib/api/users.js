// User registration API
/**
 * @param {{firstName: string, lastName: string, email: string, password: string, username: string}} userData
 */
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


// User login API
/**
 * @param {{email: string, password: string}} userData
 */
export async function loginUser(userData) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Logging in user at:', `${API_BASE_URL}/users`);
    
    const res = await fetch(`${API_BASE_URL}/auth/login`, {
      method: "POST",
      headers: { 
        "Content-Type": "application/json",
        "Accept": "application/json"
      },
      body: JSON.stringify({
        email: userData.email,
        password: userData.password
      })
    });
    
    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      console.log(errorData)
      throw new Error(errorData.message || `Login failed: ${res.status} ${res.statusText}`);
    }
    
    const result = await res.json();
    console.log('Login result:', result);
    
    return result;
    
  } catch (err) {
    console.error("loginUser error:", err);
    throw err; // Re-throw to let the calling code handle the error
  }
}
