## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Architecture Overview
This version of the project implements a 3-Tier Architecture, designed to separate responsibilities across three distinct layers:

- Presentation Tier: Handles the user interface and interactions (represented by CRUDapplication).
- Application Tier: Manages business logic and intermediary processing (represented by auxFunctions).
- Data Tier: Responsible for database operations and data management (represented by databaseFunctions and databaseConnection).

### Anti-Pattern: Direct Communication Between Presentation and Data Tiers

While the project follows the 3-tier structure, an intentional anti-pattern has been introduced. In this design, the Presentation Tier (CRUDapplication) directly creates and communicates with the Data Tier (databaseFunctions), bypassing the Application Tier (auxFunctions).

For example, the CRUDapplication class directly instantiates an object of the databaseFunctions class and uses it to interact with the database. 

```java
// In CRUDapplication (Presentation Tier)
private DatabaseFunctions3 dbFunctions = new DatabaseFunctions3();
...
dbFunctions.addEntry(..); // Direct interaction with Data Tier
```

By having the Presentation Tier directly communicate with the Data Tier, the architecture becomes less modular, harder to maintain, and introduces potential scalability and security concerns. Ideally, the CRUDapplication should only interact with the Application Tier, which then handles all communication with the Data Tier.

#### Consequences of this Anti-Pattern:

Violation of Separation of Concerns: Each tier in a 3-tier architecture should handle a distinct part of the application's responsibilities. In this version, the Presentation Tier violates this principle by interacting with the Data Tier directly.
Tight Coupling: By creating a direct dependency between the Presentation Tier and the Data Tier, changes in the database logic may require changes in the UI layer, which increases the maintenance burden and reduces flexibility.

This version of the project demonstrates the risks of breaking the separation of responsibilities in a 3-tier architecture by allowing the Presentation Tier to communicate directly with the Data Tier.
