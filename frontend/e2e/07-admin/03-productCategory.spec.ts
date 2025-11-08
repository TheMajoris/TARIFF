import { expect, test } from '@playwright/test';
import { adminPage, loginAdmin } from '../pageload';

test.beforeEach(async ({ page }) => {
    await loginAdmin({ page });
    await adminPage({ page });

    await page.getByRole('button', { name: 'Product Categories' }).click();
});

test('admin product categories loaded', async ({ page }) => {

    await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Product Category Id ▲ HSCode Name Is Active?":
          - cell "Product Category Id ▲"
          - cell "HSCode"
          - cell "Name"
          - cell "Is Active?"
          - cell
      - rowgroup:
        - row /1 \\d+ Smartphones Yes ⋮/:
          - cell "1"
          - cell /\\d+/
          - cell "Smartphones"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /2 \\d+ Microprocessors Yes ⋮/:
          - cell "2"
          - cell /\\d+/
          - cell "Microprocessors"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /3 \\d+ Memory Chips Yes ⋮/:
          - cell "3"
          - cell /\\d+/
          - cell "Memory Chips"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /4 \\d+ Laptops Yes ⋮/:
          - cell "4"
          - cell /\\d+/
          - cell "Laptops"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /5 \\d+ Desktop Computers Yes ⋮/:
          - cell "5"
          - cell /\\d+/
          - cell "Desktop Computers"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ categories/
    - button "Previous" [disabled]
    - button "Next"
    `);
})

test('admin product categories pagination', async ({ page }) => {

    await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Product Category Id ▲ HSCode Name Is Active?":
          - cell "Product Category Id ▲"
          - cell "HSCode"
          - cell "Name"
          - cell "Is Active?"
          - cell
      - rowgroup:
        - row /1 \\d+ Smartphones Yes ⋮/:
          - cell "1"
          - cell /\\d+/
          - cell "Smartphones"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /2 \\d+ Microprocessors Yes ⋮/:
          - cell "2"
          - cell /\\d+/
          - cell "Microprocessors"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /3 \\d+ Memory Chips Yes ⋮/:
          - cell "3"
          - cell /\\d+/
          - cell "Memory Chips"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /4 \\d+ Laptops Yes ⋮/:
          - cell "4"
          - cell /\\d+/
          - cell "Laptops"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /5 \\d+ Desktop Computers Yes ⋮/:
          - cell "5"
          - cell /\\d+/
          - cell "Desktop Computers"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ categories/
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
        - row "Product Category Id ▲ HSCode Name Is Active?":
          - cell "Product Category Id ▲"
          - cell "HSCode"
          - cell "Name"
          - cell "Is Active?"
          - cell
      - rowgroup:
        - row /6 \\d+ LED TVs Yes ⋮/:
          - cell "6"
          - cell /\\d+/
          - cell "LED TVs"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /7 \\d+ Semiconductor Diodes Yes ⋮/:
          - cell "7"
          - cell /\\d+/
          - cell "Semiconductor Diodes"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /8 \\d+ LED Components Yes ⋮/:
          - cell "8"
          - cell /\\d+/
          - cell "LED Components"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /9 \\d+ Computer Peripherals Yes ⋮/:
          - cell "9"
          - cell /\\d+/
          - cell "Computer Peripherals"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /\\d+ \\d+ Computer Monitors Yes ⋮/:
          - cell /\\d+/
          - cell /\\d+/
          - cell "Computer Monitors"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
    - text: /Showing 6-\\d+ of \\d+ categories/
    - button "Previous"
    - button "Next" [disabled]
    `);
})

test('admin product category sorting', async ({ page }) => {

    await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Product Category Id ▲ HSCode Name Is Active?":
          - cell "Product Category Id ▲"
          - cell "HSCode"
          - cell "Name"
          - cell "Is Active?"
          - cell
      - rowgroup:
        - row /1 \\d+ Smartphones Yes ⋮/:
          - cell "1"
          - cell /\\d+/
          - cell "Smartphones"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /2 \\d+ Microprocessors Yes ⋮/:
          - cell "2"
          - cell /\\d+/
          - cell "Microprocessors"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /3 \\d+ Memory Chips Yes ⋮/:
          - cell "3"
          - cell /\\d+/
          - cell "Memory Chips"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /4 \\d+ Laptops Yes ⋮/:
          - cell "4"
          - cell /\\d+/
          - cell "Laptops"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /5 \\d+ Desktop Computers Yes ⋮/:
          - cell "5"
          - cell /\\d+/
          - cell "Desktop Computers"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ categories/
    - button "Previous" [disabled]
    - button "Next"
    `);
    await page.getByRole('cell', { name: 'Product Category Id ▲' }).click();
    await expect(page.getByRole('main')).toMatchAriaSnapshot(`
    - button "Tariffs"
    - button "Product Categories"
    - button "Create"
    - table:
      - rowgroup:
        - row "Product Category Id ▼ HSCode Name Is Active?":
          - cell "Product Category Id ▼"
          - cell "HSCode"
          - cell "Name"
          - cell "Is Active?"
          - cell
      - rowgroup:
        - row /\\d+ \\d+ Computer Monitors Yes ⋮/:
          - cell /\\d+/
          - cell /\\d+/
          - cell "Computer Monitors"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /9 \\d+ Computer Peripherals Yes ⋮/:
          - cell "9"
          - cell /\\d+/
          - cell "Computer Peripherals"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /8 \\d+ LED Components Yes ⋮/:
          - cell "8"
          - cell /\\d+/
          - cell "LED Components"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /7 \\d+ Semiconductor Diodes Yes ⋮/:
          - cell "7"
          - cell /\\d+/
          - cell "Semiconductor Diodes"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
        - row /6 \\d+ LED TVs Yes ⋮/:
          - cell "6"
          - cell /\\d+/
          - cell "LED TVs"
          - cell "Yes"
          - cell "⋮":
            - button "⋮"
    - text: /Showing 1-5 of \\d+ categories/
    - button "Previous" [disabled]
    - button "Next"
    `);
})

test('admin individual product category', async ({ page }) => {
    await page.getByRole('row', { name: '851713 Smartphones Yes ⋮' }).getByRole('button').click();
    await page.getByRole('button', { name: 'View' }).click();
    await page.getByText('HSCode 851713').click();
    await page.getByRole('button', { name: 'Close', exact: true }).click();
})

test('admin create product category', async ({ page }) => {

    await page.getByRole('button', { name: 'Create' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).fill('123456');
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).fill('product category example');
    await page.getByRole('textbox', { name: 'Description' }).click();
    await page.getByRole('textbox', { name: 'Description' }).fill('test');
    await page.getByLabel('Is Active?').selectOption('true');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('Created product category id: ');
})

test('admin create product category missing hscode', async ({ page }) => {
    await page.getByRole('button', { name: 'Create' }).click();
    // await page.getByRole('textbox', { name: 'HSCode' }).click();
    // await page.getByRole('textbox', { name: 'HSCode' }).fill('123456');
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).fill('product category example');
    await page.getByRole('textbox', { name: 'Description' }).click();
    await page.getByRole('textbox', { name: 'Description' }).fill('test');
    await page.getByLabel('Is Active?').selectOption('true');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('HSCode must be 6 digits long');
})

test('admin create product category missing name', async ({ page }) => {
    await page.getByRole('button', { name: 'Create' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).fill('123456');
    // await page.getByRole('textbox', { name: 'Name' }).click();
    // await page.getByRole('textbox', { name: 'Name' }).fill('product category example');
    await page.getByRole('textbox', { name: 'Description' }).click();
    await page.getByRole('textbox', { name: 'Description' }).fill('test');
    await page.getByLabel('Is Active?').selectOption('true');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('Category Name can only have up to 100 characters and must not be empty');
})

test('admin create product category name exceed 100 characters', async ({ page }) => {
    await page.getByRole('button', { name: 'Create' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).fill('123456');
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).fill('test'.repeat(100));
    await page.getByRole('textbox', { name: 'Description' }).click();
    await page.getByRole('textbox', { name: 'Description' }).fill('test');
    await page.getByLabel('Is Active?').selectOption('true');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('Category Name can only have up to 100 characters and must not be empty');
})


test('admin create product category description exceed 500 characters', async ({ page }) => {
    await page.getByRole('button', { name: 'Create' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).fill('123456');
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).fill('test');
    await page.getByRole('textbox', { name: 'Description' }).click();
    await page.getByRole('textbox', { name: 'Description' }).fill('test'.repeat(200));
    await page.getByLabel('Is Active?').selectOption('true');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('Category Description can only have up to 500 characters');
})

test('admin create product category duplicate hscode', async ({ page }) => {
    await page.getByRole('button', { name: 'Create' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).click();
    await page.getByRole('textbox', { name: 'HSCode' }).fill('123456');
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).fill('product category example');
    await page.getByRole('textbox', { name: 'Description' }).click();
    await page.getByRole('textbox', { name: 'Description' }).fill('test');
    await page.getByLabel('Is Active?').selectOption('true');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('Category code (HSCode) already exists');
})

test('admin edit product category', async ({ page }) => {
    await page.getByRole('row', { name: '851713 Smartphones Yes ⋮' }).getByRole('button').click();
    await page.getByRole('button', { name: 'Edit' }).click();
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).fill('Smartphones1');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('Successfully updated Product category');
})

test('admin edit product category missing name', async ({ page }) => {

    await page.getByRole('row', { name: '854231 Microprocessors Yes ⋮' }).getByRole('button').click();
    await page.getByRole('button', { name: 'Edit' }).click();
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).press('ControlOrMeta+a');
    await page.getByRole('textbox', { name: 'Name' }).fill('');
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.locator('form')).toContainText('Category Name can only have up to 100 characters and must not be empty');
})

test('admin edit product category name exceed 100 characters', async ({ page }) => {
    await page.getByRole('row', { name: '854231 Microprocessors Yes ⋮' }).getByRole('button').click();
    await page.getByRole('button', { name: 'Edit' }).click();
    await page.getByRole('textbox', { name: 'Name' }).click();
    await page.getByRole('textbox', { name: 'Name' }).fill('test'.repeat(100));
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.locator('form')).toContainText('Category Name can only have up to 100 characters and must not be empty');
})


test('admin edit product category description exceed 500 characters', async ({ page }) => {

    await page.getByRole('row', { name: '851713 Smartphones1 Yes ⋮' }).getByRole('button').click();
    await page.getByRole('button', { name: 'Edit' }).click();
    await page.getByRole('textbox', { name: 'Description' }).click();
    await page.getByRole('textbox', { name: 'Description' }).fill('test'.repeat(200));
    await page.getByRole('button', { name: 'Submit' }).click();
    await expect(page.getByRole('main')).toContainText('Category Description can only have up to 500 characters');
})

test('admin delete product category successful (with connected tariff)', async ({ page }) => {
    await page.getByRole('row', { name: '854231 Microprocessors Yes ⋮' }).getByRole('button').click();
    await page.getByRole('button', { name: 'Delete' }).click();
    await expect(page.getByRole('main')).toContainText('Please click delete again to confirm deletion (inclusive of tariffs using it)');
    await page.getByRole('row', { name: '854231 Microprocessors Yes ⋮' }).getByRole('button', { name: 'Delete' }).click();
    await expect(page.getByRole('main')).toContainText('Successfully deleted Product category and all related tariff rates and national tariff lines');
})

test('admin delete product category successful (without connected tariff)', async ({ page }) => {
    await page.getByRole('cell', { name: 'Product Category Id ▲' }).click();
    await page.getByRole('row', { name: '11 123456 product category' }).getByRole('button').click();
    await page.getByRole('button', { name: 'Delete' }).click();
    await expect(page.getByRole('main')).toContainText('Successfully deleted Product category');
})