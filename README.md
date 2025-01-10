DevOps Project

CI/CD pipeline on Sudoku app.

## Pipeline Workflow
The pipeline is triggered on `push` events to the `main` branch and `pull_request` events to the `main` branch.



### Workflow for Push to Main Branch:
- **Build and Deploy Project**:
  - **Checkout Code**: Pulls the code from the repository.
  - **Build Docker Image**: Builds the Docker image for the app.
  - **Login to GitHub Container Registry**: Logs into GitHub Container Registry using the `GITHUB_TOKEN`.
  - **Push Docker Image**: Pushes the Docker image to GitHub Container Registry.

### Workflow for Pull Request to Main Branch:
- **Lint Code**:
  - **Checkout Code**: Pulls the code from the repository.
  - **Set up Python**: Sets up Python for linting.
  - **Install Python Dependencies**: Installs the necessary dependencies (`flake8` for Python linting).
  - **Set up Node.js**: Sets up Node.js for linting.
  - **Install JavaScript Dependencies**: Installs JavaScript dependencies (`prettier` for JS linting).
  - **Run Python Linter**: Runs the Python linter to check for code style issues.
  - **Run JavaScript Linter and Fix Issues**: Runs Prettier to format JavaScript code.

- **Snyk Security Scan**:
  - **Checkout Code**: Pulls the code from the repository.
  - **Set up Snyk**: Sets up the Snyk security tool.
  - **Install Dependencies**: Installs required dependencies for the app.
  - **Authenticate with Snyk**: Authenticates with Snyk using the `SNYK_TOKEN`.
  - **Snyk Scan**: Runs a security scan to detect vulnerabilities.

- **Trivy Security Scan**:
  - **Checkout Code**: Pulls the code from the repository.
  - **Trivy Scan**: Runs a security scan using the Trivy tool to detect security issues in the Docker image.

- **Run Tests**:
  - **Checkout Code**: Pulls the code from the repository.
  - **Run Tests**: Runs tests on the application (you should replace the `echo` command with your actual test command).

- **Run Python Unit Tests**:
  - **Checkout Code**: Pulls the code from the repository.
  - **Set up Python**: Sets up Python for running unit tests.
  - **Install Dependencies**: Installs dependencies required for running the unit tests.
  - **Run Tests**: Runs the unit tests using Python's `unittest` framework.

## Secrets

Make sure to configure the following secrets in your GitHub repository for secure access:
- `GITHUB_TOKEN`: Used to authenticate with GitHub Container Registry.
- `SNYK_TOKEN`: Used to authenticate with Snyk for security scanning.

## Requirements

- GitHub Actions
- Docker
- Python (for linting and tests)
- Node.js (for JavaScript linting)
- Snyk (for security scanning)
- Trivy (for security scanning)

## Notes 
- You may need to adjust the test commands in the `Run Tests` job based on your actual testing framework.
- The pipeline is designed for a Python-based application with a Docker container and JavaScript components.

For any further questions, feel free to open an issue in the repository.


