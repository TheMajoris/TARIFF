# TARIFF

## Overview

TARIFF is an application designed to help international traders navigate current tariff conditions in the global market. This can be used for simulating the costs incurred by tariffs, finding optimal routes to minimise those costs and give real-time updated information on trade tariffs at present.

## Features

- **User Authentication**: Secure login system for users and admins
- **Tariff Calculation**: Calculate the costs incurred for different products across countries
- **Trade Route Optimization**: Find the most cost-effective shipping routes to minimize expenses incurred
- **Real-time Updates**: Live tariff and trade policy news
- **Admin Dashboard**: Admin interface for tariff rule management
- **Modern UI**: Built with Svelte, TailwindCSS, and daisyUI for responsive design
- **Robust Backend**: Scalable Spring Boot REST API
- **Secure Database**: PostgreSQL database with proper data modeling

## Tech Stack

### Frontend
- **Framework**: SvelteKit
- **Styling**: TailwindCSS + daisyUI
- **Language**: TypeScript
- **Testing**: Vitest (unit), Playwright (E2E)

### Backend
- **Framework**: Spring Boot
- **Language**: Java
- **Database**: PostgreSQL
- **Build Tool**: Gradle
- **Testing**: JUnit

## Project Structure

*Blank for now as this is subject to change

## Getting Started

### Prerequisites
- **Node.js** (v20.19.0 or higher)
- **Java** (JDK 17)
- **PostgreSQL** (for production database)

### **Clone Repo**
```
git clone https://github.com/TheMajoris/CS203 
```
### **Backend Setup**
1. **Navigate to the backend directory:**
```bash
cd core
```

2. **Setup local instance of DB**
```bash
docker compose up --build -d
or
docker compose up -f
```

3. **Setup the application:**
```bash
bash gradlew :bootRun
```

4. **[Optional] Run Tests:**
```bash
bash gradlew test
```

### **Frontend Setup**
1. **Navigate to the frontend directory:**
```bash
cd frontend
```

2. **Install dependencies:**
```bash
npm install
npx playwright install
```

3. **Start development server:**
```bash
npm run dev -- --open
```

4. **Run Tests:**
```bash
# Unit tests
npm run test:unit

#E2E tests
npm run test:e2e
```

### **Database Setup**
Please ensure that you have completed the **backend setup** first.

```bash
#Setup an admin user in the database
sudo -u postgres psql -c "CREATE USER admin WITH PASSWORD 'admin123';"
sudo -u postgres psql -c "CREATE DATABASE tariff_db OWNER admin;"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE tariff_db TO admin;"
sudo docker exec -it core-db-1 psql -U admin -d tariff_db
```
You guys should see something like this
<img width="800" height="651" alt="image" src="https://github.com/user-attachments/assets/aa80d9c1-1bbe-4ec6-9a17-ecbd176704db" />



6. **Tear Down**
```bash
docker compose down -v
```

## API Documentation

API documentation is available in [docs/api.json](docs/api.json).

## Development Workflow

- **Issue Tracking**: Issues are tracked in JIRA
- **Branch Naming**: Relevant 
- **Pull Requests**: Use the provided [PR template](.github/pull_request_template.md)
- **CI/CD**: Automated testing via GitHub Actions

## Testing

- **Backend**: Unit tests with JUnit
- **Frontend**: Unit tests with Vitest, E2E tests with Playwright
- **CI**: Automated testing on pull requests
