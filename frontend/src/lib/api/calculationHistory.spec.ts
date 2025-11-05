import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { saveCalculation, getCalculations } from './calculationHistory.js';

declare const globalThis: any;

afterEach(() => {
  vi.restoreAllMocks();
});

describe('calculationHistory API', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('saveCalculation', () => {
    it('saves calculation successfully', async () => {
      const calculationData = {
        hsCode: '100000',
        exportFrom: 'US',
        importTo: 'SG',
        calculationDate: '2024-01-01',
        goodsValue: 1000,
        tariffCost: 50,
        totalCost: 1050
      };

      const mockResponse = {
        id: 1,
        ...calculationData,
        createdAt: '2024-01-01T00:00:00Z'
      };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await saveCalculation(calculationData);

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/calculation-history'),
        expect.objectContaining({
          method: 'POST',
          credentials: 'include',
          headers: expect.objectContaining({
            'Content-Type': 'application/json'
          }),
          body: JSON.stringify(calculationData)
        })
      );
    });

    it('throws error when user is not authenticated', async () => {
      const calculationData = {
        hsCode: '100000',
        exportFrom: 'US',
        importTo: 'SG',
        calculationDate: '2024-01-01',
        goodsValue: 1000
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 401,
        text: () => Promise.resolve('Unauthorized')
      } as any);

      await expect(saveCalculation(calculationData)).rejects.toThrow(
        'Please log in to save calculations'
      );
    });

    it('throws error when calculation data is invalid', async () => {
      const calculationData = {
        hsCode: '',
        exportFrom: '',
        importTo: '',
        calculationDate: '',
        goodsValue: 0
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 400,
        text: () => Promise.resolve('Bad Request')
      } as any);

      await expect(saveCalculation(calculationData)).rejects.toThrow(
        'Invalid calculation data'
      );
    });
  });

  describe('getCalculations', () => {
    it('fetches calculations successfully', async () => {
      const mockResponse = [
        {
          id: 1,
          hsCode: '100000',
          exportFrom: 'US',
          importTo: 'SG',
          calculationDate: '2024-01-01',
          goodsValue: 1000,
          tariffCost: 50,
          totalCost: 1050
        },
        {
          id: 2,
          hsCode: '200000',
          exportFrom: 'UK',
          importTo: 'SG',
          calculationDate: '2024-01-02',
          goodsValue: 2000,
          tariffCost: 100,
          totalCost: 2100
        }
      ];

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await getCalculations();

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/calculation-history'),
        expect.objectContaining({
          method: 'GET',
          credentials: 'include'
        })
      );
    });

    it('throws error when user is not authenticated', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 401,
        text: () => Promise.resolve('Unauthorized')
      } as any);

      await expect(getCalculations()).rejects.toThrow(
        'Please log in to view calculations'
      );
    });

    it('throws error when fetch fails', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 500,
        text: () => Promise.resolve('Internal Server Error')
      } as any);

      await expect(getCalculations()).rejects.toThrow(
        'Failed to fetch calculations'
      );
    });
  });
});

