# Java Developer Interview Questions

1. **What is encapsulation?** Keep state private and expose controlled operations through methods; this protects invariants.
2. **What is inheritance?** A class reuses and specializes behavior from a parent type. Prefer composition when it models the relationship more clearly.
3. **What is polymorphism?** Code can call a common interface while runtime implementations provide different behavior.
4. **What is abstraction?** Expose the operations a caller needs while hiding implementation details.
5. **Why use `BigDecimal` for money?** Decimal values avoid binary floating-point rounding errors and can be rounded with explicit rules.
6. **What does `final` mean for a class, field, and method?** A final class cannot be extended, a final field cannot be reassigned, and a final method cannot be overridden.
7. **What is a checked exception?** An exception the compiler requires a method to catch or declare; JDBC `SQLException` is checked.
8. **Why use try-with-resources in JDBC?** It closes connections, statements, and result sets even when an exception occurs.
9. **What is a `PreparedStatement`?** A parameterized SQL statement that separates input values from SQL syntax.
10. **How does this application prevent SQL injection?** User values are bound as parameters; query structure is fixed in repository code.
11. **What is a JDBC transaction?** A group of database statements committed together or rolled back together.
12. **Why lock the car row during booking creation?** Competing requests serialize on the same car row before they check overlaps and insert.
13. **What is a SQL join?** A join combines related rows using a relationship such as a foreign key.
14. **What is the difference between an inner and left join?** Inner joins keep matching pairs; left joins retain every row from the left side.
15. **What is an aggregate query?** A query that summarizes rows with functions such as `COUNT` and `SUM`, often grouped by a key.
16. **What is an entity in Hibernate?** A Java object mapped to a database table and managed by the persistence context.
17. **What is `SessionFactory`?** A thread-safe, application-wide Hibernate object used to create sessions.
18. **What is a Hibernate session?** A short-lived unit of work that tracks entities and communicates with the database.
19. **What is lazy loading?** Related data is fetched only when accessed, which can reduce unnecessary queries.
20. **How can N+1 queries be reduced?** Fetch required relationships with a join fetch or a purpose-built query instead of loading each relationship individually.
21. **What is an ORM?** Object-relational mapping connects object models with relational tables and relationships.
22. **What is the role of a Servlet?** It accepts HTTP requests, invokes application logic, and selects a response or view.
23. **What is a JSP?** A server-side view technology that renders HTML, commonly with EL and tag libraries.
24. **Why avoid scriptlets in JSP?** They mix Java business logic into views and make pages harder to maintain and test.
25. **What is the MVC pattern?** Model holds application data/rules, controller coordinates requests, and view presents results.
26. **How does session authentication work here?** Successful login stores a small user identity in the server-side HTTP session; filters check it on protected routes.
27. **Why rotate the session ID after login?** It reduces session-fixation risk by giving the authenticated session a fresh identifier.
28. **What is CSRF?** Cross-site request forgery causes a browser to submit an unwanted state-changing request; a per-session token helps reject such requests.
29. **Why hash passwords with BCrypt?** BCrypt is a salted, intentionally expensive password hash designed to slow offline guessing.
30. **How are overlapping rentals identified?** For half-open intervals, overlap exists when existing start is before requested end and existing end is after requested start.
31. **Why store the daily rate on each booking?** Historical agreements remain stable if the fleet's current price changes later.
32. **Why are cars deactivated instead of deleted?** Foreign keys preserve booking, rental, and payment history for reporting and audit.
33. **Why use both JDBC and Hibernate?** JDBC handles explicit reporting and carefully controlled transactions; Hibernate handles mapped entity persistence and CRUD.
34. **Why separate the service and repository layers?** Services hold business rules while repositories isolate persistence details.
35. **How is late rental pricing calculated?** Rental cost is agreed rate times rental days; late fee is configured daily fee times late days.
