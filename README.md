# Car Rental Management System

A Java 17 car-rental web application built with Jakarta Servlets 6, JSP/JSTL, Hibernate ORM 6, JDBC, MySQL 8, Maven, and Bootstrap 5. It is packaged as a WAR for Apache Tomcat 10.1. The code is intentionally organized as a small MVC application suitable for learning and interview discussion.

## Features

- Customer registration, BCrypt password hashing, login/logout, session ID rotation, role checks, CSRF tokens, profile and password updates.
- Searchable fleet with specifications, categories, daily prices, and local image fallback.
- Admin fleet CRUD, status changes, maintenance status, deactivation, booking search/cancellation, rental pickup and return, and database-driven dashboard/reports.
- Customer-owned booking list/details, cancellation of future confirmed bookings, and printable invoice.
- Date-aware booking checks using half-open intervals and a JDBC transaction that locks the car row before checking availability and inserting the booking.
- BigDecimal pricing. A booking stores its agreed rate; late returns accrue `$25.00` per calendar day beyond the planned return date.
- Demo payment records only. No real payment provider is connected.

## Technology stack

Java 17, Jakarta Servlet 6.0, JSP 3.1, JSTL 3, Hibernate ORM 6.6, JDBC, HikariCP, MySQL Connector/J 9.1, MySQL 8, Maven, Tomcat 10.1, Bootstrap 5, Bootstrap Icons, BCrypt, Logback, and JUnit 5.

## Architecture

`FrontController` dispatches HTTP routes to services. Services validate requests and implement business rules. JDBC repositories own account, booking/rental transaction, search, dashboard, and report SQL. Hibernate repositories persist car entities and validate mapped tables against the schema. JSPs under `WEB-INF/views` use JSTL and EL; `SecurityFilter` handles authentication, role authorization, and CSRF validation. Details are in [docs/architecture.md](docs/architecture.md).

```text
src/main/java/com/carrental/
  controller/  filter/  service/  dao/jdbc/  dao/hibernate/
  entity/  model/  exception/  util/
src/main/resources/       Hibernate and Logback configuration
src/main/webapp/          JSP views, static assets, and web.xml
src/test/java/            Independent service and rule tests
database/                 Schema, demo seed, and reporting SQL
docs/                     Architecture and interview notes
```

## Prerequisites

- JDK 17 (or newer to compile with release 17)
- Maven 3.9+
- MySQL 8.x
- Apache Tomcat 10.1.x

## Database setup

Create the schema, then load the sample records. From the project folder:

```powershell
mysql -u root -p < database/schema.sql
mysql -u root -p car_rental_db < database/seed.sql
```

`schema.sql` creates `car_rental_db` and its seven tables. `seed.sql` provides one admin, five customers, ten cars, bookings, completed rentals, payments, and a maintenance record. Seed data dates are sample dates, so future reservation dates should be adjusted when using the demo at a later date.

### Database configuration

The application reads these process environment variables; Maven does not load a `.env` file:

- `DB_URL` — JDBC URL, for example `jdbc:mysql://localhost:3306/car_rental_db?serverTimezone=UTC`
- `DB_USERNAME` — MySQL user with access to `car_rental_db`
- `DB_PASSWORD` — password for that user (may be empty for a local disposable database)

PowerShell example for the current terminal:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/car_rental_db?serverTimezone=UTC"
$env:DB_USERNAME = "car_rental_app"
$env:DB_PASSWORD = "set-your-local-password"
```

Use a dedicated local user and do not commit actual credentials. Hibernate uses `hibernate.hbm2ddl.auto=validate`; it does not create, update, or drop the schema. On startup the application logs a useful error if configuration is missing or database/schema initialization fails.

## Build and tests

From the project directory:

```powershell
mvn clean test
mvn package
```

The generated artifact is `target/car-rental-management.war`. Tests cover duration and price calculations, invalid dates, late fees, cancellation eligibility, and car-bookability rules. Database integration tests require a reachable, configured MySQL instance; the included tests do not need a database.

## Deploy and run

1. Set the database environment variables in the same environment that starts Tomcat.
2. Build with `mvn package`.
3. Copy `target/car-rental-management.war` to `$CATALINA_HOME/webapps/` (or the Tomcat `webapps` directory on Windows).
4. Start Tomcat and inspect its logs for the database-ready message.
5. Open [http://localhost:8080/car-rental-management/](http://localhost:8080/car-rental-management/).

The context path follows the WAR filename. A local standalone Tomcat installation is required; the project does not require Docker.

### Demo credentials

- Admin: `admin@carrental.local` / `password`
- Seed customer accounts: `alice@example.com`, `ben@example.com`, `chloe@example.com`, `dev@example.com`, and `emil@example.com`; each demo password is `password`.

These demo-only accounts share a known seed hash and are not suitable for a public deployment. Change or remove them before exposing the application. New customers can register from the Create account page.

## Routes

All state-changing actions use POST. The security filter requires a CSRF token for each POST.

| Method | Route | Purpose |
|---|---|---|
| GET | `/` | Public landing page |
| GET | `/cars` | Search available cars by brand/model, category, and maximum daily rate |
| GET | `/cars/details?id={id}` | Car specifications and booking form |
| GET/POST | `/login` | Sign in |
| GET/POST | `/register` | Customer registration |
| POST | `/logout` | Invalidate the session |
| GET | `/customer/dashboard` | Customer booking summary |
| GET | `/customer/bookings` | Own bookings |
| POST | `/customer/bookings/create` | Create a locked, date-checked booking |
| POST | `/customer/bookings/cancel` | Cancel an eligible owned booking |
| GET | `/customer/bookings/details?id={id}` | Own booking details |
| GET | `/customer/profile` | Profile and password forms |
| POST | `/customer/profile/update` | Update name and phone |
| POST | `/customer/password/change` | Change password |
| GET | `/customer/invoices/details?id={id}` | Own printable invoice |
| GET | `/admin/dashboard` | Live database metrics and recent bookings |
| GET | `/admin/cars` | Search and manage fleet |
| GET/POST | `/admin/cars/create` | Add a car |
| GET | `/admin/cars/edit?id={id}` | Edit form |
| POST | `/admin/cars/update` | Update car details/status |
| POST | `/admin/cars/deactivate` | Retain history and mark a car inactive |
| GET | `/admin/bookings` | Search and filter bookings |
| GET | `/admin/bookings/details?id={id}` | Booking and customer details |
| POST | `/admin/bookings/cancel` | Cancel eligible future booking |
| GET | `/admin/rentals` | Rental pickup/return queue |
| POST | `/admin/rentals/pickup` | Record pickup and activate rental |
| POST | `/admin/rentals/return` | Record return, late fee, and simulated payment |
| GET | `/admin/reports/revenue` | Monthly paid rental revenue |
| GET | `/admin/reports/utilization` | Most frequently rented cars |

## Troubleshooting

- **Database configuration missing:** Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in the environment that launches Tomcat, then restart it.
- **Unknown database/table:** Run `database/schema.sql` and `database/seed.sql` in order. Hibernate validates rather than creates the schema.
- **Connection refused:** Confirm MySQL is running, the URL/port are correct, and the MySQL user can connect from the Tomcat host.
- **WAR fails on Tomcat:** Use Tomcat 10.1 and Java 17+. Tomcat 9 uses the older `javax.servlet` namespace and is incompatible.
- **Maven cannot download dependencies:** Check Maven Central/network access and rerun `mvn clean test`.
- **A reservation is rejected:** Confirm the car status is `AVAILABLE`, dates are future/ordered, and no confirmed or active rental overlaps the requested half-open date range.
- **Styles are missing offline:** Bootstrap and Bootstrap Icons are loaded from their public CDN; the app's custom styling and fallback car illustration are local.

## Interview preparation

See [docs/interview-questions.md](docs/interview-questions.md) for 35 concise questions and answers covering Java, JDBC, Hibernate, SQL, Servlets/JSP, sessions, MVC, transactions, booking availability, and project choices.
