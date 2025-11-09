import { expect, test } from '@playwright/test';

test('dashboard redirect public', async ({ page }) => {
    await page.goto('/');
    await expect(page).toHaveURL('/login');
})