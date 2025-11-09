# TARIFF Frontend

## Overview

TARIFF is a Svelte frontend for simulating tariffs, calculating trade costs, and optimizing routes between countries. Designed for international traders and admins, it offers an interactive interface for tariff management and trade insights.

## Features

- User login for traders and admins
- Tariff calculation for products and countries
- Trade route cost optimization
- Real-time tariff and trade policy news
- Admin interface for tariff rule management
- Modern UI with Svelte, TailwindCSS, and daisyUI

## Getting Started

1. **Clone Repo**
```
git clone https://github.com/your-org/tariff-frontend.git
cd tariff-frontend
```

2. **Install Dependencies**
```
npm install
```

3. **Start Development Server**
```
npm run dev -- --open
```

4. **Build for Production**
```
npm run build
npm run preview
```

## Testing

### Running Tests

Run unit tests:
```bash
npm run test:unit
```

Run all tests (unit + e2e):
```bash
npm test
```

### Code Coverage

Generate HTML coverage report:
```bash
npm run test:coverage
```

The coverage report will be generated in the `coverage/` directory. Open `coverage/index.html` in your browser to view the detailed coverage report.

The coverage report includes:
- Overall coverage statistics
- Per-file coverage details
- Statement, branch, function, and line coverage
- Color-coded visualization

## Contributing
- View feature requests on JIRA.
- Follow branch naming and commit message conventions such as titling the respective JIRA key.
- Issues are tracked in Jira.