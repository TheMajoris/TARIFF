# SonarQube Setup and Usage Guide

## Overview

SonarQube is an automated code quality and security analysis tool integrated into our CI/CD pipeline. It runs automatically on every pull request to the `main` branch and provides detailed reports on code quality, security vulnerabilities, bugs, and code smells.

## Table of Contents

1. [How It Works](#how-it-works)
2. [Viewing SonarQube Results](#viewing-sonarqube-results)
3. [Interpreting Results](#interpreting-results)
4. [Understanding Metrics](#understanding-metrics)
5. [Quality Gates](#quality-gates)
6. [Taking Action](#taking-action)

---

## How It Works

### Automatic Analysis

Our GitHub Actions workflow (`.github/workflows/sonarqube.yml`) automatically:
1. Runs on every push to `main` and every pull request
2. Builds the project and runs all tests
3. Collects code coverage data using JaCoCo
4. Uploads analysis results to SonarCloud
5. Decorates the pull request with quality gate status

### SonarCloud Integration

- **Organization**: `majoris`
- **Project Key**: `TheMajoris_TARIFF`
- **Dashboard**: https://sonarcloud.io/project/overview?id=TheMajoris_TARIFF

---

## Viewing SonarQube Results

### 1. In Pull Requests

When you create or update a pull request, SonarCloud automatically:
- Posts a comment with the quality gate status
- Shows inline annotations for new issues in your code
- Provides a link to the full analysis report

**Example PR Comment:**
```
✅ Quality Gate Passed
✓ 0 New Bugs
✓ 0 New Vulnerabilities
✓ 0 New Security Hotspots
✓ 0 New Code Smells
✓ Coverage: 85.2% (target: 80%)
```

### 2. On SonarCloud Dashboard

Visit https://sonarcloud.io/project/overview?id=TheMajoris_TARIFF to see:
- Overall project health
- Historical trends
- Detailed breakdowns by issue type
- File-level analysis

---

## Interpreting Results

### Issue Severity Levels

SonarQube categorizes issues by severity:

#### 🔴 **BLOCKER**
- **Impact**: Critical - will cause bugs or crashes in production
- **Examples**: 
  - SQL injection vulnerabilities
  - Unhandled exceptions that will crash the application
  - Resource leaks (database connections, file handles)
- **Action Required**: **Must fix immediately** before merging

#### 🟠 **CRITICAL**
- **Impact**: High - likely to cause bugs or security issues
- **Examples**:
  - Hardcoded credentials
  - Potential null pointer exceptions
  - Security vulnerabilities with high risk
- **Action Required**: **Fix before merging** unless there's a documented exception

#### 🟡 **MAJOR**
- **Impact**: Medium - could cause bugs or maintainability issues
- **Examples**:
  - Unused imports or variables
  - Too many parameters in a method
  - Duplicated code blocks
  - Missing error handling
- **Action Required**: Fix if introduced in your PR; existing issues can be tracked

#### 🔵 **MINOR**
- **Impact**: Low - minor code quality or style issues
- **Examples**:
  - Missing Javadoc comments
  - Variable naming conventions
  - Code formatting inconsistencies
- **Action Required**: Fix when convenient; don't block PRs

#### ⚪ **INFO**
- **Impact**: Informational only
- **Examples**:
  - Suggestions for best practices
  - Alternative approaches
- **Action Required**: Optional; consider for future improvements

---

## Understanding Metrics

### Key Metrics Explained

#### 1. **Bugs** 🐛
- **Definition**: Code that is demonstrably wrong or will cause errors
- **Example**: Using `==` instead of `.equals()` for string comparison
- **Target**: 0 new bugs in each PR
- **Why It Matters**: Bugs lead to runtime errors and incorrect behavior

#### 2. **Vulnerabilities** 🔒
- **Definition**: Security issues that can be exploited by attackers
- **Example**: SQL injection, XSS, hardcoded secrets
- **Target**: 0 new vulnerabilities
- **Why It Matters**: Security breaches can expose sensitive data

#### 3. **Security Hotspots** 🔥
- **Definition**: Security-sensitive code that requires manual review
- **Example**: Cryptographic operations, authentication logic
- **Target**: Review and mark as safe or fix
- **Why It Matters**: Prevents security issues before they become vulnerabilities

#### 4. **Code Smells** 👃
- **Definition**: Maintainability issues that make code harder to understand/modify
- **Example**: Methods with too many lines, complex conditional logic
- **Target**: < 5% new code smells
- **Why It Matters**: Poor maintainability slows down development

#### 5. **Coverage** 📊
- **Definition**: Percentage of code executed by unit tests
- **Formula**: (Lines Covered / Total Lines) × 100
- **Target**: ≥ 80% for new code
- **Why It Matters**: Higher coverage means better tested, more reliable code

#### 6. **Duplications** 📋
- **Definition**: Percentage of duplicated code blocks
- **Target**: < 3%
- **Why It Matters**: Duplicated code makes maintenance harder and increases bug risk

#### 7. **Technical Debt** ⏱️
- **Definition**: Estimated time to fix all code smells
- **Displayed As**: "2h 30min" or "5 days"
- **Why It Matters**: Tracks maintenance burden over time

---

## Quality Gates

### What is a Quality Gate?

A Quality Gate is a set of conditions that code must meet to be considered production-ready. Our project uses the **SonarCloud default Quality Gate**:

### Default Quality Gate Conditions

| Metric | Condition | Applies To |
|--------|-----------|------------|
| Coverage | ≥ 80% | New Code |
| Duplicated Lines | ≤ 3% | New Code |
| Maintainability Rating | A | New Code |
| Reliability Rating | A | New Code |
| Security Rating | A | New Code |
| Security Hotspots Reviewed | 100% | Overall Code |

### Quality Gate Status

#### ✅ **PASSED**
- All conditions met
- Code meets quality standards
- **Safe to merge**

#### ❌ **FAILED**
- One or more conditions not met
- Review and fix issues before merging
- Check which specific metric failed

---

## Taking Action

### When Quality Gate Fails

#### Step 1: Identify the Issue
1. Click the SonarCloud link in the PR comment
2. Go to the "Issues" tab
3. Filter by "New Code" to see only your changes
4. Sort by "Severity" (Blocker → Critical → Major)

#### Step 2: Understand the Issue
- Read the issue description
- Click "Why is this an issue?" for detailed explanation
- Review the code example and suggested fix
- Check if there are similar issues elsewhere

#### Step 3: Fix the Issue
- Make the necessary code changes
- Run tests locally to verify the fix
- Push the changes to your branch
- SonarQube will automatically re-analyze

#### Step 4: Mark False Positives (Rare)
If you believe an issue is incorrect:
1. Add a comment explaining why
2. Tag a senior developer for review
3. They can mark it as "Won't Fix" or "False Positive" in SonarCloud

### Common Issues and Solutions

#### Low Code Coverage
```
❌ Coverage is 65% (target: 80%)
```
**Solution**: Add unit tests for your new code
- Test happy paths and edge cases
- Mock external dependencies
- Use `@DataJpaTest` for repository tests

#### Code Duplication
```
❌ Duplicated lines: 5% (target: < 3%)
```
**Solution**: Extract common code
- Create utility methods
- Use inheritance or composition
- Extract constants

#### Complex Methods
```
🟡 Cognitive Complexity of method is 18 (max: 15)
```
**Solution**: Refactor the method
- Break into smaller methods
- Reduce nesting levels
- Simplify conditional logic

#### Security Vulnerabilities
```
🔴 SQL Injection vulnerability detected
```
**Solution**: Use parameterized queries
- Use JPA methods instead of native queries
- Use `@Query` with named parameters
- Never concatenate user input into SQL

---

## Best Practices

### 1. **Fix Issues Early**
- Review SonarQube results as soon as analysis completes
- Don't wait until the end of the sprint
- Small, frequent fixes are easier than large refactoring

### 2. **Understand Before Fixing**
- Read the issue description thoroughly
- Understand WHY it's a problem
- Learn from each issue to avoid repeating it

### 3. **Maintain High Coverage**
- Write tests as you write code
- Aim for > 80% coverage on all new code
- Test edge cases and error conditions

### 4. **Keep Technical Debt Low**
- Address code smells promptly
- Don't let technical debt accumulate
- Refactor when you see opportunities

### 5. **Review Trends**
- Check the dashboard regularly
- Monitor if metrics are improving or degrading
- Celebrate improvements!

---

## Resources

### Official Documentation
- [SonarCloud Documentation](https://docs.sonarcloud.io/)
- [Java Rules](https://rules.sonarsource.com/java/)
- [Understanding Issues](https://docs.sonarcloud.io/improving/issues/)

### Internal Resources
- [Project Dashboard](https://sonarcloud.io/project/overview?id=TheMajoris_TARIFF)
- [Quality Gate](https://sonarcloud.io/organizations/majoris/quality_gates/show/9)

### Getting Help
- **Question about a specific issue?** Ask in the team Slack channel
- **Think an issue is a false positive?** Tag a senior developer
- **Need help fixing something?** Pair program with a teammate

---

## FAQ

### Q: Why did my PR fail the quality gate?
**A**: Check the SonarCloud comment in your PR. It will show which metric failed. Most common reasons are low test coverage or new bugs/vulnerabilities.

### Q: Can I merge if the quality gate fails?
**A**: No, passing the quality gate is required for all PRs. This ensures we maintain high code quality standards.

### Q: How long does analysis take?
**A**: Usually 2-5 minutes after pushing code. You'll see a pending check in your PR that updates when complete.

### Q: Why does SonarQube flag code that works fine?
**A**: SonarQube detects potential issues, not just current bugs. It finds code that might break in edge cases or is hard to maintain.

### Q: What if I disagree with an issue?
**A**: Add a comment in the PR explaining your reasoning. A senior developer can mark it as "Won't Fix" if appropriate.

### Q: Does SonarQube check my entire codebase?
**A**: For PRs, it focuses on **new code** (your changes). For main branch, it analyzes the entire codebase.

---

## Example: Interpreting a Real Report

```
❌ Quality Gate Failed

Bugs: 1 new bug (target: 0)
  🔴 BLOCKER: Possible NullPointerException
  Location: UserService.java:45
  
Code Smells: 3 new code smells
  🟡 MAJOR: Method has too many parameters (8, max: 7)
  Location: TariffCalculator.java:102
  
  🔵 MINOR: Variable name doesn't follow convention
  Location: CountryRepository.java:23
  
  🔵 MINOR: Missing Javadoc
  Location: ProductService.java:15

Coverage: 75.2% (target: 80%)
  Missing coverage: 15 lines in ProductService.java
```

### Action Plan:
1. **Fix the BLOCKER bug immediately** (NullPointerException in UserService)
2. **Refactor the method** with too many parameters (use a DTO or builder pattern)
3. **Add tests** for ProductService to increase coverage to 80%+
4. **Fix minor issues** (variable naming, Javadoc) while you're at it

Push changes → SonarQube re-analyzes → Quality Gate passes → Merge! ✅

---

**Last Updated**: November 6, 2025
**Maintained By**: Development Team
