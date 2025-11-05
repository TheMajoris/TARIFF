import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { fetchNews, fetchNewsByTags } from './news.js';

declare const globalThis: any;

afterEach(() => {
  vi.restoreAllMocks();
});

describe('news API', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('fetchNews', () => {
    it('fetches news successfully and maps them correctly', async () => {
      const mockResponse = {
        articles: [
          {
            id: 2,
            headline: 'Tariff Update',
            summary: 'New tariff rates announced',
            url: 'https://example.com/news1',
            tags: ['tariff', 'trade'],
            publishedDate: '2024-01-02'
          },
          {
            id: 1,
            headline: 'Trade News',
            summary: 'Trade agreement signed',
            url: 'https://example.com/news2',
            tags: ['trade'],
            publishedDate: '2024-01-01'
          }
        ]
      };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await fetchNews();

      // Should be sorted by ID descending (most recent first)
      expect(result).toEqual([
        {
          id: 2,
          title: 'Tariff Update',
          summary: 'New tariff rates announced',
          link: 'https://example.com/news1',
          tags: ['tariff', 'trade'],
          date: '2024-01-02'
        },
        {
          id: 1,
          title: 'Trade News',
          summary: 'Trade agreement signed',
          link: 'https://example.com/news2',
          tags: ['trade'],
          date: '2024-01-01'
        }
      ]);

      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/news')
      );
    });

    it('returns empty array when response structure is invalid', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({})
      } as any);

      const result = await fetchNews();

      expect(result).toEqual([]);
    });

    it('returns empty array when articles is not an array', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({ articles: null })
      } as any);

      const result = await fetchNews();

      expect(result).toEqual([]);
    });

    it('handles articles without publishedDate', async () => {
      const mockResponse = {
        articles: [
          {
            id: 1,
            headline: 'News Without Date',
            summary: 'Test summary',
            url: 'https://example.com/news',
            tags: ['test']
          }
        ]
      };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(mockResponse)
      } as any);

      const result = await fetchNews();

      expect(result[0].date).toBe('Date unavailable');
    });

    it('returns empty array when fetch fails', async () => {
      vi.spyOn(globalThis, 'fetch').mockRejectedValue(new Error('Network error'));

      const result = await fetchNews();

      expect(result).toEqual([]);
    });
  });

  describe('fetchNewsByTags', () => {
    it('fetches news by tags successfully', async () => {
      const tags = ['tariff', 'trade'];
      const mockResponse = {
        articles: [
          {
            id: 1,
            headline: 'Tariff News',
            summary: 'Tariff update',
            url: 'https://example.com/news',
            tags: ['tariff', 'trade']
          }
        ]
      };

      const fetchSpy = vi
        .spyOn(globalThis, 'fetch')
        .mockResolvedValue({
          ok: true,
          json: () => Promise.resolve(mockResponse)
        } as any);

      const result = await fetchNewsByTags(tags);

      expect(result).toEqual([
        {
          id: 1,
          title: 'Tariff News',
          summary: 'Tariff update',
          link: 'https://example.com/news',
          tags: ['tariff', 'trade'],
          date: null
        }
      ]);

      expect(fetchSpy).toHaveBeenCalledWith(
        expect.stringContaining('/news?tags=tariff&tags=trade')
      );
    });

    it('returns empty array when fetch fails', async () => {
      vi.spyOn(globalThis, 'fetch').mockRejectedValue(new Error('Network error'));

      const result = await fetchNewsByTags(['tariff']);

      expect(result).toEqual([]);
    });

    it('handles empty tags array', async () => {
      const mockResponse = { articles: [] };

      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(mockResponse)
      } as any);

      const result = await fetchNewsByTags([]);

      expect(result).toEqual([]);
    });
  });
});

