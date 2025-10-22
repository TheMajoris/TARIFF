import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async ({ locals, cookies }) => {
	// Check for server-side authentication
	// Note: This is a simplified example - adapt to your auth model
	const jwt = cookies.get('jwt');
	const role = cookies.get('role');
	
	// If we have server-side auth data, validate it
	if (jwt && role) {
		if (role !== 'ROLE_ADMIN') {
			throw error(403, 'Forbidden');
		}
		return {
			user: { jwt, role }
		};
	}
	
	// If no server-side auth, let client-side handle it
	// This prevents SSR from rendering admin content
	return {
		user: null
	};
};
