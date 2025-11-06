// function tests to run before all tests for each page to load before any actions
import { expect, test } from '@playwright/test';

export async function loginAdmin({ page }) {
    await loginPage({ page });

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
}

export async function loginUser({ page }) {
    await loginPage({ page });

    await page.getByRole('textbox', { name: 'Email' }).click();
    await page.getByRole('textbox', { name: 'Email' }).fill('jane.smith@cs203.com');
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('password123');
    await Promise.all([
        page.waitForResponse(resp => resp.url().includes('/login') && resp.status() === 200),
        page.getByRole('button', { name: 'Login' }).click()
    ]);
    await expect(page.getByRole('main')).toContainText('Welcome back! Login');
    await expect(page).toHaveURL('/');
}

export async function loginPage({ page }) {
    await page.goto('/login');
    await page.getByRole('textbox', { name: 'Email' }).waitFor({ state: 'visible' });
    await page.getByRole('textbox', { name: 'Password' }).waitFor({ state: 'visible' });
    await page.getByRole('button', { name: 'Login' }).waitFor({ state: 'visible' });
    await page.getByRole('link', { name: 'Register →' }).waitFor({ state: 'visible' });
}

export async function registerPage({ page }) {
    await page.goto('/register');
    await page.getByRole('textbox', { name: 'First Name' }).waitFor({ state: 'visible' });
    await page.getByRole('textbox', { name: 'Last Name' }).waitFor({ state: 'visible' });
    await page.getByRole('textbox', { name: 'Username' }).waitFor({ state: 'visible' });
    await page.getByRole('textbox', { name: 'Email' }).waitFor({ state: 'visible' });
    await page.getByRole('textbox', { name: 'Password', exact: true }).waitFor({ state: 'visible' });
    await page.getByRole('textbox', { name: 'Confirm Password' }).waitFor({ state: 'visible' });
    await page.getByRole('button', { name: 'Register' }).waitFor({ state: 'visible' });
    await page.getByRole('link', { name: '← Back to Login' }).waitFor({ state: 'visible' });
}

export async function navBar({ page }) {
    // check that side bar buttons are there
    await expect(page.getByRole('complementary').getByRole('link', { name: 'Dashboard' })).toBeVisible();
    await expect(page.getByRole('complementary').getByRole('link', { name: 'Calculation History' })).toBeVisible();
    await expect(page.getByRole('complementary').getByRole('link', { name: 'Settings' })).toBeVisible();

    // check that bottom bar are there
    await expect(page.getByRole('contentinfo').getByRole('link', { name: 'Dashboard' })).toBeVisible();
    await expect(page.getByRole('contentinfo').getByRole('link', { name: 'Calculation History' })).toBeVisible();
    await expect(page.getByRole('contentinfo').getByRole('link', { name: 'Settings' })).toBeVisible();
}

export async function calculatorPage({ page }) {
    await page.goto('/');
    // check that calculator components are there
    await expect(page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' })).toBeVisible();
    await expect(page.locator('div').filter({ hasText: /^Select country$/ }).first()).toBeVisible();
    await expect(page.locator('div').filter({ hasText: /^Select country$/ }).nth(1)).toBeVisible();
    await expect(page.locator('input[type="date"]')).toBeVisible();
    await expect(page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value')).toBeVisible();
    await expect(page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Calculate Cost' })).toBeVisible();
}

export async function calculatorHistoryPage({ page }) {
    await page.goto('/history');

    // check that calculator history components are there
    await expect(page.getByRole('heading', { name: 'Calculation History' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Refresh' })).toBeVisible();
}

export async function settingPage({ page }) {

    await page.goto('/settings');

    await expect(page.getByText('Appearance Choose a theme for')).toBeVisible();
    await expect(page.getByText('Account Your profile')).toBeVisible();
    await expect(page.getByRole('combobox')).toBeVisible();
}

export async function adminPage({ page }) {
    await page.goto('/admin');

    await expect(page.getByRole('heading', { name: 'Admin' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Tariffs' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Product Categories' })).toBeVisible();
}