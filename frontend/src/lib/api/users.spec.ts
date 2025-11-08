import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import {
  registerUser,
  loginUser,
  logoutUser,
  refreshToken
} from './users.js';

declare const globalThis: any;

afterEach(() => {
  vi.restoreAllMocks();
});

describe('users API', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('registerUser', () => {
    it('registers user successfully with correct payload', async () => {
      const userData = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com',
        password: 'Password123!',
        username: 'johndoe'
      };

      const mockResponse = {
        message: 'User registered successfully',
        userId: 1
      };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await registerUser(userData);

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/users'),
        expect.objectContaining({
          method: 'POST',
          headers: expect.objectContaining({
            'Content-Type': 'application/json'
          }),
          body: JSON.stringify({
            firstName: userData.firstName,
            lastName: userData.lastName,
            email: userData.email,
            password: userData.password,
            username: userData.username,
            isAdmin: false
          })
        })
      );
    });

    it('throws error when registration fails', async () => {
      const userData = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com',
        password: 'Password123!',
        username: 'johndoe'
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 400,
        statusText: 'Bad Request',
        json: () => Promise.resolve({ message: 'Email already exists' })
      } as any);

      await expect(registerUser(userData)).rejects.toThrow('Email already exists');
    });
  });

  describe('loginUser', () => {
    it('logs in user successfully', async () => {
      const userData = {
        email: 'john@example.com',
        password: 'Password123!'
      };

      const mockResponse = {
        message: {
          jwt: 'token123',
          fullName: 'John Doe',
          role: 'USER',
          userId: 1
        }
      };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await loginUser(userData);

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/auth/login'),
        expect.objectContaining({
          method: 'POST',
          credentials: 'include',
          body: JSON.stringify({
            email: userData.email,
            password: userData.password
          })
        })
      );
    });

    it('throws error when login fails', async () => {
      const userData = {
        email: 'john@example.com',
        password: 'WrongPassword'
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 401,
        statusText: 'Unauthorized',
        json: () => Promise.resolve({ message: 'Invalid credentials' })
      } as any);

      await expect(loginUser(userData)).rejects.toThrow('Invalid credentials');
    });
  });

  describe('logoutUser', () => {
    it('logs out user successfully', async () => {
      const mockResponse = { message: 'Logged out successfully' };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await logoutUser();

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/auth/logout'),
        expect.objectContaining({
          method: 'POST',
          credentials: 'include'
        })
      );
    });

    it('throws error when logout fails', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 500,
        statusText: 'Internal Server Error',
        json: () => Promise.resolve({ message: 'Logout failed' })
      } as any);

      await expect(logoutUser()).rejects.toThrow('Logout failed');
    });
  });

  describe('refreshToken', () => {
    it('refreshes token successfully', async () => {
      const mockResponse = {
        message: {
          jwt: 'newToken123',
          fullName: 'John Doe',
          role: 'USER',
          userId: 1
        }
      };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await refreshToken();

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/auth/refresh'),
        expect.objectContaining({
          method: 'POST',
          credentials: 'include'
        })
      );
    });

    it('throws error when token refresh fails', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 401,
        statusText: 'Unauthorized',
        json: () => Promise.resolve({ message: 'Token expired' })
      } as any);

      await expect(refreshToken()).rejects.toThrow('Token expired');
    });
  });
});

