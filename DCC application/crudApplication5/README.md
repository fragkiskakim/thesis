## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Architecture Overview
This version of the project implements a proper 3-Tier Architecture, effectively separating concerns across three distinct layers:

- Presentation Tier: Handles user interface and interactions (CRUDapplication).
- Application Tier: Manages business logic and acts as a mediator between the presentation and data layers (auxFunctions).
- Data Tier: Responsible for all database operations (databaseFunctions and databaseConnection).

### Corrected 3-Tier Architecture

In this version, the key architectural flaw from previous iterations has been resolved. The Presentation Tier (CRUDapplication) now only interacts with the Application Tier (auxFunctions), and the Application Tier handles communication with the Data Tier (databaseFunctions).

For example, the flow in the Presentation Tier looks like this:

```java
// In CRUDapplication (Presentation Tier)
auxFunctions.add(...); // Communicate only with Application Tier
The Application Tier (auxFunctions) is now responsible for managing all the business logic, including communicating with the Data Tier. This might look like:
```

```java
// In auxFunctions (Application Tier)
double[] coordinates = calculateCoordinates(...);
dbFunctions.addEntry(...); // Application Tier communicates with Data Tier

```

### Separation of Concerns:
The Presentation Tier is now only responsible for user interaction and delegates all business logic and database interaction to the Application Tier. This ensures a clear separation of concerns, with each tier focusing on its own responsibilities.

### Final Flow of Communication
Presentation Tier: Initiates user requests and communicates with the Application Tier.
Application Tier: Processes the request, applies business logic, and handles communication with the Data Tier.
Data Tier: Executes database operations as directed by the Application Tier.


This version follows the principles of a well-architected 3-Tier System, where each layer operates independently, ensuring modularity, maintainability, and scalability.