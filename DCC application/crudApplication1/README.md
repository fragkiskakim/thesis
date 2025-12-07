## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Architecture Overview
This version of the project continues to follow a simple Client-Server architecture with a thick client. The client still handles much of the business logic, and the server is responsible for communicating with the database to retrieve and manipulate data.

### Improved Database Connection Design
In this version, the previous issue with the database connection has been resolved. Instead of using a static method to connect to the database, the DatabaseConnection class is now properly instantiated as an object, creating a clear association between the client and the database connection.

### Remaining Issue: Query Execution on Client

However, a significant architectural issue persists. The client is currently responsible for preparing and executing SQL queries, which is work that should ideally be done by the server. For example:

In a proper client-server model, the client should only send requests (such as via REST or RPC) to the server, which then handles all the database operations internally. This ensures better separation of concerns and a more secure, scalable architecture.