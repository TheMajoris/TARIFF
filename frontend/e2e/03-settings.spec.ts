import { expect, test } from '@playwright/test';
import { loginAdmin, settingPage } from './pageload';

test.beforeEach(async ({ page }) => {
    await loginAdmin({ page });
    await settingPage({ page });
});

test('change to dark theme', async ({ page }) => {

    await page.getByRole('combobox').selectOption('dark');
    await expect(page.locator('html')).toHaveAttribute('data-theme', 'dark');
});

test('change to light theme', async ({ page }) => {

    await page.getByRole('combobox').selectOption('light');
    await expect(page.locator('html')).toHaveAttribute('data-theme', 'light');
});

test('settings check account info', async ({ page }) => {

    await expect(page.getByRole('main')).toContainText('System Administrator');
    await expect(page.getByRole('main')).toContainText('ROLE_ADMIN');
    await expect(page.getByRole('main')).toContainText('1');
})