import { describe, it, expect, vi, afterEach } from 'vitest';
import {
  getAllProductCategories,
  getProductCategoriesPage,
  createProductCategory,
  editProductCategory,
  deleteSpecificProductCategory,
  forceDeleteSpecificProductCategory
} from './productCategory.js';

declare const globalThis: any;

afterEach(() => {
  vi.restoreAllMocks();
});

describe('productCategory API', () => {
  it('getAllProductCategories returns data on success', async () => {
    const categories = [{ id: 1, categoryCode: '100000' }];
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: true, json: () => Promise.resolve(categories) }));

    const result = await getAllProductCategories();
    expect(result).toEqual(categories);
  });

  it('getProductCategoriesPage throws on failure with message', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({ ok: false, status: 500, statusText: 'Server Error', json: () => Promise.resolve({ message: 'boom' }) })
    );

    await expect(getProductCategoriesPage(0, 5, 'id', 'ascending')).rejects.toThrow(/boom/);
  });

  it('createProductCategory posts payload and returns response', async () => {
    const payload = { categoryCode: '123456', categoryName: 'X', description: '', isActive: true };
    const response = { data: { id: 9 } };
    const fetchSpy = vi
      .spyOn(globalThis, 'fetch')
      .mockResolvedValue({ ok: true, json: () => Promise.resolve(response) } as any);

    const result = await createProductCategory(payload as any);
    expect(result).toEqual(response);
    expect(fetchSpy).toHaveBeenCalled();
    const [url, init] = fetchSpy.mock.calls[0];
    expect(String(url)).toMatch(/product-categories$/);
    expect(init?.method).toBe('POST');
    expect(init?.body).toBe(JSON.stringify(payload));
  });

  it('editProductCategory uses PUT and returns response', async () => {
    const payload = { id: 2, categoryCode: '123456', categoryName: 'Y', description: '', isActive: true };
    const response = { message: 'ok' };
    const fetchSpy = vi
      .spyOn(globalThis, 'fetch')
      .mockResolvedValue({ ok: true, json: () => Promise.resolve(response) } as any);

    const result = await editProductCategory(payload as any);
    expect(result).toEqual(response);
    const [url, init] = fetchSpy.mock.calls[0];
    expect(String(url)).toMatch(/product-categories\/2$/);
    expect(init?.method).toBe('PUT');
  });

  it('deleteSpecificProductCategory uses DELETE', async () => {
    const response = { message: 'deleted' };
    const fetchSpy = vi
      .spyOn(globalThis, 'fetch')
      .mockResolvedValue({ ok: true, json: () => Promise.resolve(response) } as any);

    const result = await deleteSpecificProductCategory(3);
    expect(result).toEqual(response);
    const [url, init] = fetchSpy.mock.calls[0];
    expect(String(url)).toMatch(/product-categories\/3$/);
    expect(init?.method).toBe('DELETE');
  });

  it('forceDeleteSpecificProductCategory passes forceDelete param', async () => {
    const response = { message: 'deleted' };
    const fetchSpy = vi
      .spyOn(globalThis, 'fetch')
      .mockResolvedValue({ ok: true, json: () => Promise.resolve(response) } as any);

    const result = await forceDeleteSpecificProductCategory(4);
    expect(result).toEqual(response);
    const [url, init] = fetchSpy.mock.calls[0];
    expect(String(url)).toMatch(/product-categories\/4\?forceDelete=true$/);
    expect(init?.method).toBe('DELETE');
  });
});


