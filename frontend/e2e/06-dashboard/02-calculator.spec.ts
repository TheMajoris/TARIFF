import { expect, test } from '@playwright/test';
import { calculatorPage, loginAdmin } from '../pageload';

test.beforeEach(async ({ page }) => {
    await loginAdmin({ page });
    await calculatorPage({ page });
});

test('calculator success ad valorem calculation', async ({ page }) => {

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

    // check calculation is correct
    await expect(page.getByText('Base Value: $')).toBeVisible();
    await expect(page.getByRole('main')).toContainText('Ad Valorem');
    await expect(page.getByRole('main')).toContainText('2.5%');
    await expect(page.getByRole('main')).toContainText('+ $2.50');
    await expect(page.getByRole('main')).toContainText('$102.50');
})

test('calculator success specific calculation', async ({ page }) => {

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

    // check calculation is correct
    await expect(page.getByText('Base Value: $')).toBeVisible();
    await expect(page.getByRole('main')).toContainText('Specific');
    await expect(page.getByRole('main')).toContainText('$35.0/1kg');
    await expect(page.getByRole('main')).toContainText('+ $7000.00');
    await expect(page.getByRole('main')).toContainText('$7100.00');
})

test('calculator hscode does not exist', async ({ page }) => {

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('123456');
    await page.locator('form div').filter({ hasText: 'Importing To Select country' }).locator('span').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('sg');
    await page.locator('form div').filter({ hasText: '(SG) Singapore' }).nth(4).click();
    await page.getByText('Select country').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('us');
    await page.getByText('(US) United States').click();
    await page.locator('input[type="date"]').fill('2023-11-06');
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').fill('100');
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').fill('200');
    await page.getByRole('button', { name: 'Calculate Cost' }).click();
    await expect(page.getByRole('main')).toContainText('No tariff data found for the specified countries and product. Please check your selection or contact support.');

})

test('calculator invalid hscode', async ({ page }) => {

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('1234567');
    await page.locator('form div').filter({ hasText: 'Importing To Select country' }).locator('span').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('sg');
    await page.locator('form div').filter({ hasText: '(SG) Singapore' }).nth(4).click();
    await page.getByText('Select country').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('us');
    await page.getByText('(US) United States').click();
    await page.locator('input[type="date"]').fill('2023-11-06');
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').fill('100');
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').fill('200');
    await page.getByRole('button', { name: 'Calculate Cost' }).click();
    await expect(page.getByRole('main')).toContainText('Please enter a valid HS Code format (6 digits, e.g., 850110)');
})

test('calculator missing values', async ({ page }) => {

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('854231');
    // await page.locator('form div').filter({ hasText: 'Importing To Select country' }).locator('span').click();
    // await page.getByRole('textbox', { name: 'Type to search...' }).click();
    // await page.getByRole('textbox', { name: 'Type to search...' }).fill('sg');
    // await page.locator('form div').filter({ hasText: '(SG) Singapore' }).nth(4).click();
    // await page.getByText('Select country').click();
    // await page.getByRole('textbox', { name: 'Type to search...' }).click();
    // await page.getByRole('textbox', { name: 'Type to search...' }).fill('us');
    // await page.getByText('(US) United States').click();
    // await page.locator('input[type="date"]').fill('2023-11-06');
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').fill('100');
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').fill('200');
    await page.getByRole('button', { name: 'Calculate Cost' }).click();
    await expect(page.getByRole('main')).toContainText('Please fill in all fields before calculating.');

})

test('calculator current date have no tariff', async ({ page }) => {

    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).click();
    await page.getByRole('textbox', { name: 'Enter HS Code (e.g., 850110)' }).fill('854231');
    await page.locator('form div').filter({ hasText: 'Importing To Select country' }).locator('span').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('sg');
    await page.locator('form div').filter({ hasText: '(SG) Singapore' }).nth(4).click();
    await page.getByText('Select country').click();
    await page.getByRole('textbox', { name: 'Type to search...' }).click();
    await page.getByRole('textbox', { name: 'Type to search...' }).fill('us');
    await page.getByText('(US) United States').click();
    // await page.locator('input[type="date"]').fill('2023-11-06');
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Value of Goods (USD)' }).getByPlaceholder('Enter value').fill('100');
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').click();
    await page.locator('form div').filter({ hasText: 'Quantity of Goods (KG)' }).getByPlaceholder('Enter value').fill('200');

    await page.getByRole('button', { name: 'Calculate Cost' }).click();
    await expect(page.getByRole('main')).toContainText('No tariff data found for the specified countries and product. Please check your selection or contact support.');

})