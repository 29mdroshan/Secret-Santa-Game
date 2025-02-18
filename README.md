# Secret-Santa-Game
Design for playing secret santa game

# Secret Santa Application
## Overview
The Secret Santa application allows users to organize a Secret Santa gift exchange among a group of participants. Users can upload a CSV file containing the Employee_Name and Employee_Email of participants, and the application randomly assigns each participant a Secret Santa, ensuring that no one is assigned to themselves.

## Features
- **CSV File Upload**: Upload a CSV file with participant details (Employee_Name, Employee_Email).
- **Previous Assignments**: Optionally upload a CSV file with previous Secret Santa assignments to avoid repeating matches.(Csv should contain Employee_Name, Employee_EmailID, Secret_Child_Name, Secret_Child_EmailID)
- **CSV Download**: Download a CSV file with the current assignments.

## Technologies Used
- Java
- Spring Boot
- JUnit
- Mockito
- Spring MVC
- OpenCSV

## Getting Started

### Prerequisites
- Java (JDK 11 or higher)
- Gradle
- An IDE (e.g., IntelliJ IDEA, Eclipse)

### Installation
1. Clone the repository:
2. Navigate to the project directory: cd secret-santa
3. Build the project: ./gradlew clean build
4. Running the Application: ./gradlew bootrun

The application will start on http://localhost:8000.

API Endpoints
Assign Secret Santa
- URL: /v1/assign/secret/santa
- Method: POST
- Request:
- Multipart form data containing:
	- employees (CSV file with participants)
	- previousAssignments (optional, previous assignments CSV file)
- Response: CSV file with current Secret Santa assignments.

https://github.com/29mdroshan/Secret-Santa-Game.git
