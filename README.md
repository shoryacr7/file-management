# File Manager

This is a full-stack file management application.

## Project Overview

The project consists of two main parts:

*   **Backend:** A Spring Boot application that handles file uploads, storage, and retrieval.
*   **Frontend:** An Angular application that provides a user interface for interacting with the backend.

## Technologies Used

*   **Backend:**
    *   Java
    *   Spring Boot
    *   Maven
*   **Frontend:**
    *   TypeScript
    *   Angular
    *   Angular CLI

## How to Run

### Backend

1.  Navigate to the `backend` directory.
2.  Run the application using Maven: `mvn spring-boot:run`

### Frontend

1.  Navigate to the `frontend` directory.
2.  Install dependencies: `npm install`
3.  Run the development server: `ng serve`. The application will automatically reload if you change any of the source files.
4.  Open your browser to `http://localhost:4200/`

## Additional Frontend Commands

*   **Build:** `ng build` (The build artifacts will be stored in the `dist/` directory.)
*   **Unit Tests:** `ng test`
*   **End-to-end Tests:** `ng e2e`