# TARIFF

## Overview

TARIFF is an application designed to help international traders navigate current tariff conditions in the global market. This can be used for simulating the costs incurred by tariffs, finding optimal routes to minimise those costs and give real-time updated information on trade tariffs at present.

## Features

- **User Authentication**: Secure login system for users and admins
- **Tariff Calculation**: Calculate the costs incurred for different products across countries
- **Tariff Calculation History**: Save your calculations for future reference
- **Trade Route Optimization**: Find the most cost-effective shipping routes to minimize expenses incurred
- **Real-time Updates**: Live tariff and trade policy news
- **Admin Dashboard**: Admin interface for tariff rule and product category management
- **Settings**: Change the theme of the website from light to dark mode based on user's preference
- **Modern UI**: Built with Svelte, TailwindCSS, and daisyUI for responsive design
- **Robust Backend**: Scalable Spring Boot REST API
- **Secure Database**: PostgreSQL database with proper data modeling

## Tech Stack

### Frontend
- **Framework**: SvelteKit 2.22
- **Language**: TypeScript 5.0
- **Styling**: TailwindCSS 4.1 + daisyUI 5.1
- **Testing**: Vitest 3.2 (unit), Playwright 1.49 (E2E)
- **Build Tool**: Vite 7.0
- **Code Quality**: ESLint, Prettier

### Backend
- **Framework**: Spring Boot 3.5.5
- **Language**: Java 17
- **Database**: PostgreSQL 16
- **ORM**: Spring Data JPA (Hibernate)
- **Build Tool**: Gradle 8.14
- **Testing**: JUnit 5, Spring Boot Test, JaCoCo
- **Security**: Spring Security + OAuth2 + JWT (Nimbus JOSE)
- **API Documentation**: SpringDoc OpenAPI 2.8 + SwaggerUI
- **Object Mapping**: MapStruct 1.6
- **AI Integration**: Spring AI 1.0 (OpenAI)
- **Code Quality**: SonarQube/SonarCloud

### DevOps & Tools
- **Containerization**: Docker
- **CI/CD**: GitHub Actions
- **Code Analysis**: SonarCloud
- **Issue Tracking**: JIRA
- **PR Reviews**: CodeRabbit

## Project Structure

```
TARIFF/
├── core/                          # Backend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cs203/core/
│   │   │   │   ├── controller/    # REST API endpoints
│   │   │   │   ├── service/       # Business logic
│   │   │   │   ├── repository/    # Database access layer
│   │   │   │   ├── model/         # Entity classes
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   ├── config/        # Configuration classes
│   │   │   │   └── security/      # Authentication & authorization
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── seed-database.sql
│   │   └── test/
│   │       ├── java/com/cs203/core/
│   │       │   ├── controller/    # Controller tests
│   │       │   ├── service/       # Service tests
│   │       │   └── repository/    # Repository tests
│   │       └── resources/
│   │           └── application-test.properties
│   ├── build.gradle               # Gradle build configuration
│   ├── Dockerfile                 # Docker configuration
│   └── compose.yaml               # Docker Compose setup
│
├── frontend/                      # Frontend (SvelteKit)
│   ├── src/
│   │   ├── lib/
│   │   │   ├── components/        # Svelte components
│   │   │   ├── api/               # API client functions
│   │   │   ├── stores/            # State management
│   │   │   └── assets/            # Images, icons, etc.
│   │   └── routes/                # SvelteKit routes (pages)
│   │       ├── admin/             # Admin dashboard
│   │       ├── history/           # Calculation history
│   │       ├── login/             # Login page
│   │       ├── register/          # Registration page
│   │       └── settings/          # User settings
│   ├── e2e/                       # Playwright E2E tests
│   ├── static/                    # Static assets
│   ├── package.json
│   ├── vite.config.ts
│   └── playwright.config.ts
│
├── docs/                          # Documentation
│   ├── api.json                   # API specification
│   ├── api-guide.md               # API usage guide
│   ├── sonarqube-setup.md         # SonarQube integration guide
│   └── Standups/                  # Team standup notes
│   └── Sprint Meeting Agenda/     # Team Sprint meeting agenda notes
│   └── Wireframe/                  # Wireframes created for each sprint
│
├── .github/
│   ├── workflows/                 # CI/CD pipelines
│   │   ├── unit-test.yml          # Backend unit tests
│   │   └── sonarqube.yml          # Code quality analysis
│   └── pull_request_template.md   # PR template
│
└── README.md                      # README
```

## Getting Started

### Prerequisites
- **Node.js** (v20.19.0 or higher)
- **Java** (JDK 17)
- **PostgreSQL** (for production database)

### **Clone Repo**
```
git clone https://github.com/TheMajoris/TARIFF
```
### **Backend Setup**
1. **Navigate to the backend directory:**
```bash
cd core
```

2. **Start the Backend Spring Boot Application**
This will build the Spring Boot JAR and start both the backend and PostgreSQL containers.
```bash
docker compose up --build -d
or
docker compose up -f
```
Backend by default will be available at http://localhost:8080


4. **[Optional] Run Tests:**
```bash
bash gradlew clean test jacocoTestReport
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

### **Database Setup** (Optional)
If you're not using Docker and want to run PostgreSQL manually:
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

API documentation is available on [our dedicated swagger page](http://localhost:8080/swagger-ui.html)

## Development Workflow

- **Issue Tracking**: Issues are tracked in JIRA
- **Branch Naming**: Relevant to the issue or named after the SCRUM task
- **Pull Requests**: Use the provided [PR template](.github/pull_request_template.md)
- **CI/CD**: Automated testing via GitHub Actions
- **Code Review**: CodeRabbitAI for a first look, then shifted to manual review.

## Testing

- **Backend**: Unit tests with JUnit; Generated code coverage report by JaCoCo can be found in this file: `core/build/reports/jacoco/test/html/index.html`
- **Frontend**: Unit tests with Vitest, E2E tests with Playwright can be found in this file: `frontend/test-results/playwright-e2e-report/index.html`
- **CI**: Automated testing on pull requests
