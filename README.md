Fitness Tracking System 🏋️‍♂️
A comprehensive, Single-File Java Application designed to demonstrate advanced Java concepts. This project implements a hybrid architecture that works as both a Console Application and a Web Application (Servlet) within a single cohesive codebase.

📋 Features & Requirement Coverage
This project successfully implements all the following grading criteria:

1. OOP Implementation (10 marks)
Polymorphism: FitnessActivity abstract class with specialized Running and Cycling implementations.

Inheritance: Shared attributes (duration, date) in the parent class, specific logic in child classes.

Interfaces: Activity interface defining the contract for calorie calculation.

Exception Handling: Custom DatabaseException and InvalidActivityException.

2. Collections & Generics (6 marks)
Generics: Custom GoalValidator<T extends Number> class to validate numeric targets dynamically.

Collections: Usage of ArrayList<FitnessActivity> for storing and retrieving history.

3. Multithreading & Synchronization (4 marks)
Background Processing: AnalyticsProcessor class implements Runnable to handle heavy analysis.

Synchronization: Uses synchronized blocks to ensure thread-safe reporting to the console/logs.

Thread Management: Manual thread creation and joining for background tasks.

4. Database Operations & JDBC (16 marks)
DAO Pattern: ActivityDAO isolates all SQL operations (INSERT, SELECT).

JDBC Connectivity: DBConnection helper class manages the MySQL connection.

Security: Uses PreparedStatement to prevent SQL Injection.

Transaction Management: Auto-commit mode with resource closing (try-with-resources).

5. Servlet Implementation (10 marks)
Hybrid Controller: FitnessServlet handles both the Dashboard view (doGet) and Data Submission (doPost).

Dynamic HTML: Generates responsive HTML tables programmatically.

Deployment: Compliant with Apache Tomcat and standard Servlet containers.

🏗️ Project Structure
Unlike typical Maven projects, this solution is consolidated into a Single Source File for ease of review and compilation.

Plaintext

src/
└── FitnessSystem.java  <-- CONTAINS ALL LOGIC
     ├── public class FitnessSystem (Main)
     ├── class FitnessServlet (Web Controller)
     ├── class ActivityDAO (Database Layer)
     ├── class Running / Cycling (Models)
     └── class AnalyticsProcessor (Threading)
🚀 Setup & Execution
Prerequisites
Java JDK 8+

Apache Tomcat 9+ (For Web Mode)

MySQL Server

JARs: mysql-connector-java.jar and servlet-api.jar

1. Database Setup
Run this SQL script in your MySQL Workbench:

SQL

CREATE DATABASE fitness_db;
USE fitness_db;

CREATE TABLE activities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    type VARCHAR(50),
    duration INT,
    calories DOUBLE,
    date_logged VARCHAR(100)
);
2. How to Run (Console Mode)
To test the Logic, OOP, and Multithreading without a server:

Bash

# Compile
javac -cp .:mysql-connector.jar FitnessSystem.java

# Run
java -cp .:mysql-connector.jar FitnessSystem
3. How to Run (Web Mode)
To use the Dashboard and Servlet features:

Place FitnessSystem.java into your Dynamic Web Project's src folder.

Ensure the mysql-connector.jar is in WEB-INF/lib.

Deploy to Tomcat.

Access via Browser: http://localhost:8080/YourProject/fitness

📡 API / Servlet Endpoints
The FitnessServlet handles the following interactions:

GET /fitness: Renders the HTML Dashboard, Forms, and History Table.

POST /fitness: Accepts form data (type, duration, distance), saves it to MySQL, triggers the background thread, and redirects back to the dashboard.

🎯 Grading Checklist
Requirement	Implementation Status
OOP (Polymorphism, Inheritance)	✅ Implemented via FitnessActivity hierarchy
Collections & Generics	✅ Implemented via GoalValidator<T> & ArrayList
Multithreading	✅ Implemented via AnalyticsProcessor
Database Classes	✅ Implemented via ActivityDAO
JDBC Connectivity	✅ Implemented via DBConnection
Servlet Implementation	✅ Implemented via FitnessServlet
Code Quality	✅ Clean, Modular, Single-File Design

📝 License
Educational Project.
