# Contributing

Thank you for your interest in contributing to this project! We welcome issues, bug fixes, features, and constructive feedback.

By participating in this project, you agree to abide by our guidelines.

---

## How to Contribute

### 1. Reporting Bugs
- Search existing issues to ensure the bug hasn't been reported yet.
- Use our structured [Bug Report Template](https://github.com/pug523/shelf/issues/new?template=bug_report.yaml) to file a new issue.
- Provide clear steps to reproduce, Minecraft/Fabric versions, and log files where applicable.

### 2. Suggesting Features
- Open a [Feature Request](https://github.com/pug523/shelf/issues/new?template=feature_request.yaml) to discuss potential new options, GUI widgets, or layout engine adjustments.

### 3. Submitting Pull Requests (PRs)
Before writing any code, please keep the following project rules in mind:
1. **No Non-ASCII Characters in Code**:
   All source code files excluding language file must be strictly restricted to ASCII characters. Do not include Japanese, emojis, or any other non-ASCII text inside source code or code comments (use standard English for comments instead).
2. **Keep PRs Focused**:
   Do not bundle multiple unrelated fixes or features into a single PR. Create separate branches and PRs for distinct problems.

#### Development Workflow:
1. Fork the repository and create your branch from `main`.
2. To launch the test client and preview changes, run:
  ```bash
  ./gradlew :26.2:runClient
  ```
