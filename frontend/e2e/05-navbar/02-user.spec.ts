import { expect, test } from '@playwright/test';
import { loginUser, navBar } from '../pageload.js';

test('sidebar check that admin is not there', async ({ page }) => {

    await loginUser({ page });

    await navBar({ page });
    await expect(page.getByRole('link', { name: 'Admin' })).toBeHidden();
})