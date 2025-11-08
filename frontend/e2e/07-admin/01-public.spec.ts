import { expect, test } from '@playwright/test';
import { loginUser } from '../pageload';

test('admin redirect public', async ({ page }) => {
    await page.goto('/admin');
    await expect(page).toHaveURL('/login');
})

test('admin redirect user', async ({ page }) => {
    await loginUser({ page });
    await page.goto('/admin');
    await expect(page).toHaveURL('/error/403');
})