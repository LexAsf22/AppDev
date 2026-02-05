Purpose
-------
This file provides repository-specific guidance for AI coding agents to be immediately productive in the College-TimeTabling Spring Boot project.

Quick start
-----------
- **Build:** `./mvnw package` (Windows: `mvnw.cmd package`). Uses the included Maven wrapper in the repo root (`mvnw`, `mvnw.cmd`).
- **Run (dev):** `./mvnw spring-boot:run` or run the produced jar: `java -jar target/College-TimeTabling-0.0.1-SNAPSHOT.jar` after packaging.
- **Tests:** `./mvnw test`.

Important files / anchors
------------------------
- Main app entry: [src/main/java/com/timecraft/College/TimeTabling/CollegeTimeTablingApplication.java](src/main/java/com/timecraft/College/TimeTabling/CollegeTimeTablingApplication.java#L1-L20)
- Spring config: [src/main/resources/application.properties](src/main/resources/application.properties#L1-L40)
- OpenAPI spec for auth: [src/main/resources/specs/auth.service.yml](src/main/resources/specs/auth.service.yml#L1-L120)
- Flyway migrations: [src/main/resources/db/migration](src/main/resources/db/migration)
- JPA models: [src/main/java/com/timecraft/College/TimeTabling/model](src/main/java/com/timecraft/College/TimeTabling/model)
- Tests: [test/java](test/java)

Architecture notes (big-picture)
--------------------------------
- Spring Boot monolith with standard auto-configuration: the entrypoint is `CollegeTimeTablingApplication`.
- Persistence uses JPA (Jakarta `jakarta.persistence`) and expects PostgreSQL by default (see `application.properties`).
- DB migrations are implemented with Flyway under `src/main/resources/db/migration`, but note the `application.properties` contains conflicting `spring.flyway.enabled` entries — the last occurrence disables Flyway by default. Verify intended behavior before running migrations.
- OpenAPI specs live in `src/main/resources/specs` and are used as the contract for authentication endpoints (start with `auth.service.yml`).

Project-specific conventions & gotchas
------------------------------------
- Package paths use mixed-case directories (`com/timecraft/College/TimeTabling`). Preserve the package name `com.timecraft.College.TimeTabling` when generating new classes.
- The repo uses the Maven wrapper; prefer `./mvnw` to system `mvn` to ensure consistent builds.
- Database: `application.properties` points at `jdbc:postgresql://localhost:5432/timecraft_db` with username `postgres` and password `1234` — tests or local runs may rely on this; confirm with maintainers before changing credentials.
- OpenAPI spec issues discovered: `auth.service.yml` contains typos like `minLenght` and an empty response code entry — treat the spec as authoritative but validate and correct schema typos when generating code from it.

Integration points
------------------
- Database: PostgreSQL (via JDBC / Spring Data JPA). Flyway migrations live in `src/main/resources/db/migration`.
- API contract: OpenAPI YAMLs in `src/main/resources/specs` (e.g., `auth.service.yml`).

Examples to reference when making changes
---------------------------------------
- Add new JPA entities under `src/main/java/com/timecraft/College/TimeTabling/model` alongside `Employee.java` and `User.java`.
- Put SQL migrations as `V{n}__description.sql` in `src/main/resources/db/migration`.
- Update or extend OpenAPI contracts in `src/main/resources/specs` and keep them syntactically valid OpenAPI 3.0.

When in doubt
------------
- Run `./mvnw -e -X` for verbose Maven diagnostics.
- Double-check `application.properties` overrides; the file currently contains duplicated keys (e.g., Flyway on/off). Use the last definition as effective.

Please review and tell me any project details or workflows I missed (CI, external services, or non-default run steps) and I'll iterate.
