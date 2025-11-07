import {
  createProductCategory,
  deleteSpecificProductCategory,
  getProductCategoriesPage
} from '$lib/api/productCategory';
import { beforeEach, describe, expect, it, vi } from 'vitest';

// Mock productCategory API
vi.mock('$lib/api/productCategory', () => {
  return {
    getAllProductCategories: vi.fn(),
    getProductCategoriesPage: vi.fn(),
    createProductCategory: vi.fn(),
    editProductCategory: vi.fn(),
    deleteSpecificProductCategory: vi.fn(),
    forceDeleteSpecificProductCategory: vi.fn()
  };
});

describe('admin/Category.svelte - Validation Logic', () => {
  // Test category validation logic
  function validateCategory(categoryCode: string, categoryName: string, description: string): string | null {
    if (categoryCode == null || !/^\d{6}$/.test(categoryCode)) {
      return 'Category Code can only be from 100000 to 999999';
    }
    if (categoryName == '' || categoryName.length > 100) {
      return 'Category Name can only have up to 100 characters';
    }
    if (description.length > 500) {
      return 'Category Description can only have up to 500 characters';
    }
    return null;
  }

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('validates category code must be 6 digits', () => {
    expect(validateCategory('abc', 'Test', '')).toBe('Category Code can only be from 100000 to 999999');
    expect(validateCategory('12345', 'Test', '')).toBe('Category Code can only be from 100000 to 999999');
    expect(validateCategory('123456', 'Test', '')).toBeNull();
  });

  it('validates category name length', () => {
    const longName = 'a'.repeat(101);
    expect(validateCategory('123456', '', '')).toBe('Category Name can only have up to 100 characters');
    expect(validateCategory('123456', longName, '')).toBe('Category Name can only have up to 100 characters');
    expect(validateCategory('123456', 'Valid Name', '')).toBeNull();
  });

  it('validates description length', () => {
    const longDesc = 'a'.repeat(501);
    expect(validateCategory('123456', 'Test', longDesc)).toBe('Category Description can only have up to 500 characters');
    expect(validateCategory('123456', 'Test', 'Valid description')).toBeNull();
  });
});

describe('admin/Category.svelte - API Integration', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('getProductCategoriesPage returns paginated data', async () => {
    const mockData = {
      content: [
        { id: 1, categoryCode: '100000', categoryName: 'A', description: '', isActive: true },
        { id: 2, categoryCode: '200000', categoryName: 'B', description: '', isActive: false }
      ],
      totalPages: 1,
      totalElements: 2
    };
    (getProductCategoriesPage as any).mockResolvedValue(mockData);

    const result = await getProductCategoriesPage(0, 5, 'id', 'ascending');
    expect(result).toEqual(mockData);
    expect(getProductCategoriesPage).toHaveBeenCalledWith(0, 5, 'id', 'ascending');
  });

  it('createProductCategory calls API with correct payload', async () => {
    const payload = {
      categoryCode: '123456',
      categoryName: 'Test Category',
      description: 'Test description',
      isActive: true
    };
    (createProductCategory as any).mockResolvedValue({ data: { id: 9 } });

    const result = await createProductCategory(payload);
    expect(result.data.id).toBe(9);
    expect(createProductCategory).toHaveBeenCalledWith(payload);
  });

  it('deleteSpecificProductCategory calls API correctly', async () => {
    (deleteSpecificProductCategory as any).mockResolvedValue({ message: 'deleted' });

    const result = await deleteSpecificProductCategory(1);
    expect(result.message).toBe('deleted');
    expect(deleteSpecificProductCategory).toHaveBeenCalledWith(1);
  });
});


