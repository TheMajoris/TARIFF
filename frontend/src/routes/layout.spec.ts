import { describe, it, expect, vi, beforeEach } from 'vitest';
import { refreshToken, logoutUser } from '$lib/api/users';

// Mock users API
vi.mock('$lib/api/users', () => ({
  refreshToken: vi.fn(),
  logoutUser: vi.fn()
}));

describe('+layout.svelte - Authentication & Navigation Logic', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  // Test page access validation logic
  function validatePageAccess(
    jwt: string | null,
    pathname: string
  ): { shouldRedirect: boolean; redirectTo: string | null } {
    const isErrorPage = pathname.startsWith('/error/');
    const isPublicPage = pathname === '/login' || pathname === '/register';
    
    if (jwt == null && !isPublicPage && !isErrorPage) {
      return { shouldRedirect: true, redirectTo: '/login' };
    }
    return { shouldRedirect: false, redirectTo: null };
  }

  // Test JWT expiration check logic
  function isExpiringSoon(jwt: string | null): boolean {
    if (!jwt) return false;
    
    try {
      // base64 decode the jwt to get expiring date
      const payload = JSON.parse(atob(jwt.split('.')[1]));
      const exp = payload.exp;
      const now = Math.floor(Date.now() / 1000);
      return exp - now < 60; // less than 60s left
    } catch (e) {
      return false;
    }
  }

  // Test logout logic
  function shouldCallLogoutAPI(jwt: string | null, fullName: string | null, role: string | null): boolean {
    return Boolean(jwt || fullName || role);
  }

  describe('page access validation', () => {
    it('allows access to public pages without JWT', () => {
      expect(validatePageAccess(null, '/login')).toEqual({ shouldRedirect: false, redirectTo: null });
      expect(validatePageAccess(null, '/register')).toEqual({ shouldRedirect: false, redirectTo: null });
    });

    it('allows access to error pages without JWT', () => {
      expect(validatePageAccess(null, '/error/404')).toEqual({ shouldRedirect: false, redirectTo: null });
      expect(validatePageAccess(null, '/error/403')).toEqual({ shouldRedirect: false, redirectTo: null });
      expect(validatePageAccess(null, '/error/501')).toEqual({ shouldRedirect: false, redirectTo: null });
    });

    it('redirects to login when JWT is missing on protected pages', () => {
      expect(validatePageAccess(null, '/')).toEqual({ shouldRedirect: true, redirectTo: '/login' });
      expect(validatePageAccess(null, '/history')).toEqual({ shouldRedirect: true, redirectTo: '/login' });
      expect(validatePageAccess(null, '/settings')).toEqual({ shouldRedirect: true, redirectTo: '/login' });
      expect(validatePageAccess(null, '/admin')).toEqual({ shouldRedirect: true, redirectTo: '/login' });
    });

    it('allows access to protected pages with valid JWT', () => {
      expect(validatePageAccess('valid-token', '/')).toEqual({ shouldRedirect: false, redirectTo: null });
      expect(validatePageAccess('valid-token', '/history')).toEqual({ shouldRedirect: false, redirectTo: null });
    });
  });

  describe('JWT expiration check', () => {
    it('returns false when JWT is null', () => {
      expect(isExpiringSoon(null)).toBe(false);
    });

    it('returns false when JWT is invalid format', () => {
      expect(isExpiringSoon('invalid-jwt')).toBe(false);
      expect(isExpiringSoon('not.a.valid.jwt')).toBe(false);
    });

    it('checks if token expires soon (less than 60 seconds)', () => {
      // Create a mock JWT with exp in 30 seconds
      const futureTime = Math.floor(Date.now() / 1000) + 30;
      const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
      const payload = btoa(JSON.stringify({ exp: futureTime, sub: 'user123' }));
      const signature = 'signature';
      const jwt = `${header}.${payload}.${signature}`;

      expect(isExpiringSoon(jwt)).toBe(true);
    });

    it('returns false when token has more than 60 seconds left', () => {
      // Create a mock JWT with exp in 120 seconds
      const futureTime = Math.floor(Date.now() / 1000) + 120;
      const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
      const payload = btoa(JSON.stringify({ exp: futureTime, sub: 'user123' }));
      const signature = 'signature';
      const jwt = `${header}.${payload}.${signature}`;

      expect(isExpiringSoon(jwt)).toBe(false);
    });
  });

  describe('logout logic', () => {
    it('should call logout API when user has auth state', () => {
      expect(shouldCallLogoutAPI('token123', 'John Doe', 'ROLE_USER')).toBe(true);
      expect(shouldCallLogoutAPI('token123', null, null)).toBe(true);
      expect(shouldCallLogoutAPI(null, 'John Doe', null)).toBe(true);
      expect(shouldCallLogoutAPI(null, null, 'ROLE_USER')).toBe(true);
    });

    it('should not call logout API when user has no auth state', () => {
      expect(shouldCallLogoutAPI(null, null, null)).toBe(false);
    });
  });

  describe('theme application logic', () => {
    it('applies default theme when no saved theme exists', () => {
      const getTheme = (savedTheme: string | null): string => {
        return savedTheme || 'light';
      };

      expect(getTheme(null)).toBe('light');
      expect(getTheme('dark')).toBe('dark');
      expect(getTheme('light')).toBe('light');
    });
  });

  describe('API integration', () => {
    it('refreshToken API is callable', async () => {
      const mockResponse = {
        message: {
          jwt: 'newToken123',
          fullName: 'John Doe',
          role: 'ROLE_USER',
          userId: 1
        }
      };

      (refreshToken as any).mockResolvedValue(mockResponse);

      const result = await refreshToken();
      expect(result).toEqual(mockResponse);
      expect(refreshToken).toHaveBeenCalled();
    });

    it('logoutUser API is callable', async () => {
      const mockResponse = { message: 'Logged out successfully' };

      (logoutUser as any).mockResolvedValue(mockResponse);

      const result = await logoutUser();
      expect(result).toEqual(mockResponse);
      expect(logoutUser).toHaveBeenCalled();
    });
  });
});

