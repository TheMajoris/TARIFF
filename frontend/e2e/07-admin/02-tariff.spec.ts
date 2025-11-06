import { expect, test } from '@playwright/test';
import { adminPage, loginAdmin } from '../pageload';

test.beforeEach(async ({ page }) => {
  await loginAdmin({ page });
  await adminPage({ page });
});

test('admin tariff loaded', async ({ page }) => {

  await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Tariff Id ▲ Importing Country Exporting Country Preferential Tariff Tariff Type Tariff Rate Effective Date Expiry Date":
          - cell "Tariff Id ▲"
          - cell "Importing Country"
          - cell "Exporting Country"
          - cell "Preferential Tariff"
          - cell "Tariff Type"
          - cell "Tariff Rate"
          - cell "Effective Date"
          - cell "Expiry Date"
          - cell
      - rowgroup:
        - row /1 SG CN No Ad Valorem 1% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "1"
          - cell "SG"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell "1%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /2 SG US No Ad Valorem 5% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "2"
          - cell "SG"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell "5%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /3 MY SG Yes Ad Valorem 0% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "3"
          - cell "MY"
          - cell "SG"
          - cell "Yes"
          - cell "Ad Valorem"
          - cell "0%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /4 CN US No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "4"
          - cell "CN"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /5 US CN No Ad Valorem \\d+\\.\\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "5"
          - cell "US"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+\\.\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ tariffs/
    - button "Previous" [disabled]
    - button "Next"
    `);
})

test('admin tariff pagination', async ({ page }) => {

  await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Tariff Id ▲ Importing Country Exporting Country Preferential Tariff Tariff Type Tariff Rate Effective Date Expiry Date":
          - cell "Tariff Id ▲"
          - cell "Importing Country"
          - cell "Exporting Country"
          - cell "Preferential Tariff"
          - cell "Tariff Type"
          - cell "Tariff Rate"
          - cell "Effective Date"
          - cell "Expiry Date"
          - cell
      - rowgroup:
        - row /1 SG CN No Ad Valorem 1% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "1"
          - cell "SG"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell "1%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /2 SG US No Ad Valorem 5% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "2"
          - cell "SG"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell "5%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /3 MY SG Yes Ad Valorem 0% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "3"
          - cell "MY"
          - cell "SG"
          - cell "Yes"
          - cell "Ad Valorem"
          - cell "0%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /4 CN US No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "4"
          - cell "CN"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /5 US CN No Ad Valorem \\d+\\.\\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "5"
          - cell "US"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+\\.\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ tariffs/
    - button "Previous" [disabled]
    - button "Next"
    `);

  await page.getByRole('button', { name: 'Next' }).click();

  await expect(page.getByRole('main')).toMatchAriaSnapshot(`
      - button "Tariffs"
      - button "Product Categories"
      - button "Create"
      - table:
        - rowgroup:
          - row "Tariff Id ▲ Importing Country Exporting Country Preferential Tariff Tariff Type Tariff Rate Effective Date Expiry Date":
            - cell "Tariff Id ▲"
            - cell "Importing Country"
            - cell "Exporting Country"
            - cell "Preferential Tariff"
            - cell "Tariff Type"
            - cell "Tariff Rate"
            - cell "Effective Date"
            - cell "Expiry Date"
            - cell
        - rowgroup:
          - row /6 DE US No Ad Valorem 0% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "6"
            - cell "DE"
            - cell "US"
            - cell "No"
            - cell "Ad Valorem"
            - cell "0%"
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /7 JP KR No Ad Valorem 0% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "7"
            - cell "JP"
            - cell "KR"
            - cell "No"
            - cell "Ad Valorem"
            - cell "0%"
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /8 IN CN No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "8"
            - cell "IN"
            - cell "CN"
            - cell "No"
            - cell "Ad Valorem"
            - cell /\\d+%/
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /9 SG CN No Specific \\$3 \\/ 1 kg \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "9"
            - cell "SG"
            - cell "CN"
            - cell "No"
            - cell "Specific"
            - cell "$3 / 1 kg"
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /\\d+ SG US No Specific \\$5 \\/ 1 kg \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell /\\d+/
            - cell "SG"
            - cell "US"
            - cell "No"
            - cell "Specific"
            - cell "$5 / 1 kg"
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
      - text: /Showing 6-\\d+ of \\d+ tariffs/
      - button "Previous"
      - button "Next"
      `);
  await page.getByRole('button', { name: 'Previous' }).click();
  await expect(page.getByRole('main')).toMatchAriaSnapshot(`
      - button "Tariffs"
      - button "Product Categories"
      - button "Create"
      - table:
        - rowgroup:
          - row "Tariff Id ▲ Importing Country Exporting Country Preferential Tariff Tariff Type Tariff Rate Effective Date Expiry Date":
            - cell "Tariff Id ▲"
            - cell "Importing Country"
            - cell "Exporting Country"
            - cell "Preferential Tariff"
            - cell "Tariff Type"
            - cell "Tariff Rate"
            - cell "Effective Date"
            - cell "Expiry Date"
            - cell
        - rowgroup:
          - row /1 SG CN No Ad Valorem 1% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "1"
            - cell "SG"
            - cell "CN"
            - cell "No"
            - cell "Ad Valorem"
            - cell "1%"
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /2 SG US No Ad Valorem 5% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "2"
            - cell "SG"
            - cell "US"
            - cell "No"
            - cell "Ad Valorem"
            - cell "5%"
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /3 MY SG Yes Ad Valorem 0% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "3"
            - cell "MY"
            - cell "SG"
            - cell "Yes"
            - cell "Ad Valorem"
            - cell "0%"
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /4 CN US No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "4"
            - cell "CN"
            - cell "US"
            - cell "No"
            - cell "Ad Valorem"
            - cell /\\d+%/
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
          - row /5 US CN No Ad Valorem \\d+\\.\\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
            - cell "5"
            - cell "US"
            - cell "CN"
            - cell "No"
            - cell "Ad Valorem"
            - cell /\\d+\\.\\d+%/
            - cell /\\d+-\\d+-\\d+/
            - cell /\\d+-\\d+-\\d+/
            - cell "⋮":
              - button "⋮"
      - text: /Showing 1-5 of \\d+ tariffs/
      - button "Previous" [disabled]
      - button "Next"
      `);
})


test('admin tariff sorting', async ({ page }) => {

  await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Tariff Id ▲ Importing Country Exporting Country Preferential Tariff Tariff Type Tariff Rate Effective Date Expiry Date":
          - cell "Tariff Id ▲"
          - cell "Importing Country"
          - cell "Exporting Country"
          - cell "Preferential Tariff"
          - cell "Tariff Type"
          - cell "Tariff Rate"
          - cell "Effective Date"
          - cell "Expiry Date"
          - cell
      - rowgroup:
        - row /1 SG CN No Ad Valorem 1% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "1"
          - cell "SG"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell "1%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /2 SG US No Ad Valorem 5% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "2"
          - cell "SG"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell "5%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /3 MY SG Yes Ad Valorem 0% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "3"
          - cell "MY"
          - cell "SG"
          - cell "Yes"
          - cell "Ad Valorem"
          - cell "0%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /4 CN US No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "4"
          - cell "CN"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /5 US CN No Ad Valorem \\d+\\.\\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "5"
          - cell "US"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+\\.\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ tariffs/
    - button "Previous" [disabled]
    - button "Next"
    `);
  await page.getByRole('cell', { name: 'Tariff Id ▲' }).click();
  await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Tariff Id ▼ Importing Country Exporting Country Preferential Tariff Tariff Type Tariff Rate Effective Date Expiry Date":
          - cell "Tariff Id ▼"
          - cell "Importing Country"
          - cell "Exporting Country"
          - cell "Preferential Tariff"
          - cell "Tariff Type"
          - cell "Tariff Rate"
          - cell "Effective Date"
          - cell "Expiry Date"
          - cell
      - rowgroup:
        - row /\\d+ AU MY Yes Ad Valorem 2\\.5% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell /\\d+/
          - cell "AU"
          - cell "MY"
          - cell "Yes"
          - cell "Ad Valorem"
          - cell "2.5%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /\\d+ MY CN Yes Ad Valorem 5% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell /\\d+/
          - cell "MY"
          - cell "CN"
          - cell "Yes"
          - cell "Ad Valorem"
          - cell "5%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /\\d+ AU CN No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell /\\d+/
          - cell "AU"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /\\d+ AU SG No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell /\\d+/
          - cell "AU"
          - cell "SG"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /\\d+ IN CN No Specific \\$2 \\/ \\d+ g \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell /\\d+/
          - cell "IN"
          - cell "CN"
          - cell "No"
          - cell "Specific"
          - cell /\\$2 \\/ \\d+ g/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ tariffs/
    - button "Previous" [disabled]
    - button "Next"
    `);
  await page.getByRole('cell', { name: 'Tariff Id ▼' }).click();
  await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Tariff Id ▲ Importing Country Exporting Country Preferential Tariff Tariff Type Tariff Rate Effective Date Expiry Date":
          - cell "Tariff Id ▲"
          - cell "Importing Country"
          - cell "Exporting Country"
          - cell "Preferential Tariff"
          - cell "Tariff Type"
          - cell "Tariff Rate"
          - cell "Effective Date"
          - cell "Expiry Date"
          - cell
      - rowgroup:
        - row /1 SG CN No Ad Valorem 1% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "1"
          - cell "SG"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell "1%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /2 SG US No Ad Valorem 5% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "2"
          - cell "SG"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell "5%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /3 MY SG Yes Ad Valorem 0% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "3"
          - cell "MY"
          - cell "SG"
          - cell "Yes"
          - cell "Ad Valorem"
          - cell "0%"
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /4 CN US No Ad Valorem \\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "4"
          - cell "CN"
          - cell "US"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
        - row /5 US CN No Ad Valorem \\d+\\.\\d+% \\d+-\\d+-\\d+ \\d+-\\d+-\\d+ ⋮/:
          - cell "5"
          - cell "US"
          - cell "CN"
          - cell "No"
          - cell "Ad Valorem"
          - cell /\\d+\\.\\d+%/
          - cell /\\d+-\\d+-\\d+/
          - cell /\\d+-\\d+-\\d+/
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ tariffs/
    - button "Previous" [disabled]
    - button "Next"
    `);
})


test('admin get individual tariff', async ({ page }) => {

  await page.getByRole('row', { name: '1 SG CN No Ad Valorem 1% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'View' }).click();
  await page.getByText('Tariff ID 1').click();
  await page.getByRole('button', { name: 'Close', exact: true }).click();
})

test('admin create tariff ad_valorem successful', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('ad_valorem');
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('010');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Tariff rate created successfully! (ID:');

})

test('admin create tariff specific successful', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('specific');
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).fill('010');
  await page.getByRole('spinbutton', { name: 'Quantity' }).click();
  await page.getByRole('spinbutton', { name: 'Quantity' }).fill('2');
  await page.getByLabel('Unit').selectOption('kg');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Tariff rate created successfully! (ID:');

})

test('admin create tariff missing import country', async ({ page }) => {
  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Exporting Country Select' }).locator('span').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('ad_valorem');
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('010');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Please select an importing country');
})

test('admin create tariff missing export country', async ({ page }) => {
  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('ad_valorem');
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('010');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Please select an exporting country');
})

test('admin create tariff import and export country is the same', async ({ page }) => {
  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(AU) Australia').nth(1).click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('ad_valorem');
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('010');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Importing and Exporting country must not be the same');
})

test('admin create tariff missing hscode', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  // await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('ad_valorem');
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('010');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Please select a product category (HSCode)');

})

test('admin create tariff missing tariff type', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  // await page.getByLabel('Tariff Type').selectOption('ad_valorem');
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('010');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Tariff Type must not be blank');

})

test('admin create tariff, tariff rate invalid number', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('ad_valorem');
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('999999');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Tariff Rate can only be from 0 to 999.9999');

})

test('admin create tariff missing quantity', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('specific');
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).fill('010');
  // await page.getByRole('spinbutton', { name: 'Quantity' }).click();
  // await page.getByRole('spinbutton', { name: 'Quantity' }).fill('2');
  await page.getByLabel('Unit').selectOption('kg');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Please insert the quantity');

})

test('admin create tariff missing unit', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('specific');
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).fill('010');
  await page.getByRole('spinbutton', { name: 'Quantity' }).click();
  await page.getByRole('spinbutton', { name: 'Quantity' }).fill('2');
  // await page.getByLabel('Unit').selectOption('kg');
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Please select a unit');

})

test('admin create tariff missing effective date', async ({ page }) => {

  await page.getByRole('button', { name: 'Create' }).click();
  await page.locator('form div').filter({ hasText: 'Importing Country Select' }).locator('span').click();
  await page.getByText('(AU) Australia').click();
  await page.getByText('Select country').click();
  await page.getByText('(BR) Brazil').click();
  await page.getByLabel('(HSCode) Product Category Name').selectOption('847141');
  await page.getByLabel('Tariff Type').selectOption('specific');
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Price per' }).fill('010');
  await page.getByRole('spinbutton', { name: 'Quantity' }).click();
  await page.getByRole('spinbutton', { name: 'Quantity' }).fill('2');
  await page.getByLabel('Unit').selectOption('kg');
  // await page.getByRole('textbox', { name: 'Effective Date' }).fill('2025-10-30');
  await page.getByRole('textbox', { name: 'Expiry Date' }).fill('2027-12-03');
  await page.getByLabel('Preferential Tariff').selectOption('true');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('html')).toContainText('Please select an effective date');

})

test('admin edit tariff successful', async ({ page }) => {
  await page.getByRole('row', { name: '2 SG US No Ad Valorem 5% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Edit' }).click();
  await page.getByText('(SG) Singapore').click();
  await page.getByText('(AU) Australia').click();
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.getByRole('main')).toContainText('Successfully updated Tariff Rate');
})

test('admin edit tariff import and export country is the same', async ({ page }) => {
  await page.getByRole('row', { name: '1 SG CN No Ad Valorem 1% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Edit' }).click();
  await page.locator('div').filter({ hasText: /^\(SG\) Singapore$/ }).click();
  await page.locator('form div').filter({ hasText: '(BR) Brazil' }).nth(4).click();
  await page.locator('div').filter({ hasText: /^\(CN\) China$/ }).click();
  await page.locator('.max-h-60 > div:nth-child(2)').click();
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('form')).toContainText('Importing and Exporting country must not be the same');

})

test('admin edit tariff, tariff rate invalid number', async ({ page }) => {
  await page.getByRole('row', { name: '1 SG CN No Ad Valorem 1% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Edit' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).click();
  await page.getByRole('spinbutton', { name: 'Tariff Rate' }).fill('999999');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('form')).toContainText('Tariff Rate can only be from 0 to 999.9999');
})

test('admin edit tariff missing quantity', async ({ page }) => {
  await page.getByRole('row', { name: '1 SG CN No Ad Valorem 1% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Edit' }).click();
  await page.getByLabel('Tariff Type').selectOption('specific');
  await page.getByRole('spinbutton', { name: 'Quantity' }).click();
  await page.getByRole('spinbutton', { name: 'Quantity' }).fill('');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('form')).toContainText('Please insert the quantity');

})

test('admin edit tariff missing unit', async ({ page }) => {
  await page.getByRole('row', { name: '1 SG CN No Ad Valorem 1% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Edit' }).click();
  await page.getByLabel('Tariff Type').selectOption('specific');
  await page.getByRole('spinbutton', { name: 'Quantity' }).click();
  await page.getByRole('spinbutton', { name: 'Quantity' }).fill('10');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('form')).toContainText('Please select a unit');
})

test('admin edit tariff missing effective date', async ({ page }) => {
  await page.getByRole('row', { name: '1 SG CN No Ad Valorem 1% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Edit' }).click();
  await page.getByRole('textbox', { name: 'Effective Date' }).fill('');
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.locator('form')).toContainText('Please select an effective date');
})

test('admin delete tariff', async ({ page }) => {

  await page.getByRole('row', { name: '1 SG CN No Ad Valorem 1% 2024' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Delete' }).click();
  await expect(page.getByRole('main')).toContainText('Successfully deleted Tariff Rate');
})