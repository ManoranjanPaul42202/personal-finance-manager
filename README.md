# Personal Finance Manager (Java Swing + Maven)

A desktop personal finance application built with **Java Swing** to track income/expenses, manage categories, set budgets, and view monthly/yearly summaries. Uses **Maven** for build and dependency management and supports JDBC-backed storage MySQL.

## Features

- Transactions: add, edit, delete income and expenses with date, amount, category, and notes.
- Categories: default/custom categories for organization.
- Reports: monthly summaries and category breakdowns.
- Storage: file-based H2 (embedded) or MySQL via JDBC (configurable).


## Tech stack

- Language: Java (Swing UI).
- Build: Maven (pom.xml).
- DB: MySQL.


## Getting started

### Prerequisites

- JDK 8+ installed and on PATH.
- Maven 3.8+ installed and on PATH.
- Git for cloning.


### Clone

- git clone https://github.com/ManoranjanPaul42202/personal-finance-manager.git
- cd personal-finance-manager


## Build and run (Maven)

- Build: mvn clean package
- Run without POM config: mvn exec:java -Dexec.mainClass="com.mycompany.financeManagerApp.Main"
- Recommended POM config:
    - Add to pom.xml:
        - <plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>1.6.0</version>
  <configuration>
    <mainClass>com.example.finance.Main</mainClass>
  </configuration>
</plugin>
    - Then run: mvn exec:java
- Create runnable JAR (if shaded):
    - Add Maven Shade or Assembly plugin, then:
        - mvn clean package and run target/personal-finance-manager-jar-with-dependencies.jar


## Configuration

Example properties MySQL:
- db.driver=com.mysql.cj.jdbc.Driver
- db.url=jdbc:mysql://127.0.0.1:3306/finance_manager\&useSSL=false
- db.user=finance_user
- db.password=strong_password


## Database setup

- Install MySQL and create database/user:
    - CREATE DATABASE finance CHARACTER SET utf8mb4;
    - CREATE USER 'finance_user'@'%' IDENTIFIED BY 'strong_password';
    - GRANT ALL ON finance.* TO 'finance_user'@'%';
    - FLUSH PRIVILEGES;
- Add dependency in pom.xml (if not present):
    - <dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <version>8.4.0</version>
</dependency> 
- JDBC URL:
    - jdbc:mysql://127.0.0.1:3306/finance?serverTimezone=UTC\&useSSL=false 

Example Java snippet (for reference; the app likely loads from properties):

- Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/finance?serverTimezone=UTC\&useSSL=false", "finance_user", "strong_password");


### Schema initialization

- On first run, the app can initialize tables automatically or load SQL from resources/schema.sql. If schema.sql exists, run it once against MySQL or trigger it via an initialization routine.
- Typical tables:
    - categories(id PK, name, type, color)
    - transactions(id PK, category_id FK, amount, date, note)
    - budgets(id PK, month, amount, category_id nullable)
    - users(id PK, email, password_hash, created_at) if authentication is implemented
 


## Project structure

Adjust to match actual packages once the source tree is reviewed: 

- src/main/java/.../ui: Swing frames, panels, dialogs (Dashboard, Transactions, Categories). 
- src/main/java/.../model: POJOs (Transaction, Category, Budget). 
- src/main/java/.../service: business logic and validation. 
- src/main/java/.../storage: JDBC DAO or repository classes. 
- src/main/resources: app.properties, schema.sql, icons. 


## Common tasks

- Add a transaction: open Transactions window → Add → enter fields → Save. 
- Create a category: open Categories → New → name/color → Save. 
- Set a budget: Budgets → select month/category/amount → Save. 
- Export data: File → Export (if implemented). 


## Development

- Follow standard Swing EDT rules (create/update UI on Event Dispatch Thread). 
- Prefer parameterized PreparedStatement for all DB queries. 
- Add JUnit tests for model/service layers when applicable. 


## Packaging

- Maven: create a runnable JAR using Shade/Assembly; optionally create native installers with jpackage via a Maven plugin. 
- Example: mvn clean package, then run target/*-jar-with-dependencies.jar (if configured). 


## Roadmap

- Recurring transactions and reminders. 
- CSV import and rule-based auto-categorization. 
- More charts and insights (spending trends). 
- Preferences for currency/locale and themes. 



