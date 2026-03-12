# API Design Assignment

## Project Name

Books API

## Objective

The Books API is a RESTful API built on a dataset of over 60,000 books from Kaggle. 
It allows users to browse, search, and manage books along with their associated authors 
and categories. Books support full CRUD operations, while authors and categories are read-only resources. 
Write operations are protected with JWT authentication.

<!-- Design and develop a robust, well-documented API (REST or GraphQL) that allows users to retrieve and manage information from a dataset of your choice. The API must include JWT authentication, automated testing via Postman/Newman in a CI/CD pipeline, and be publicly deployed.
Choose a dataset (10000+ data points) that interests you — it should include at least one primary CRUD resource and two additional read-only resources. Sources like [Kaggle](https://www.kaggle.com/datasets), public APIs, or CSV files work well. Pick something you find interesting, as you will reuse this API in the next assignment (WT dashboard).
*Describe your API in a few sentences: what dataset does it serve, what are its main resources, and what can users do with it?*
-->
## Implementation Type

REST

## Links and Testing

| | URL / File |
|---|--|
| **Production API** | https://cu3119.camp.lnu.se/api/v1 |
| **API Documentation** | https://cu3119.camp.lnu.se/swagger-ui/index.html |
| **Postman Collection** | `books-api.postman_collection.json` |
| **Production Environment** | `production.postman_environment.json` |

**Examiner can verify tests manually - no setup needed:**
   ```
   npx newman run postman/books-api.postman_collection.json -e postman/production.postman_environment.json --insecure
   ```
*The **--insecure** flag might be required because the server uses a certificate issued by the university's internal CA, 
which does not seem to be trusted by Newman by default (or at least it was like this locally for me).*

<!-- **Examiner can verify tests in one of the following ways:**

1. **CI/CD pipeline** — check the pipeline output in GitLab for test results.
2. **Run manually** — no setup needed:
   ```
   npx newman run <collection.json> -e production.postman_environment.json
   ```
-->

## Dataset

<!--*Describe the dataset you chose:*-->

| Field | Description                                                                                   |
|---|-----------------------------------------------------------------------------------------------|
| **Dataset source** | https://www.kaggle.com/datasets/elvinrustam/books-dataset/                                    |
| **Primary resource (CRUD)** | Books (id, title, author, description, category, publisher, price, publishMonth, publishYear) |
| **Secondary resource 1 (read-only)** | Authors (id, name, books)                                                                     |
| **Secondary resource 2 (read-only)** | Categories (id, name, books)                                                                 |


## Design Decisions

### Seed Data

The API uses an automated seed script ([DataLoader.java](src/main/java/booksapi/util/DataLoader.java))
that runs automatically on startup. It loads all books with associated authors and categories from a CSV file 
into the database.
The seed script only runs if the database is empty — restarting the application will not duplicate data.

### Authentication

<!--*Describe your JWT authentication solution. Why did you choose this approach? What alternatives exist, and what are their trade-offs?*-->
JWT (JSON Web Tokens) was chosen for its stateless nature, which fits well with a REST API. 
Tokens are generated on login/register and must be included in the Authorization header as a Bearer token for write operations. 
The token expires after 24 hours.

Alternatives include session-based authentication (stateful, requires server-side storage) and API keys (simpler but 
less flexible for user-specific permissions).
JWT was preferred because it is stateless, widely supported, and suitable for future frontend integration.

### API Design

<!-- **REST students:**
- *How did you implement HATEOAS? How does it improve API discoverability?*
- *How did you structure your resource URLs and use HTTP methods/status codes?*
-->
HATEOAS is implemented using Spring HATEOAS. Each resource response includes a _links object with relevant hypermedia links. 
For example, a book response includes links for self, update, and delete with their respective HTTP methods specified.
This improves discoverability by allowing clients to navigate the API without hardcoding URLs.

Resources are structured as /api/v1/{resource} with nested routes for related resources (e.g. /api/v1/authors/{id}/books). 
Standard HTTP methods are used: GET for retrieval, POST for creation, PUT for updates, and DELETE for deletion.
Status codes follow REST conventions: 200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 404 Not Found, 
and 409 Conflict.

### Error Handling

<!-- *How does your API handle errors? Describe the format and consistency of your error responses.* -->
All errors follow a consistent JSON format, see example below:  
```json
{
"status": 404,
"message": "Book with id 1 not found",
"timestamp": "2026-03-10T12:00:00"
}
```
Errors are handled centrally via a GlobalExceptionHandler (@ControllerAdvice) that catches specific exceptions 
(ResourceNotFoundException, InvalidCredentialsException, UsernameAlreadyExistsException) as well as validation 
errors and a generic fallback for unexpected errors.

### Code Quality

This project uses Google's Checkstyle configuration to enforce code style consistency.
The ruleset is followed closely, with a deliberate exception documented in
`checkstyle-suppressions.xml`:

- **Abbreviations** — `DTO` is a widely accepted Java convention and is exempt from
  Google's abbreviation rules

Beyond automated checks, the code aims to follow clean code principles: meaningful naming, single responsibility, 
and keeping methods focused and readable.

## Core Technologies Used

<!--*List the technologies you chose and briefly explain why:*-->

- Java 21 — LTS version with modern language features
- Spring Boot 4.0.3 — framework for rapid API development with built-in security, validation and data access
- Spring Security + JWT (jjwt) — stateless authentication
- Spring HATEOAS — hypermedia links in responses
- Hibernate/JPA — ORM for database interaction
- PostgreSQL 16 — robust relational database
- Docker + Docker Compose — containerization for consistent deployment
- Nginx — reverse proxy handling HTTPS termination
- Springdoc OpenAPI 3 — automatic Swagger documentation generation

## Reflection

<!--*What was hard? What did you learn? What would you do differently?*-->
Since I chose to use several tools that were new to me, and also had a new IDE, it was a bit more of an 
uphill climb than usual in this project. Fortunately, there is a lot of documentation in various forms, 
which was really needed, so even if I only just scratched the surface, it feels positive to have tried 
both Spring Boot and PostgreSQL.

With Spring Boot and Hibernate, the annotations were what felt most complicated and also difficult 
when it comes to clean code. I got to see a glimpse of what is apparently called "annotation hell", 
and in combination with Checkstyle, my code became unusually difficult to read in some places.

I still felt that everything went quite well, and I am generally happy with the stack I chose.
It would have been fun to set up a CI/CD pipeline as well, but due to the other course I am 
currently taking, there is unfortunately too little time. My hope is that I will be able to set one 
up when I move the API over to Github later. 


## Acknowledgements

<!--*Resources, attributions, or shoutouts.*-->
- [Books Dataset](https://www.kaggle.com/datasets/elvinrustam/books-dataset/) by Elvin Rustam on Kaggle
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/index.html)
- [Cumulus guide](https://coursepress.lnu.se/manual/cumulus/how-to-connect-to-a-cumulus-server)
- Various guides on Youtube
- Claude Sonnet 4.6
- My earlier projects
- A LOT of chocolate

## Requirements

<!-- See [all requirements in Issues](../../issues/). Close issues as you implement them. Create additional issues for any custom functionality. See [TESTING.md](TESTING.md) for detailed testing requirements.
-->
### Functional Requirements — Common
<!-- :white_check_mark: -->

| Requirement | Issue | Status |
|---|---|---|
| Data acquisition — choose and document a dataset (1000+ data points) | [#1](../../issues/1) | :white_check_mark: |
| Full CRUD for primary resource, read-only for secondary resources | [#2](../../issues/2) | :white_check_mark: |
| JWT authentication for write operations | [#3](../../issues/3) | :white_check_mark: |
| Error handling (400, 401, 404 with consistent format) | [#4](../../issues/4) | :white_check_mark: |
| Filtering and pagination for large result sets | [#17](../../issues/17) | :white_check_mark: |

### Functional Requirements — REST

| Requirement | Issue | Status |
|---|---|---|
| RESTful endpoints with proper HTTP methods and status codes | [#12](../../issues/12) | :white_check_mark: |
| HATEOAS (hypermedia links in responses) | [#13](../../issues/13) | :white_check_mark: |

<!-- ### Functional Requirements — GraphQL

| Requirement | Issue | Status |
|---|---|---|
| Queries and mutations via single `/graphql` endpoint | [#14](../../issues/14) | :white_large_square: |
| At least one nested query | [#15](../../issues/15) | :white_large_square: |
| GraphQL Playground available | [#16](../../issues/16) | :white_large_square: |
-->
### Non-Functional Requirements

| Requirement | Issue | Status |
|---|---|---|
| API documentation (Swagger/OpenAPI or Postman) | [#6](../../issues/6) | :white_check_mark: |
| Automated Postman tests (20+ test cases, success + failure) | [#7](../../issues/7) | :white_check_mark: |
| CI/CD pipeline running tests on every commit/MR | [#8](../../issues/8) | :white_large_square: |
| Seed script for sample data | [#5](../../issues/5) | :white_check_mark: |
| Code quality (consistent standard, modular, documented) | [#10](../../issues/10) | :white_check_mark: |
| Deployed and publicly accessible | [#9](../../issues/9) | :white_check_mark: |
| Peer review reflection submitted on merge request | [#11](../../issues/11) | :white_large_square: |


