## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Architecture Overview
In this version of the project, we continue using the 3-Tier Architecture with the following layers:

- Presentation Tier: Manages user interaction (CRUDapplication).
- Application Tier: Handles business logic (auxFunctions).
- Data Tier: Manages database operations (databaseFunctions and databaseConnection).

### Attempt to Correct Layered Communication

To address the anti-pattern from the previous version, an attempt was made to have the Presentation Tier communicate with the Application Tier first, and then pass the result to the Data Tier. In this approach, the CRUDapplication instantiates both the auxFunctions (Application Tier) and the databaseFunctions (Data Tier) and uses them sequentially. For example:

```java
// In CRUDapplication (Presentation Tier)
double[] coordinates = auxFunctions.calculateCoordinates(...);
dbFunctions.updateEntry(...);

```

In this way, the Presentation Tier requests business logic from the Application Tier (auxFunctions) before interacting with the Data Tier (databaseFunctions). However, this still introduces issues and does not fully conform to a proper 3-tier design.

### Anti-Pattern: Presentation Tier Controls Both Layers

The core problem with this approach is that the Presentation Tier is directly managing the flow between the Application Tier and Data Tier, which leads to the following issues:

Responsibility mixing:
The Presentation Tier should only handle user interactions, not coordinate or manage the flow between the Application and Data Tiers. By having the CRUDapplication directly invoke both auxFunctions and databaseFunctions, the Presentation Tier is assuming control over logic that should be handled within the Application Tier.

Complex flow management: The CRUDapplication class now has to manage the flow of data across the layers, which increases its complexity and blurs the lines between UI logic and core business logic.

### Ideal Approach

In a well-designed 3-tier architecture, the Presentation Tier should only interact with the Application Tier, which then handles all communication with the Data Tier. The Application Tier should encapsulate the entire process, coordinating both the business logic and data access behind the scenes, keeping the Presentation Tier unaware of the database operations.

This version demonstrates an improved, but still flawed, approach to layering in a 3-tier architecture, as the Presentation Tier is still handling responsibilities that should belong to the Application Tier.