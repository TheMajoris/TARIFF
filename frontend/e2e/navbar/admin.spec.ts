import { expect, test } from '@playwright/test';

test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.getByRole('textbox', { name: 'Email' }).waitFor({ state: 'visible' });
    await page.getByRole('textbox', { name: 'Password' }).waitFor({ state: 'visible' });
    await page.getByRole('button', { name: 'Login' }).waitFor({ state: 'visible' });
    await page.getByRole('link', { name: 'Register →' }).waitFor({ state: 'visible' });

    await page.getByRole('textbox', { name: 'Email' }).click();
    await page.getByRole('textbox', { name: 'Email' }).fill('admin@cs203.com');
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('password123');
    await Promise.all([
        page.waitForResponse(resp => resp.url().includes('/login') && resp.status() === 200),
        page.getByRole('button', { name: 'Login' }).click()
    ]);
    await expect(page.getByRole('main')).toContainText('Welcome back! Login');
    await expect(page).toHaveURL('/');

    // check that side bar buttons are there
    await expect(page.getByRole('complementary').getByRole('link', { name: 'Dashboard' })).toBeVisible();
    await expect(page.getByRole('complementary').getByRole('link', { name: 'Calculation History' })).toBeVisible();
    await expect(page.getByRole('link', { name: 'Admin' })).toBeVisible();
    await expect(page.getByRole('complementary').getByRole('link', { name: 'Settings' })).toBeVisible();

    // check that bottom bar are there
    await expect(page.getByRole('contentinfo').getByRole('link', { name: 'Dashboard' })).toBeVisible();
    await expect(page.getByRole('contentinfo').getByRole('link', { name: 'Calculation History' })).toBeVisible();
    await expect(page.getByRole('contentinfo').getByRole('link', { name: 'Settings' })).toBeVisible();
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

    await page.getByRole('link', { name: 'Admin' }).click();
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