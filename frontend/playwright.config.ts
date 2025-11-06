import { defineConfig } from '@playwright/test';

export default defineConfig({
	// globalSetup: './e2e/global-setup.js',
	// globalTeardown: './e2e/global-teardown.js',
	workers: 1,
	webServer: {
		command: 'npm run build && npm run preview',
		port: 4173
	},
	testDir: 'e2e'
});
