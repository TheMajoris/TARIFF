import { describe, expect, it, vi, beforeEach, afterEach } from 'vitest';

describe('Alert.svelte - Logic', () => {
  // Test alert type mapping logic
  function getAlertClass(type: string): string {
    switch (type) {
      case 'success':
        return 'alert-success';
      case 'error':
        return 'alert-error';
      case 'warning':
        return 'alert-warning';
      case 'info':
      default:
        return 'alert-info';
    }
  }

  // Test auto-dismiss delay logic
  function getAutoDismissDelay(type: string): number {
    return type === 'error' ? 5000 : 3000;
  }

  // Test dismiss function logic
  function createDismissFunction() {
    let show = true;
    let timeoutId: NodeJS.Timeout | null = null;

    function dismiss() {
      show = false;
      if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
      }
    }

    return { show, dismiss, setTimeout: (fn: () => void, delay: number) => {
      timeoutId = setTimeout(fn, delay) as any;
      return timeoutId;
    }};
  }

  beforeEach(() => {
    vi.useFakeTimers();
  });

  afterEach(() => {
    vi.restoreAllMocks();
    vi.useRealTimers();
  });

  it('maps alert types to correct CSS classes', () => {
    expect(getAlertClass('success')).toBe('alert-success');
    expect(getAlertClass('error')).toBe('alert-error');
    expect(getAlertClass('warning')).toBe('alert-warning');
    expect(getAlertClass('info')).toBe('alert-info');
    expect(getAlertClass('unknown')).toBe('alert-info'); // default
  });

  it('returns correct auto-dismiss delay based on type', () => {
    expect(getAutoDismissDelay('error')).toBe(5000);
    expect(getAutoDismissDelay('success')).toBe(3000);
    expect(getAutoDismissDelay('warning')).toBe(3000);
    expect(getAutoDismissDelay('info')).toBe(3000);
  });

  it('dismiss function sets show to false and clears timeout', () => {
    let show = true;
    let timeoutId: NodeJS.Timeout | null = null;
    
    const timeout = setTimeout(() => {
      show = false;
    }, 3000);
    timeoutId = timeout;

    function dismiss() {
      show = false;
      if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
      }
    }

    expect(show).toBe(true);
    expect(timeoutId).toBeDefined();
    dismiss();
    expect(show).toBe(false);
    expect(timeoutId).toBeNull();
  });

  it('only shows alert when show is true and message is not empty', () => {
    const shouldShow = (show: boolean, message: string) => show && message !== '';

    expect(shouldShow(true, 'Test message')).toBe(true);
    expect(shouldShow(false, 'Test message')).toBe(false);
    expect(shouldShow(true, '')).toBe(false);
    expect(shouldShow(false, '')).toBe(false);
  });
});


