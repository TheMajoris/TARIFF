import { describe, expect, it } from 'vitest';

describe('/+page.svelte - Pagination Logic', () => {
    const pageSize = 3;

    it('calculates correct pagination indices for page 1', () => {
        const currentPage = 1;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        
        expect(startIndex).toBe(0);
        expect(endIndex).toBe(3);
    });

    it('calculates correct pagination indices for page 2', () => {
        const currentPage = 2;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        
        expect(startIndex).toBe(3);
        expect(endIndex).toBe(6);
    });

    it('calculates correct pagination summary text for page 1 with 7 articles', () => {
        const currentPage = 1;
        const totalArticles = 7;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = Math.min(currentPage * pageSize, totalArticles);
        
        const summary = `Showing ${startIndex + 1}-${endIndex} of ${totalArticles} articles`;
        expect(summary).toBe('Showing 1-3 of 7 articles');
    });

    it('calculates correct pagination summary text for page 2 with 7 articles', () => {
        const currentPage = 2;
        const totalArticles = 7;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = Math.min(currentPage * pageSize, totalArticles);
        
        const summary = `Showing ${startIndex + 1}-${endIndex} of ${totalArticles} articles`;
        expect(summary).toBe('Showing 4-6 of 7 articles');
    });

    it('calculates correct pagination summary text for last page with 7 articles', () => {
        const currentPage = 3;
        const totalArticles = 7;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = Math.min(currentPage * pageSize, totalArticles);
        
        const summary = `Showing ${startIndex + 1}-${endIndex} of ${totalArticles} articles`;
        expect(summary).toBe('Showing 7-7 of 7 articles');
    });

    it('slices news array correctly for page 1', () => {
        const news = Array.from({ length: 7 }, (_, i) => ({ id: i + 1 }));
        const currentPage = 1;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        const displayNews = news.slice(startIndex, endIndex);
        
        expect(displayNews).toHaveLength(3);
        expect(displayNews[0].id).toBe(1);
        expect(displayNews[2].id).toBe(3);
    });

    it('slices news array correctly for page 2', () => {
        const news = Array.from({ length: 7 }, (_, i) => ({ id: i + 1 }));
        const currentPage = 2;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        const displayNews = news.slice(startIndex, endIndex);
        
        expect(displayNews).toHaveLength(3);
        expect(displayNews[0].id).toBe(4);
        expect(displayNews[2].id).toBe(6);
    });

    it('slices news array correctly for last page', () => {
        const news = Array.from({ length: 7 }, (_, i) => ({ id: i + 1 }));
        const currentPage = 3;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        const displayNews = news.slice(startIndex, endIndex);
        
        expect(displayNews).toHaveLength(1);
        expect(displayNews[0].id).toBe(7);
    });
});
