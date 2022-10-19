# Custom project from start.vaadin.com

This project was created from https://start.vaadin.com. It's a fully working Vaadin application that you continue developing locally.
It has all the necessary dependencies and files to help you get going.

The project is a standard Maven project, so you can import it to your IDE of choice. You'll need to have Java 8+ and Node.js 10+ installed.

To run from the command line, use `mvn spring-boot:run` and open [http://localhost:8080](http://localhost:8080) in your browser.

## Project structure

- `MainView.java` in `src/main/java` contains the navigation setup. It uses [App Layout](https://vaadin.com/components/vaadin-app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/src/` contains the client-side JavaScript views of your application.

## How it Works?

- You need AWS DynamoDB and AWS S3 services.
- Update the BackendService class with the credentials from the services above.
- The application will search all the DynamoDB tables from the account provided and open a UI containing a list with the DynamoDb tables.
- The user choose the table and generates a CSV file.
- The system then saves the CSV file on AWS S3.
