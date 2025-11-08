import { expect, test } from '@playwright/test';
import { loginPage } from './pageload';

test.beforeEach(async ({ page }) => {
	await loginPage({ page });
});

test('login go to register', async ({ page }) => {
	await page.getByRole('link', { name: 'Register →' }).click();
	await expect(page).toHaveURL('/register');
});

test('login invalid email', async ({ page }) => {
	await page.getByRole('textbox', { name: 'Email' }).click();
	await page.getByRole('textbox', { name: 'Email' }).fill('a@a');
	await page.getByRole('textbox', { name: 'Password' }).click();
	await page.getByRole('textbox', { name: 'Password' }).fill('a');

	await page.getByRole('button', { name: 'Login' }).click();
	await expect(page.getByRole('main')).toContainText('Please enter a valid email address (e.g., user@example.com)');
});

test('login both empty fields', async ({ page }) => {
	await page.getByRole('button', { name: 'Login' }).click();
	// does not work as the for input has "required" which is blocking it
	// await expect(page.getByRole('main')).toContainText('Please fill in both email and password fields to continue');
	// hence just check that its still on the correct page  
	await expect(page).toHaveURL('/login');
});


test('login empty email fields', async ({ page }) => {
	await page.getByRole('textbox', { name: 'Password' }).click();
	await page.getByRole('textbox', { name: 'Password' }).fill('a');
	await page.getByRole('button', { name: 'Login' }).click();
	// does not work as the for input has "required" which is blocking it
	// await expect(page.getByRole('main')).toContainText('Please fill in both email and password fields to continue');
	// hence just check that its still on the correct page  
	await expect(page).toHaveURL('/login');
});


test('login empty password fields', async ({ page }) => {
	await page.getByRole('textbox', { name: 'Email' }).click();
	await page.getByRole('textbox', { name: 'Email' }).fill('a@a.com');
	await page.getByRole('button', { name: 'Login' }).click();
	// does not work as the for input has "required" which is blocking it
	// await expect(page.getByRole('main')).toContainText('Please fill in both email and password fields to continue');
	// hence just check that its still on the correct page  
	await expect(page).toHaveURL('/login');
});

test('login successful', async ({ page }) => {
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
});

test('login wrong credentials', async ({ page }) => {
	await page.getByRole('textbox', { name: 'Email' }).click();
	await page.getByRole('textbox', { name: 'Email' }).fill('admin@cs203.com');
	await page.getByRole('textbox', { name: 'Password' }).click();
	await page.getByRole('textbox', { name: 'Password' }).fill('WrongPassword');
	await page.getByRole('button', { name: 'Login' }).click();
	await expect(page.getByRole('main')).toContainText('Invalid email or password');
});