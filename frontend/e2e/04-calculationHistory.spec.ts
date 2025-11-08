import { expect, test } from '@playwright/test';
import { calculatorHistoryPage, calculatorPage, loginAdmin } from './pageload';

test.beforeEach(async ({ page }) => {
    await loginAdmin({ page });

    await calculatorHistoryPage({ page });
});

test('check empty calculator history', async ({ page }) => {
    await expect(page.getByRole('main')).toContainText('No Saved Calculations');
    await page.getByRole('link', { name: 'Go to Calculator' }).click();
    await expect(page).toHaveURL('/');
})

test('save calculator history ad valorem calculation', async ({ page }) => {
    // go back to calculator
    await calculatorPage({ page });

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

    // save
    await expect(page.getByRole('button', { name: 'Save' })).toBeVisible();
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'Add any additional notes...' })).toBeVisible();
    await expect(page.locator('button').filter({ hasText: /^Save$/ })).toBeVisible();
    await page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' }).click();
    await page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' }).fill('Ad valorem tariff');
    await page.getByRole('textbox', { name: 'Add any additional notes...' }).click();
    await page.getByRole('textbox', { name: 'Add any additional notes...' }).fill('Ad valorem tariff notes');
    await page.locator('button').filter({ hasText: /^Save$/ }).click();
    await expect(page.locator('html')).toContainText('Calculation saved successfully! View it in your calculation history.');
})

test('save calculator history specific calculation', async ({ page }) => {
    // go back to calculator
    await calculatorPage({ page });

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('854170');
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

    // save
    await expect(page.getByRole('button', { name: 'Save' })).toBeVisible();
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'Add any additional notes...' })).toBeVisible();
    await expect(page.locator('button').filter({ hasText: /^Save$/ })).toBeVisible();
    await page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' }).click();
    await page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' }).fill('Specific tariff');
    await page.getByRole('textbox', { name: 'Add any additional notes...' }).click();
    await page.getByRole('textbox', { name: 'Add any additional notes...' }).fill('Specific tariff notes');
    await page.locator('button').filter({ hasText: /^Save$/ }).click();
    await expect(page.locator('html')).toContainText('Calculation saved successfully! View it in your calculation history.');
})

test('save calculator history without notes', async ({ page }) => {
    // go back to calculator
    await calculatorPage({ page });

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('851713');
    await page.locator('form div').filter({ hasText: 'Importing To Select country' }).locator('span').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('sg');
    await page.locator('form div').filter({ hasText: '(SG) Singapore' }).nth(4).click();
    await page.getByText('Select country').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('cn');
    await page.getByText('(CN) China').click();
    await page.locator('input[type="date"]').fill('2023-11-06');
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').fill('100');
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').fill('200');
    await page.getByRole('button', { name: 'Calculate Cost' }).click();

    // save
    await expect(page.getByRole('button', { name: 'Save' })).toBeVisible();
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'Add any additional notes...' })).toBeVisible();
    await expect(page.locator('button').filter({ hasText: /^Save$/ })).toBeVisible();
    await page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' }).click();
    await page.getByRole('textbox', { name: 'e.g., Laptop Import US-CN' }).fill('Specific tariff 2');
    await page.locator('button').filter({ hasText: /^Save$/ }).click();
    await expect(page.locator('html')).toContainText('Calculation saved successfully! View it in your calculation history.');
})

test('check Ad Valorem history', async ({ page }) => {

    await expect(page.getByRole('main')).toContainText('Ad valorem tariff');
    await expect(page.getByRole('main')).toContainText('851713');
    await expect(page.getByRole('main')).toContainText('MY → AU');
    await expect(page.getByRole('main')).toContainText('USD 100.00');
    await expect(page.getByRole('main')).toContainText('2.50%');
    await expect(page.getByRole('main')).toContainText('Ad Valorem');
    await expect(page.getByRole('main')).toContainText('USD 2.50');
    await expect(page.getByRole('main')).toContainText('USD 102.50');
    await expect(page.getByRole('main')).toContainText('Ad valorem tariff notes');
})


test('check Specific history', async ({ page }) => {
    await expect(page.getByRole('main')).toContainText('Specific tariff');
    await expect(page.getByRole('main')).toContainText('854170');
    await expect(page.getByRole('main')).toContainText('CN → AU');
    await expect(page.getByRole('main')).toContainText('USD 7000.00');
    await expect(page.getByRole('main')).toContainText('$35.00/1.00kg');
    await expect(page.getByRole('main')).toContainText('Specific');
    await expect(page.getByRole('main')).toContainText('USD 7000.00');
    await expect(page.getByRole('main')).toContainText('USD 7100.00');
    await expect(page.getByRole('main')).toContainText('Specific tariff notes');
})

test('check history without notes', async ({ page }) => {


    await expect(page.getByRole('main')).toContainText('Specific tariff 2');
    await expect(page.getByRole('main')).toContainText('851713');
    await expect(page.getByRole('main')).toContainText('CN → SG');
    await expect(page.getByRole('main')).toContainText('USD 100.00');
    await expect(page.getByRole('main')).toContainText('$2.50/1.00kg');
    await expect(page.getByRole('main')).toContainText('Specific');
    await expect(page.getByRole('main')).toContainText('USD 500.00');
    await expect(page.getByRole('main')).toContainText('USD 600.00');
})