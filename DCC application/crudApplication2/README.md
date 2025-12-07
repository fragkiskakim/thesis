## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Architecture Overview
This version of the project continues to utilize a Client-Server architecture with a thick client, but now addresses the remaining architectural issues present in previous versions

### Corrected Database Connection Design

The issue with the database connection has been fully resolved. The DatabaseConnection class is now instantiated as an object, allowing the client to interact with the database through a properly managed connection instance. 

### Server-Side Query Execution

One major improvement in this version is that all database operations have been moved to the server. In previous versions, the client was responsible for preparing and executing SQL queries, which posed security, performance, and maintainability risks. Now, the client only sends requests to the server, which handles all query preparation, execution, and interaction with the database.

This version reflects a well-architected client-server model with proper separation of client and server responsibilities.