import { get } from 'svelte/store';
import { beforeEach, describe, expect, it } from 'vitest';
// Import store to increase coverage
import { AlertMessages, alertStore, showAlert } from './alertStore.js';

describe('alertStore - Store Logic', () => {
  beforeEach(() => {
    // Reset store to initial state before each test
    showAlert.clear();
  });

  it('has valid store structure', () => {
    // Test that the store can be imported without errors
    expect(alertStore).toBeDefined();
    expect(showAlert).toBeDefined();
    expect(AlertMessages).toBeDefined();
  });

  it('showAlert supports all required alert types', () => {
    // Test that showAlert object has methods for all supported alert types
    // This validates the actual implementation, not a local array
    const supportedTypes = ['success', 'error', 'warning', 'info'];
    
    supportedTypes.forEach((type) => {
      expect(showAlert).toHaveProperty(type);
      expect(typeof showAlert[type as keyof typeof showAlert]).toBe('function');
    });
    
    // Verify that calling these methods sets the correct type in the store
    supportedTypes.forEach((type) => {
      showAlert[type as keyof typeof showAlert]('Test message');
      const storeValue = get(alertStore);
      expect(storeValue.type).toBe(type);
      showAlert.clear();
    });
  });

  it('defines alert message constants', () => {
    expect(AlertMessages.LOGIN_SUCCESS).toBeDefined();
    expect(AlertMessages.REGISTRATION_SUCCESS).toBeDefined();
    expect(AlertMessages.LOGOUT_SUCCESS).toBeDefined();
    expect(AlertMessages.SAVE_SUCCESS).toBeDefined();
    expect(AlertMessages.DELETE_SUCCESS).toBeDefined();
    expect(AlertMessages.CREATE_SUCCESS).toBeDefined();
    expect(AlertMessages.UPDATE_SUCCESS).toBeDefined();
    expect(AlertMessages.LOGIN_ERROR).toBeDefined();
    expect(AlertMessages.REGISTRATION_ERROR).toBeDefined();
    expect(AlertMessages.SESSION_EXPIRED).toBeDefined();
    expect(AlertMessages.NETWORK_ERROR).toBeDefined();
    expect(AlertMessages.VALIDATION_ERROR).toBeDefined();
    expect(AlertMessages.UNAUTHORIZED).toBeDefined();
    expect(AlertMessages.NOT_FOUND).toBeDefined();
    expect(AlertMessages.GENERIC_ERROR).toBeDefined();
    expect(AlertMessages.UNSAVED_CHANGES).toBeDefined();
    expect(AlertMessages.DELETE_CONFIRMATION).toBeDefined();
    expect(AlertMessages.SESSION_WARNING).toBeDefined();
    expect(AlertMessages.LOADING).toBeDefined();
    expect(AlertMessages.PROCESSING).toBeDefined();
    expect(AlertMessages.SAVED).toBeDefined();
    expect(AlertMessages.UPDATED).toBeDefined();
  });

  it('showAlert has all required methods', () => {
    expect(typeof showAlert.success).toBe('function');
    expect(typeof showAlert.error).toBe('function');
    expect(typeof showAlert.warning).toBe('function');
    expect(typeof showAlert.info).toBe('function');
    expect(typeof showAlert.clear).toBe('function');
  });

  it('showAlert.success sets alert with success type', () => {
    showAlert.success('Test success message');
    const storeValue = get(alertStore);
    expect(storeValue.show).toBe(true);
    expect(storeValue.type).toBe('success');
    expect(storeValue.message).toBe('Test success message');
  });

  it('showAlert.error sets alert with error type', () => {
    showAlert.error('Test error message');
    const storeValue = get(alertStore);
    expect(storeValue.show).toBe(true);
    expect(storeValue.type).toBe('error');
    expect(storeValue.message).toBe('Test error message');
  });

  it('showAlert.warning sets alert with warning type', () => {
    showAlert.warning('Test warning message');
    const storeValue = get(alertStore);
    expect(storeValue.show).toBe(true);
    expect(storeValue.type).toBe('warning');
    expect(storeValue.message).toBe('Test warning message');
  });

  it('showAlert.info sets alert with info type', () => {
    showAlert.info('Test info message');
    const storeValue = get(alertStore);
    expect(storeValue.show).toBe(true);
    expect(storeValue.type).toBe('info');
    expect(storeValue.message).toBe('Test info message');
  });

  it('showAlert.clear resets alert store', () => {
    showAlert.success('Test message');
    showAlert.clear();
    const storeValue = get(alertStore);
    expect(storeValue.show).toBe(false);
    expect(storeValue.type).toBe('info');
    expect(storeValue.message).toBe('');
  });
});

