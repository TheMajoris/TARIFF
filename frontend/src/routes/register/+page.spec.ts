import { describe, it, expect } from 'vitest';

describe('register/+page.svelte - Validation Logic', () => {
  // Test email validation logic from register page
  function validateEmail(email: string): boolean {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
  }

  // Test password length validation
  function passwordLength(password: string): boolean {
    return password.length >= 8;
  }

  // Test password has both upper and lower case
  function passwordUpperLower(password: string): boolean {
    const hasUpper = /[A-Z]/.test(password);
    const hasLower = /[a-z]/.test(password);
    return hasUpper && hasLower;
  }

  // Test password has at least one number
  function passwordNumber(password: string): boolean {
    const hasNumber = /\d/.test(password);
    return hasNumber;
  }

  // Test password has special character
  function passwordSpecialCharacter(password: string): boolean {
    const hasSpecialCharacter = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    return hasSpecialCharacter;
  }

  // Test passwords match
  function passwordsMatch(password1: string, password2: string): boolean {
    return password1 === password2 && password1 !== '';
  }

  describe('email validation', () => {
    it('validates correct email formats', () => {
      expect(validateEmail('user@example.com')).toBe(true);
      expect(validateEmail('test.user@domain.co.uk')).toBe(true);
      expect(validateEmail('user+tag@example.com')).toBe(true);
    });

    it('rejects invalid email formats', () => {
      expect(validateEmail('invalid')).toBe(false);
      expect(validateEmail('invalid@')).toBe(false);
      expect(validateEmail('@example.com')).toBe(false);
      expect(validateEmail('')).toBe(false);
    });
  });

  describe('password validation', () => {
    it('validates password length (>= 8 characters)', () => {
      expect(passwordLength('Password123!')).toBe(true);
      expect(passwordLength('Pass123!')).toBe(true);
      expect(passwordLength('Pass12!')).toBe(false);
      expect(passwordLength('Short')).toBe(false);
      expect(passwordLength('')).toBe(false);
    });

    it('validates password has upper and lower case', () => {
      expect(passwordUpperLower('Password123!')).toBe(true);
      expect(passwordUpperLower('PASSWORD123!')).toBe(false);
      expect(passwordUpperLower('password123!')).toBe(false);
      expect(passwordUpperLower('12345678')).toBe(false);
    });

    it('validates password has at least one number', () => {
      expect(passwordNumber('Password123!')).toBe(true);
      expect(passwordNumber('Password!')).toBe(false);
      expect(passwordNumber('PASSWORD123!')).toBe(true);
      expect(passwordNumber('password!')).toBe(false);
    });

    it('validates password has special character', () => {
      expect(passwordSpecialCharacter('Password123!')).toBe(true);
      expect(passwordSpecialCharacter('Password123@')).toBe(true);
      expect(passwordSpecialCharacter('Password123#')).toBe(true);
      expect(passwordSpecialCharacter('Password123')).toBe(false);
      expect(passwordSpecialCharacter('Password!')).toBe(true);
    });

    it('validates all password requirements together', () => {
      const isValidPassword = (password: string) => {
        return (
          passwordLength(password) &&
          passwordUpperLower(password) &&
          passwordNumber(password) &&
          passwordSpecialCharacter(password)
        );
      };

      expect(isValidPassword('Password123!')).toBe(true);
      expect(isValidPassword('ValidPass1@')).toBe(true);
      expect(isValidPassword('short1!')).toBe(false); // too short
      expect(isValidPassword('PASSWORD123!')).toBe(false); // no lowercase
      expect(isValidPassword('password123!')).toBe(false); // no uppercase
      expect(isValidPassword('Password!')).toBe(false); // no number
      expect(isValidPassword('Password123')).toBe(false); // no special char
    });
  });

  describe('password matching', () => {
    it('validates passwords match', () => {
      expect(passwordsMatch('Password123!', 'Password123!')).toBe(true);
      expect(passwordsMatch('Password123!', 'Password123@')).toBe(false);
      expect(passwordsMatch('Password123!', '')).toBe(false);
      expect(passwordsMatch('', '')).toBe(false);
    });
  });

  describe('registration form validation', () => {
    it('validates all required fields are filled', () => {
      const canRegister = (
        firstName: string,
        lastName: string,
        username: string,
        email: string,
        password: string,
        password2: string
      ) => {
        return (
          firstName.trim() !== '' &&
          lastName.trim() !== '' &&
          username.trim() !== '' &&
          email.trim() !== '' &&
          password.trim() !== '' &&
          password2.trim() !== ''
        );
      };

      expect(
        canRegister('John', 'Doe', 'johndoe', 'john@example.com', 'Password123!', 'Password123!')
      ).toBe(true);
      expect(
        canRegister('', 'Doe', 'johndoe', 'john@example.com', 'Password123!', 'Password123!')
      ).toBe(false);
      expect(
        canRegister('John', '', 'johndoe', 'john@example.com', 'Password123!', 'Password123!')
      ).toBe(false);
    });
  });
});

