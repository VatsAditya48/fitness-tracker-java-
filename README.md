# Fitness Tracker App (Java Swing + JDBC)

**Author:** Aadii

## Short description
A Java Swing GUI-based fitness tracker with JDBC (MySQL) backend. Implements core OOP concepts, collections & generics, multithreading, and DAO-based database operations — built for Review 1.

## Tech stack
- Java 11+
- Swing (GUI)
- MySQL (or SQLite)
- JDBC (mysql-connector-java)
- (Optional) Maven

## Repository structure
See the repository tree in the root directory.

## Setup & Run
1. Clone repository:
```bash
git clone <this-repo-url>
cd fitness-tracker-java
```

2. Create database:
Run `sql/schema.sql` in your MySQL client.

3. Update DB credentials:
Edit `src/main/java/config/DBConfig.java` and set `URL`, `USER`, `PASS`.

4. Build & run
- **If using Maven**
```bash
mvn compile
mvn exec:java -Dexec.mainClass="App"
```
- **If not using Maven**
```bash
javac -d out src/main/java/**/*.java
java -cp out App
```

5. Login:
- Enter an email to login. If the email is new, enter name to register.

## Files to check
- `presentation/` — PPTX & PDF for Review 1
- `sql/schema.sql` — DB schema
- `src/main/java/` — Java source code
- `screenshots/` — UI screenshots (add before submission)

## Mapping to Marking Rubric
- **OOP:** models + potential inheritance (Workout base class). Exception handling in DAOs.
- **Collections & Generics:** `List<T>`, `GenericRepository<T>`
- **Multithreading:** ScheduledExecutorService for auto-sync
- **DB classes:** `DBHelper`, `UserDAO`, `WorkoutDAO`, `ProgressDAO`
- **JDBC:** fully implemented CRUD via PreparedStatement

## License
MIT License — see `LICENSE`.