import { expect, test } from '@playwright/test';
import { calculatorPage, loginAdmin } from '../pageload';

test.beforeEach(async ({ page }) => {
    await loginAdmin({ page });
    await calculatorPage({ page });
});

test('calculator no optimized route', async ({ page }) => {

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('851713');
    await page.locator('form div').filter({ hasText: 'Importing To Select country' }).locator('span').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('au');
    await page.locator('form div').filter({ hasText: '(AU) Australia' }).nth(4).click();
    await page.getByText('Select country').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('my');
    await page.getByText('(MY) Malaysia').click();
    await page.locator('input[type="date"]').fill('2024-11-06');
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').fill('100');
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').fill('200');
    await page.getByRole('button', { name: 'Calculate Cost' }).click();


    await page.getByRole('button', { name: 'Find Optimized Route' }).click();
    await expect(page.getByRole('main')).toContainText('No alternative routes found. The direct route is already optimal.');
})

test('calculator have optimized route', async ({ page }) => {

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('851713');
    await page.locator('form div').filter({ hasText: 'Importing To Select country' }).locator('span').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('au');
    await page.locator('form div').filter({ hasText: '(AU) Australia' }).nth(4).click();
    await page.getByText('Select country').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('cn');
    await page.getByText('(CN) China').click();
    await page.locator('input[type="date"]').fill('2024-11-06');
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').fill('100');
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').fill('200');
    await page.getByRole('button', { name: 'Calculate Cost' }).click();


    await page.getByRole('button', { name: 'Find Optimized Route' }).click();
    await expect(page.getByRole('main')).toContainText('Alternative Routes & Cost Comparison');
    
    await expect(page.getByRole('main')).toContainText('Route 1 Cheapest Trade Route China Malaysia Australia Original Cost $125.00 Optimized Cost $107.50 You save $17.50 (14% off) View Detailed Breakdown');
    await page.getByRole('button', { name: 'View Detailed Breakdown' }).click();
    await expect(page.getByRole('main')).toContainText('Tariff Breakdown 1 China → Malaysia $5.00 Tariff Rate: 5.0%2 Malaysia → Australia $2.50 Tariff Rate: 2.5% This route takes advantage of trade agreements and preferential tariff rates.');
})
