import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import {
  calculateTariffCost,
  getAllTariff,
  getTariffPage,
  createTariff,
  editTariff,
  deleteSpecificTariff
} from './tariff.js';

declare const globalThis: any;

afterEach(() => {
  vi.restoreAllMocks();
});

describe('tariff API', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('calculateTariffCost', () => {
    it('calculates tariff cost successfully for ad_valorem', async () => {
      const params = {
        hsCode: '100000',
        exportFrom: '1',
        importTo: '2',
        calculationDate: '2024-01-01',
        goodsValue: '1000',
        quantity: '10'
      };

      const mockResponse = {
        tariffRate: 5.0,
        tariffCost: 50.0,
        finalPrice: 1050.0,
        quantity: 10,
        tariffType: 'ad_valorem',
        rateUnit: 'percent'
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(mockResponse)
      } as any);

      const result = await calculateTariffCost(params);

      expect(result).toEqual({
        baseValue: '1000.00',
        tariffRate: '5.00',
        tariffCost: '50.00',
        totalCost: '1050.00',
        quantity: 10,
        tariffType: 'ad_valorem',
        rateUnit: 'percent'
      });
    });

    it('handles 404 error when no tariff data found', async () => {
      const params = {
        hsCode: '999999',
        exportFrom: '1',
        importTo: '2',
        calculationDate: '2024-01-01',
        goodsValue: '1000',
        quantity: '10'
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 404,
        statusText: 'Not Found',
        text: () => Promise.resolve('Not found')
      } as any);

      const result = await calculateTariffCost(params);
      expect(result).toBeNull();
    });

    it('handles 400 error for invalid parameters', async () => {
      const params = {
        hsCode: 'invalid',
        exportFrom: '1',
        importTo: '2',
        calculationDate: '2024-01-01',
        goodsValue: '1000',
        quantity: '10'
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 400,
        statusText: 'Bad Request',
        text: () => Promise.resolve('Invalid request')
      } as any);

      const result = await calculateTariffCost(params);
      expect(result).toBeNull();
    });
  });

  describe('getAllTariff', () => {
    it('fetches all tariffs successfully', async () => {
      const mockResponse = [
        { id: 1, tariffRate: 5.0, tariffType: 'ad_valorem' },
        { id: 2, tariffRate: 10.0, tariffType: 'specific' }
      ];

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await getAllTariff();

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/tariff-rate/all'),
        expect.objectContaining({
          method: 'GET',
          credentials: 'include'
        })
      );
    });
  });

  describe('getTariffPage', () => {
    it('fetches paginated tariffs successfully', async () => {
      const mockResponse = {
        content: [{ id: 1, tariffRate: 5.0 }],
        totalPages: 1,
        totalElements: 1
      };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await getTariffPage(0, 5, 'id', 'ascending');

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/tariff-rate?'),
        expect.objectContaining({
          method: 'GET'
        })
      );
    });
  });

  describe('createTariff', () => {
    it('creates tariff successfully', async () => {
      const payload = {
        tariffRate: 5.0,
        tariffType: 'ad_valorem',
        rateUnit: 'percent',
        effectiveDate: '2024-01-01',
        importingCountryCode: 'SG',
        exportingCountryCode: 'US',
        hsCode: '100000',
        unitQuantity: null
      };

      const mockResponse = { data: { id: 1 } };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await createTariff(payload);

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/tariff-rate'),
        expect.objectContaining({
          method: 'POST',
          body: JSON.stringify(payload)
        })
      );
    });
  });

  describe('editTariff', () => {
    it('edits tariff successfully', async () => {
      const payload = {
        id: 1,
        tariffRate: 6.0,
        tariffType: 'ad_valorem',
        rateUnit: 'percent',
        effectiveDate: '2024-01-01',
        importingCountryCode: 'SG',
        exportingCountryCode: 'US',
        unitQuantity: null,
        productCategory: { id: 1, categoryCode: '100000', categoryName: 'Test' }
      };

      const mockResponse = { message: 'Updated successfully' };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await editTariff(payload);

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/tariff-rate/1'),
        expect.objectContaining({
          method: 'PUT',
          body: JSON.stringify(payload)
        })
      );
    });
  });

  describe('deleteSpecificTariff', () => {
    it('deletes tariff successfully', async () => {
      const mockResponse = { message: 'Deleted successfully' };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await deleteSpecificTariff(1);

      expect(result).toEqual(mockResponse);
      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/tariff-rate/1'),
        expect.objectContaining({
          method: 'DELETE'
        })
      );
    });
  });
});

