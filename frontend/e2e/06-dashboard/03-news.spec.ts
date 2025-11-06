import { expect, test } from '@playwright/test';
import { loginAdmin } from '../pageload';

test('news page exist', async ({ page }) => {
    await loginAdmin({ page });

    // check that news components are there
    await expect(page.getByText('Related News & Updates Stay')).toBeVisible();
    await expect(page.getByRole('heading', { name: 'Related News & Updates' })).toBeVisible();

    await expect(page.getByRole('button', { name: 'Next' })).toBeVisible();

});