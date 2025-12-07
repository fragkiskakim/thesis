## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Class Instantiation Issue
In this version, I encountered a cyclic dependency problem related to class instantiation:

I need an instance of CRUDApplication6 in auxFunctions6.
Simultaneously, I need an instance of auxFunctions6 in CRUDApplication6.
Problem:

This results in a cyclic dependency, where each class depends on the other. Since both classes require the other to be instantiated first, this creates a situation where neither can be instantiated successfully. This leads to initialization problems and makes the architecture harder to maintain.

To resolve this, i have used setter methods to decouple the instantiation process and avoid mutual dependencies during object creation.

## Architecture Overview
This version of the project implements the MVC (Model-View-Controller) Architecture, a design pattern that separates the application into three interconnected components:

- Model: Manages the data.
- View: Handles the user interface and user interactions.
- Controller: Acts as an intermediary, processing user actions from the View and updating the Model or View accordingly.

### Communication Flow in this MVC Architecture

In this implementation, the components interact as follows:

View (User Interface):
The View is responsible for handling user actions and interactions. When a user performs an action, the View communicates with the Controller via an instance of it and calling its methods to process the action. 

Controller (Request Handler):
The Controller receives the user's request from the View, processes it, and applies the necessary business logic. If the operation requires interacting with data (e.g., adding or retrieving entries), the Controller communicates with the Model. 
Once the Controller retrieves or updates the data via the Model, it sends a command to the View to refresh or update the user interface based on the new data.

Model (Data and Business Logic):
The Model handles the data management. It is responsible for interacting with the database, performing calculations. The Model does not directly interact with the View, keeping a clear separation between data and presentation.

Example Flow
The View receives user input (e.g., a button click to add an entry) and calls the Controller:
```java
// In View
controller.add();
```

The Controller processes the request, handles data logic and communicates with the Model to perform the necessary data operation:
```java
// In Controller
model.addEntry(...); // Calls Model to handle data

//After processing the data, the Controller updates the View to refresh its tables or UI elements:
view.refreshTable(); // Informs View to update UI
```

### Anti-Pattern: Controller Handling Data Logic
The key issue with this approach is the violation of MVC (Model-View-Controller) principles, where the controller is improperly handling data logic. In a proper MVC architecture, the controller’s role is limited to managing the communication flow between the view and the model. It should not manage or manipulate data directly.

The model is responsible for all data-related operations, including performing any necessary calculations or transformations before or after interacting with the database. By moving data logic into the controller, this anti-pattern leads to tighter coupling, making the system less modular, harder to maintain, and more prone to errors.