import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { fetchCountries } from './countries.js';

declare const globalThis: any;

afterEach(() => {
  vi.restoreAllMocks();
});

describe('countries API', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('fetches countries successfully and maps them correctly', async () => {
    const mockResponse = [
      { id: 1, countryName: 'United States', countryCode: 'US' },
      { id: 2, countryName: 'Singapore', countryCode: 'SG' },
      { id: 3, countryName: 'United Kingdom', countryCode: 'UK' }
    ];

    const fetchSpy = vi
      .spyOn(globalThis, 'fetch')
      .mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(mockResponse)
      } as any);

    const result = await fetchCountries();

    expect(result).toEqual([
      { id: 1, name: 'United States', code: 'US' },
      { id: 2, name: 'Singapore', code: 'SG' },
      { id: 3, name: 'United Kingdom', code: 'UK' }
    ]);

    expect(fetchSpy).toHaveBeenCalledWith(
      expect.stringContaining('/countries')
    );
  });

  it('returns empty array when fetch fails', async () => {
    vi.spyOn(globalThis, 'fetch').mockRejectedValue(new Error('Network error'));

    const result = await fetchCountries();

    expect(result).toEqual([]);
  });

  it('returns empty array when response is not ok', async () => {
    vi.spyOn(globalThis, 'fetch').mockResolvedValue({
      ok: false,
      status: 500,
      statusText: 'Internal Server Error'
    } as any);

    const result = await fetchCountries();

    expect(result).toEqual([]);
  });

  it('handles empty response array', async () => {
    vi.spyOn(globalThis, 'fetch').mockResolvedValue({
      ok: true,
      json: () => Promise.resolve([])
    } as any);

    const result = await fetchCountries();

    expect(result).toEqual([]);
  });
});

