import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
  await page.goto('/register'); 
  await page.getByRole('textbox', { name: 'First Name' }).waitFor({ state: 'visible' });
  await page.getByRole('textbox', { name: 'Last Name' }).waitFor({ state: 'visible' });
  await page.getByRole('textbox', { name: 'Username' }).waitFor({ state: 'visible' });
  await page.getByRole('textbox', { name: 'Email' }).waitFor({ state: 'visible' });
  await page.getByRole('textbox', { name: 'Password', exact: true }).waitFor({ state: 'visible' });
  await page.getByRole('textbox', { name: 'Confirm Password' }).waitFor({ state: 'visible' });
  await page.getByRole('button', { name: 'Register' }).waitFor({ state: 'visible' });
  await page.getByRole('link', { name: '← Back to Login' }).waitFor({ state: 'visible' });
});

test('register successful', async ({ page }) => {
  await page.getByRole('textbox', { name: 'First Name' }).click();
  await page.getByRole('textbox', { name: 'First Name' }).fill('test');
  await page.getByRole('textbox', { name: 'Last Name' }).click();
  await page.getByRole('textbox', { name: 'Last Name' }).fill('test2');
  await page.getByRole('textbox', { name: 'Username' }).click();
  await page.getByRole('textbox', { name: 'Username' }).fill('test3');
  await page.getByRole('textbox', { name: 'Email' }).click();
  await page.getByRole('textbox', { name: 'Email' }).fill('test@test.com');
  await page.getByRole('textbox', { name: 'Password', exact: true }).click();
  await page.getByRole('textbox', { name: 'Password', exact: true }).fill('testTEST@123');
  await page.getByRole('textbox', { name: 'Confirm Password' }).click();
  await page.getByRole('textbox', { name: 'Confirm Password' }).fill('testTEST@123');
  await page.getByRole('button', { name: 'Register' }).click();
  await expect(page.getByRole('main')).toContainText('Account created successfully');
  await page.goto('/login');
});

test('register all empty fields', async ({ page }) => {
  await page.getByRole('button', { name: 'Register' }).click();
  // does not work as the for input has "required" which is blocking it
  // hence just check that its still on the correct page  
  await expect(page).toHaveURL('/register');
});

test('register invalid email', async ({ page }) => {
  await page.getByRole('textbox', { name: 'First Name' }).click();
  await page.getByRole('textbox', { name: 'First Name' }).fill('test');
  await page.getByRole('textbox', { name: 'Last Name' }).click();
  await page.getByRole('textbox', { name: 'Last Name' }).fill('test2');
  await page.getByRole('textbox', { name: 'Last Name' }).fill('test3');
  await page.getByRole('textbox', { name: 'Username' }).click();
  await page.getByRole('textbox', { name: 'Username' }).fill('test3');
  await page.getByRole('textbox', { name: 'Email' }).click();
  await page.getByRole('textbox', { name: 'Email' }).fill('test@test');
  await page.getByRole('textbox', { name: 'Password', exact: true }).click();
  await page.getByRole('textbox', { name: 'Password', exact: true }).fill('testTEST@123');
  await page.getByRole('textbox', { name: 'Confirm Password' }).click();
  await page.getByRole('textbox', { name: 'Confirm Password' }).fill('testTEST@123');
  await page.getByRole('button', { name: 'Register' }).click();
  await expect(page.getByRole('main')).toContainText('Please enter a valid email address (e.g., user@example.com)');
});

test('register with used email', async ({ page }) => {
  await page.getByRole('textbox', { name: 'First Name' }).click();
  await page.getByRole('textbox', { name: 'First Name' }).fill('test');
  await page.getByRole('textbox', { name: 'Last Name' }).click();
  await page.getByRole('textbox', { name: 'Last Name' }).fill('test2');
  await page.getByRole('textbox', { name: 'Username' }).click();
  await page.getByRole('textbox', { name: 'Username' }).fill('test3');
  await page.getByRole('textbox', { name: 'Email' }).click();
  await page.getByRole('textbox', { name: 'Email' }).fill('test@test.com');
  await page.getByRole('textbox', { name: 'Password', exact: true }).click();
  await page.getByRole('textbox', { name: 'Password', exact: true }).fill('testTEST@123');
  await page.getByRole('textbox', { name: 'Confirm Password' }).click();
  await page.getByRole('textbox', { name: 'Confirm Password' }).fill('testTEST@123');
  await page.getByRole('button', { name: 'Register' }).click();
  await expect(page.getByRole('main')).toContainText('Email already exists: test@test.com');
});

test('register different password', async ({ page }) => {
  await page.getByRole('textbox', { name: 'First Name' }).click();
  await page.getByRole('textbox', { name: 'First Name' }).fill('testt');
  await page.getByRole('textbox', { name: 'Last Name' }).click();
  await page.getByRole('textbox', { name: 'Last Name' }).fill('test2');
  await page.getByRole('textbox', { name: 'Username' }).click();
  await page.getByRole('textbox', { name: 'Username' }).fill('test3');
  await page.getByRole('textbox', { name: 'Email' }).click();
  await page.getByRole('textbox', { name: 'Email' }).fill('test@test.com');
  await page.getByRole('textbox', { name: 'Password', exact: true }).click();
  await page.getByRole('textbox', { name: 'Password', exact: true }).fill('testTEST@123');
  await page.getByRole('textbox', { name: 'Confirm Password' }).click();
  await page.getByRole('textbox', { name: 'Confirm Password' }).fill('testTEST@1234');
  await page.getByRole('button', { name: 'Register' }).click();
  await expect(page.getByRole('main')).toContainText('The passwords provided are different.');

});

test('register password does not fulfil requirements', async ({ page }) => {
  await page.getByRole('textbox', { name: 'First Name' }).click();
  await page.getByRole('textbox', { name: 'First Name' }).fill('testt');
  await page.getByRole('textbox', { name: 'Last Name' }).click();
  await page.getByRole('textbox', { name: 'Last Name' }).fill('test2');
  await page.getByRole('textbox', { name: 'Username' }).click();
  await page.getByRole('textbox', { name: 'Username' }).fill('test3');
  await page.getByRole('textbox', { name: 'Email' }).click();
  await page.getByRole('textbox', { name: 'Email' }).fill('test@test.com');
  await page.getByRole('textbox', { name: 'Password', exact: true }).click();
  await page.getByRole('textbox', { name: 'Password', exact: true }).fill('testTEST123');
  await page.getByRole('textbox', { name: 'Confirm Password' }).click();
  await page.getByRole('textbox', { name: 'Confirm Password' }).fill('testTEST123');
  await page.getByRole('button', { name: 'Register' }).click();
  await expect(page.getByRole('main')).toContainText('The password need to fulfil all of the requirements.');
});

test('register go back to login', async ({ page }) => {
  await page.getByRole('link', { name: '← Back to Login' }).click();
  await expect(page).toHaveURL('/login');
});