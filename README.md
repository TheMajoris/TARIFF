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
- **Node.js** (v18 or higher)
- **Java** (JDK 17 or higher)
- **PostgreSQL** (for production database)

### **Clone Repo**
```
git clone https://github.com/your-org/repository-name.git
```
### **Backend Setup**
1. **Navigate to the backend directory:**
```
cd core
```

2. **Setup the application:**
```
./gradlew build
```
### **Frontend Setup**
1. Navigate to the frontend directory:
```bash
cd frontend
```

2. **Install dependencies:**
```bash
npm install
```

3. **Start development server:**
```bash
npm run dev -- --open
```
