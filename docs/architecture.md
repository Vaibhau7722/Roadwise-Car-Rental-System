# Architecture

The application is a single deployable WAR with a conventional MVC split:

1. JSP pages under `WEB-INF/views` render request attributes with JSTL and escaped output.
2. `FrontController` dispatches the documented routes and calls services. It contains no SQL.
3. Services validate input and implement pricing, booking, cancellation, and fleet rules.
4. JDBC repositories own SQL for account/profile operations, car search, reports, dashboards, and booking/rental transactions.
5. Hibernate repositories own car persistence and provide ORM CRUD. Hibernate validates the schema on startup; it does not create or drop tables.
6. `SecurityFilter` enforces the session role and checks a session CSRF token for every POST.

## Booking transaction

`JdbcBookingRepository.create` opens one JDBC connection and transaction, locks the selected car with `SELECT ... FOR UPDATE`, checks its current status, evaluates reservation overlap with the half-open date predicate, then inserts the booking and pending payment before committing. Every booking creation uses this path. The lock serializes bookings for one vehicle, so two requests cannot both pass a stale availability read. Active rentals with no return timestamp are considered occupied through their actual return, even when their original planned return date has passed.

## Booking and pricing rules

Dates use `LocalDate`. Rental days equal the day count between pickup and return, so October 20 through October 23 is three days. The interval is `[pickup, return)`. The agreed daily rate and base amount are stored in the booking. Return charges use a configured `$25.00` late fee per calendar day past the planned return date. Payment records are simulated; no payment provider is integrated.

## Data boundaries

Passwords are stored as BCrypt hashes. A customer identity comes from the authenticated session; booking detail, invoice, and cancellation queries also include that customer ID. Cars with history are retained and deactivated by status rather than deleted. SQL values are bound through prepared statements.
