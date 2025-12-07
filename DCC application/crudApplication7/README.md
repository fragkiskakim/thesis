## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Architecture Overview
This version of the project implements the MVC (Model-View-Controller) Architecture, a design pattern that separates the application into three interconnected components:

- Model: Manages the data and handles data logic
- View: Handles the user interface and user interactions.
- Controller: Acts as an intermediary, processing user actions from the View and updating the Model or View accordingly.

### Communication Flow in this MVC Architecture

In this implementation, the components interact as follows:

View (User Interface):
The View is responsible for handling user actions and interactions. When a user performs an action, the View communicates with the Controller via an instance of it and calling its methods to process the action. 

Controller (Request Handler):
The Controller receives the user's request from the View and processes it. If the operation requires interacting with data (e.g., adding or retrieving entries), the Controller communicates with the Model. 
Once the Controller retrieves or updates the data via the Model, it sends a command to the View to refresh or update the user interface based on the new data.

Model (Data and Business Logic):
The Model handles the data management. It is responsible for interacting with the database, performing calculations. The Model does not directly interact with the View, keeping a clear separation between data and presentation.

The current approach addresses the anti-pattern from the previous version of MVC and properly separates the responsibilities across the Model, View, and Controller, following the MVC pattern to ensure a maintainable and scalable architecture.