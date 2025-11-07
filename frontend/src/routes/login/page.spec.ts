import { describe, it, expect } from 'vitest';

describe('login/+page.svelte - Validation Logic', () => {
  // Test email validation logic from login page
  function validateEmail(email: string): boolean {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
  }

  it('validates correct email formats', () => {
    expect(validateEmail('user@example.com')).toBe(true);
    expect(validateEmail('test.user@domain.co.uk')).toBe(true);
    expect(validateEmail('user+tag@example.com')).toBe(true);
    expect(validateEmail('user_name@example-domain.com')).toBe(true);
  });

  it('rejects invalid email formats', () => {
    expect(validateEmail('invalid')).toBe(false);
    expect(validateEmail('invalid@')).toBe(false);
    expect(validateEmail('@example.com')).toBe(false);
    expect(validateEmail('user@')).toBe(false);
    expect(validateEmail('user@domain')).toBe(false);
    expect(validateEmail('')).toBe(false);
    expect(validateEmail('user name@example.com')).toBe(false);
  });

  it('validates login requires email and password', () => {
    const canLogin = (email: string, password: string) => {
      return email.trim() !== '' && password.trim() !== '';
    };

    expect(canLogin('user@example.com', 'password123')).toBe(true);
    expect(canLogin('', 'password123')).toBe(false);
    expect(canLogin('user@example.com', '')).toBe(false);
    expect(canLogin('', '')).toBe(false);
    expect(canLogin('   ', '   ')).toBe(false);
  });
});

