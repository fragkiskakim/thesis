## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Architecture Overview
This project follows a simple Client-Server architecture with a thick client. The client handles most of the business logic and interacts with the "server" to perform actions such as retrieving or updating data. 

The main part of this project is the database connection. However, it intentionally deviates from the standard approach to highlight a common anti-pattern. Instead of creating an instance of the DatabaseConnection class and establishing an association between the client and database connection (which would be represented in a UML diagram as an object relationship), the project uses a static method getConnection() in the DatabaseConnection class.

This method is called directly from the client without instantiating an object, which breaks the association between the "client" class and the databaseConnection class in the UML diagram.