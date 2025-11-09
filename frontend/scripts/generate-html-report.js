import { readFileSync, writeFileSync } from 'fs';
import { dirname, join } from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);
const resultsPath = join(__dirname, '../test-results/results.json');
const outputPath = join(__dirname, '../test-results/index.html');

try {
  const results = JSON.parse(readFileSync(resultsPath, 'utf-8'));
  
  const totalTests = results.numTotalTests || 0;
  const passedTests = results.numPassedTests || 0;
  const failedTests = results.numFailedTests || 0;
  const skippedTests = results.numSkippedTests || 0;
  const duration = results.startTime ? (Date.now() - results.startTime) / 1000 : 0;
  
  const successRate = totalTests > 0 ? ((passedTests / totalTests) * 100).toFixed(2) : 0;
  
  const html = `<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vitest Test Report</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: #f5f5f5;
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
        }
        .header .timestamp {
            opacity: 0.9;
            font-size: 0.9em;
        }
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            padding: 30px;
            background: #f9f9f9;
        }
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .stat-card .value {
            font-size: 2.5em;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .stat-card .label {
            color: #666;
            font-size: 0.9em;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .stat-card.passed .value { color: #10b981; }
        .stat-card.failed .value { color: #ef4444; }
        .stat-card.skipped .value { color: #f59e0b; }
        .stat-card.total .value { color: #3b82f6; }
        .stat-card.success-rate .value { color: #667eea; }
        .test-files {
            padding: 30px;
        }
        .test-files h2 {
            margin-bottom: 20px;
            color: #333;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }
        .test-file {
            margin-bottom: 25px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            overflow: hidden;
        }
        .test-file-header {
            background: #f9fafb;
            padding: 15px 20px;
            font-weight: 600;
            color: #374151;
            border-bottom: 1px solid #e5e7eb;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .test-file-header.passed { border-left: 4px solid #10b981; }
        .test-file-header.failed { border-left: 4px solid #ef4444; }
        .test-cases {
            padding: 0;
        }
        .test-case {
            padding: 12px 20px;
            border-bottom: 1px solid #f3f4f6;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .test-case:last-child {
            border-bottom: none;
        }
        .test-case.passed {
            background: #f0fdf4;
        }
        .test-case.failed {
            background: #fef2f2;
        }
        .test-case.skipped {
            background: #fffbeb;
        }
        .test-icon {
            font-size: 1.2em;
        }
        .test-case.passed .test-icon { color: #10b981; }
        .test-case.failed .test-icon { color: #ef4444; }
        .test-case.skipped .test-icon { color: #f59e0b; }
        .test-name {
            flex: 1;
            font-family: 'Monaco', 'Courier New', monospace;
            font-size: 0.9em;
        }
        .test-duration {
            color: #6b7280;
            font-size: 0.85em;
        }
        .error-details {
            margin-top: 10px;
            padding: 15px;
            background: #fee2e2;
            border-left: 4px solid #ef4444;
            border-radius: 4px;
            font-family: 'Monaco', 'Courier New', monospace;
            font-size: 0.85em;
            white-space: pre-wrap;
            color: #991b1b;
        }
        .footer {
            text-align: center;
            padding: 20px;
            color: #6b7280;
            font-size: 0.9em;
            border-top: 1px solid #e5e7eb;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🧪 Vitest Test Report</h1>
            <div class="timestamp">Generated: ${new Date().toLocaleString()}</div>
        </div>
        
        <div class="stats">
            <div class="stat-card total">
                <div class="value">${totalTests}</div>
                <div class="label">Total Tests</div>
            </div>
            <div class="stat-card passed">
                <div class="value">${passedTests}</div>
                <div class="label">Passed</div>
            </div>
            <div class="stat-card failed">
                <div class="value">${failedTests}</div>
                <div class="label">Failed</div>
            </div>
            <div class="stat-card skipped">
                <div class="value">${skippedTests}</div>
                <div class="label">Skipped</div>
            </div>
            <div class="stat-card success-rate">
                <div class="value">${successRate}%</div>
                <div class="label">Success Rate</div>
            </div>
            <div class="stat-card">
                <div class="value">${duration.toFixed(2)}s</div>
                <div class="label">Duration</div>
            </div>
        </div>
        
        <div class="test-files">
            <h2>Test Results</h2>
            ${generateTestFilesHTML(results.testResults || [])}
        </div>
        
        <div class="footer">
            Generated by Vitest Test Reporter
        </div>
    </div>
</body>
</html>`;

  writeFileSync(outputPath, html, 'utf-8');
  console.log(`✅ HTML report generated: ${outputPath}`);
} catch (error) {
  console.error('❌ Error generating HTML report:', error.message);
  process.exit(1);
}

function generateTestFilesHTML(testResults) {
  if (!testResults || testResults.length === 0) {
    return '<p>No test results available.</p>';
  }
  
  return testResults.map(file => {
    const status = file.status || 'unknown';
    const statusClass = status.toLowerCase();
    const fileName = file.name || 'Unknown';
    const relativePath = fileName.split('/').slice(-3).join('/');
    
    const testCases = file.assertionResults || [];
    const passed = testCases.filter(t => t.status === 'passed').length;
    const failed = testCases.filter(t => t.status === 'failed').length;
    const skipped = testCases.filter(t => t.status === 'skipped').length;
    
    return `
      <div class="test-file">
        <div class="test-file-header ${statusClass}">
          <span>${relativePath}</span>
          <span style="font-size: 0.85em; color: #6b7280;">
            ${passed} passed, ${failed} failed, ${skipped} skipped
          </span>
        </div>
        <div class="test-cases">
          ${testCases.map(test => generateTestCaseHTML(test)).join('')}
        </div>
      </div>
    `;
  }).join('');
}

function generateTestCaseHTML(test) {
  const status = test.status || 'unknown';
  const statusClass = status.toLowerCase();
  const icon = status === 'passed' ? '✓' : status === 'failed' ? '✗' : '⊘';
  const duration = test.duration ? `${test.duration}ms` : '';
  
  let errorHTML = '';
  if (status === 'failed' && test.failureMessages && test.failureMessages.length > 0) {
    errorHTML = `<div class="error-details">${test.failureMessages.join('\\n')}</div>`;
  }
  
  return `
    <div class="test-case ${statusClass}">
      <span class="test-icon">${icon}</span>
      <span class="test-name">${test.fullName || test.title || 'Unknown test'}</span>
      ${duration ? `<span class="test-duration">${duration}</span>` : ''}
    </div>
    ${errorHTML}
  `;
}

