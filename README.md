# Recipe Management System Application

This project is a Spring Boot application that provides a comprehensive platform for users to manage culinary recipes. It incorporates Spring Security to handle user registration and authentication, ensuring that users can securely access their recipes and perform CRUD operations with proper authorization.

## Features

- **User Registration and Authentication**: Securely register and authenticate users using Spring Security and HTTP Basic Auth, ensuring that only registered users can access the application's functionalities.

- **Recipe Management**: Allows users to create, read, update, and delete (CRUD) recipes. Each recipe includes details such as name, category, description, ingredients, directions, and the date it was posted or updated.

- **Recipe Ownership**: Enforces that only the author of a recipe can update or delete it, leveraging Spring Security to restrict access based on user ownership.

- **Search Functionality**: Users can search for recipes by name or category. The search is case-insensitive and results are sorted by the most recent.

- **Data Persistence**: Utilizes JPA (Java Persistence API) and an H2 in-memory database to store users and recipes, ensuring data persistence across sessions.

## Getting Started

Access the application at `http://localhost:8080`.

## Endpoints

- `POST /api/register`: Registers a new user.
- `POST /api/recipe/new`: Adds a new recipe (authenticated users only).
- `GET /api/recipe/{id}`: Retrieves a recipe by its ID (authenticated users only).
- `PUT /api/recipe/{id}`: Updates an existing recipe (recipe owners only).
- `DELETE /api/recipe/{id}`: Deletes a recipe (recipe owners only).
- `GET /api/recipe/search`: Searches for recipes by name or category (authenticated users only).
