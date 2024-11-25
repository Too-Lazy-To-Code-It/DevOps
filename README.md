# CI/CD Pipeline for Spring Application

This repository contains the CI/CD pipeline configuration for a Spring Boot application. The pipeline integrates with various tools to ensure a secure, efficient, and high-quality development workflow.

## Pipeline Features
1. **Jenkins**: Orchestrates the CI/CD pipeline with automated build and deployment stages.
2. **Trivy**: Performs container image scanning to identify vulnerabilities.
3. **OWASP Dependency-Check**: Scans for known security vulnerabilities in project dependencies.
4. **Docker**: Builds and deploys Docker images for the Spring Boot application.
5. **Nexus Repository**: Manages artifacts, including Docker images and dependencies.
6. **JUnit**: Executes unit tests to ensure code correctness.
7. **JaCoCo**: Generates test coverage reports to track and improve code quality.

## Frontend Integration
This backend API connects seamlessly with the frontend project, showcasing real-time functionality.  
**Frontend Repository**: [Angular v18](<https://github.com/Too-Lazy-To-Code-It/Devops-Front>)

## Pipeline Stages
1. **Code Checkout**: Clones the repository from version control.
2. **Static Code Analysis**: Runs OWASP Dependency-Check for dependency security.
3. **Unit Testing**: Executes JUnit tests and collects JaCoCo coverage reports.
4. **Docker Build**: Builds the Docker image for the Spring Boot application.
5. **Security Scan**: Uses Trivy to scan the built Docker image for vulnerabilities.
6. **Artifact Upload**: Pushes the Docker image to Nexus for versioning and distribution.
7. **Deployment**: Deploys the Docker container to the target environment.
