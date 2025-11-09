import { expect, test } from '@playwright/test';
import { loginAdmin, navBar } from '../pageload';

test.beforeEach(async ({ page }) => {
    await loginAdmin({ page });

    await navBar({page});
    await expect(page.getByRole('link', { name: 'Tariff Management' })).toBeVisible();
});

test('sidebar click dashboard', async ({ page }) => {

    await page.getByRole('complementary').getByRole('link', { name: 'Dashboard' }).click();
    await expect(page).toHaveURL('/');
})

test('sidebar click Calculation History', async ({ page }) => {

    await page.getByRole('complementary').getByRole('link', { name: 'Calculation History' }).click();
    await expect(page).toHaveURL('/history');
})

test('sidebar click Admin', async ({ page }) => {

    await page.getByRole('link', { name: 'Tariff Management' }).click();
    await expect(page).toHaveURL('/admin');
})

test('sidebar click Settings', async ({ page }) => {

    await page.getByRole('complementary').getByRole('link', { name: 'Settings' }).click();
    await expect(page).toHaveURL('/settings');
})

test('bottom bar click dashboard', async ({ page }) => {

    await page.getByRole('contentinfo').getByRole('link', { name: 'Dashboard' }).click();
    await expect(page).toHaveURL('/');
})

test('bottom bar click Calculation History', async ({ page }) => {

    await page.getByRole('contentinfo').getByRole('link', { name: 'Calculation History' }).click();
    await expect(page).toHaveURL('/history');
})

test('bottom bar click Settings', async ({ page }) => {

    await page.getByRole('contentinfo').getByRole('link', { name: 'Settings' }).click();
    await expect(page).toHaveURL('/settings');
})