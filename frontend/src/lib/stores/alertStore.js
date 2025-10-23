import { writable } from 'svelte/store';

// Alert store for global alert management
export const alertStore = writable({
	show: false,
	type: 'info', // 'success', 'error', 'warning', 'info'
	message: ''
});

// Helper functions for different alert types
export const showAlert = {
	success: (message) => {
		alertStore.set({
			show: true,
			type: 'success',
			message: message
		});
	},
	
	error: (message) => {
		alertStore.set({
			show: true,
			type: 'error',
			message: message
		});
	},
	
	warning: (message) => {
		alertStore.set({
			show: true,
			type: 'warning',
			message: message
		});
	},
	
	info: (message) => {
		alertStore.set({
			show: true,
			type: 'info',
			message: message
		});
	},
	
	// Clear the current alert
	clear: () => {
		alertStore.set({
			show: false,
			type: 'info',
			message: ''
		});
	}
};

// Common alert messages for consistency
export const AlertMessages = {
	// Success messages
	LOGIN_SUCCESS: 'Login successful! Redirecting to dashboard...',
	REGISTRATION_SUCCESS: 'Registration successful! Redirecting to login...',
	LOGOUT_SUCCESS: 'Logged out successfully',
	SAVE_SUCCESS: 'Changes saved successfully',
	DELETE_SUCCESS: 'Item deleted successfully',
	CREATE_SUCCESS: 'Item created successfully',
	UPDATE_SUCCESS: 'Item updated successfully',
	
	// Error messages
	LOGIN_ERROR: 'Login failed. Please check your credentials and try again.',
	REGISTRATION_ERROR: 'Registration failed. Please try again.',
	SESSION_EXPIRED: 'Your session has expired. Please log in again.',
	NETWORK_ERROR: 'Network error. Please check your connection and try again.',
	VALIDATION_ERROR: 'Please check your input and try again.',
	UNAUTHORIZED: 'You are not authorized to perform this action.',
	NOT_FOUND: 'The requested resource was not found.',
	GENERIC_ERROR: 'An unexpected error occurred. Please try again.',
	
	// Warning messages
	UNSAVED_CHANGES: 'You have unsaved changes. Are you sure you want to leave?',
	DELETE_CONFIRMATION: 'Are you sure you want to delete this item?',
	SESSION_WARNING: 'Your session will expire soon. Please save your work.',
	
	// Info messages
	LOADING: 'Loading...',
	PROCESSING: 'Processing your request...',
	SAVED: 'Your changes have been saved.',
	UPDATED: 'Information updated successfully.'
};
