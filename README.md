# Demo Spring Boot Application

A minimal Spring Boot app used to get comfortable with IntelliJ IDEA: building,
running, testing, and managing dependencies.

## Endpoints

| Method | Path                            | Description                                        |
|--------|---------------------------------|----------------------------------------------------|
| GET    | `/api/hello`                    | Fixed greeting payload                              |
| GET    | `/api/random?min=1&max=100`     | Random integer in `[min, max]`; both params optional |

Examples:

    curl http://localhost:8080/api/hello
    # {"message":"Hello, world!","status":"ok"}

    curl http://localhost:8080/api/random
    # {"value":90,"min":1,"max":100}

    curl "http://localhost:8080/api/random?min=1&max=6"
    # {"value":2,"min":1,"max":6}

    curl "http://localhost:8080/api/random?min=10&max=1"
    # HTTP 400
    # {"error":"invalid range","message":"min (10) must not be greater than max (1)"}

## Project layout

    src/main/java/com/example/demo/
      DemoApplication.java                  <- entry point (main method)
      controller/HelloController.java
      controller/RandomController.java      <- the new endpoint
      service/RandomNumberService.java      <- the logic, kept out of the controller

    src/test/java/com/example/demo/
      DemoApplicationTests.java             <- @SpringBootTest, whole context
      service/RandomNumberServiceTest.java  <- plain JUnit, no Spring, fast
      controller/HelloControllerTest.java   <- @WebMvcTest + MockMvc
      controller/RandomControllerTest.java  <- @WebMvcTest + MockMvc + @MockBean

The logic lives in a service rather than the controller so it can be unit-tested
without starting Spring at all, and so the controller test can mock it and get a
predictable "random" number.

---

## Working in IntelliJ IDEA

### 1. Open the project

`File > Open`, select the folder containing `pom.xml` (not the `pom.xml` itself),
and choose **Open as Project**. IntelliJ detects Maven and starts importing.

### 2. Set the JDK (do this first)

This project targets Java 17. In `File > Project Structure > Project`:

- **SDK**: pick a JDK 17 or newer. If none is listed, choose
  `Add SDK > Download JDK...` and grab Temurin/Corretto **17** — IntelliJ
  downloads and configures it for you, no separate installer needed.
- **Language level**: 17.

Also check `Settings > Build, Execution, Deployment > Build Tools > Maven >
Importing > JDK for importer` points at the same JDK.

### 3. Install / refresh dependencies

Dependencies are declared in `pom.xml`; Maven downloads them into `~/.m2/repository`.

- Open the **Maven** tool window: `View > Tool Windows > Maven` (right edge of the IDE).
- Click the **Reload All Maven Projects** button (circular arrows) whenever you
  edit `pom.xml`. IntelliJ also shows a small floating "Load Maven Changes"
  notification after an edit — clicking it does the same thing.
- To add a new library: add a `<dependency>` block inside `<dependencies>` in
  `pom.xml`, then reload. Inside the `<dependency>` tag, `Ctrl+Space` gives you
  autocomplete for groupId/artifactId/version straight from Maven Central.
- Under the Maven tool window, expand `demo > Dependencies` to see the full
  resolved tree, which is the quickest way to spot a version conflict.

Offline/first import is the slow part; after that it is cached.

### 4. Run the application

Three equivalent ways:

- Open `DemoApplication.java` and click the green ▶ arrow in the gutter next to
  the `main` method, then **Run 'DemoApplication'**. IntelliJ creates a run
  configuration named `DemoApplication` you can reuse from the toolbar.
- Maven tool window: `demo > Plugins > spring-boot > spring-boot:run`.
- Terminal: `mvn spring-boot:run`.

The app starts on port 8080 (`src/main/resources/application.properties`). Watch
the **Run** tool window for `Started DemoApplication in ... seconds`. Stop it
with the red square. If the port is busy you will see
`Web server failed to start. Port 8080 was already in use` — change `server.port`
or stop the other process.

To hit the endpoint without leaving the IDE, create a scratch HTTP file:
`Ctrl+Alt+Shift+Insert > HTTP Request`, type `GET http://localhost:8080/api/random`,
and click the ▶ next to the line.

### 5. Run the tests

- **One test method**: click the ▶ in the gutter next to the method name.
- **One test class**: click the ▶ next to the class name, or `Ctrl+Shift+F10`
  with the cursor inside the class.
- **All tests**: right-click the `src/test/java` folder in the Project view >
  **Run 'All Tests'**. Or use the Maven tool window: `demo > Lifecycle > test`.
- **Re-run only what failed**: in the Run tool window, the ⟳ with the orange
  arrows re-runs failed tests only.

The test results panel shows a tree of passed/failed tests. Click a failed test
to see the assertion diff, and double-click a line in the stack trace to jump
straight to the offending code.

Coverage: `Run > Run 'All Tests' with Coverage` colours the gutter green/red per
line in the editor.

### 6. Build

- Maven tool window: `demo > Lifecycle > package` produces
  `target/demo-0.0.1-SNAPSHOT.jar` (an executable fat jar). `clean` first if you
  want a fresh build.
- `Build > Build Project` (`Ctrl+F9`) only compiles — it does not produce the jar.
- Terminal equivalent: `mvn clean package`, then
  `java -jar target/demo-0.0.1-SNAPSHOT.jar`.

`mvn package` runs the tests as part of the build, so a failing test fails the
build. `mvn package -DskipTests` skips them.

---

## Command line (outside IntelliJ)

There is no Maven wrapper (`mvnw`) in this repo, so you need Maven on your PATH —
or just use IntelliJ's bundled one via the Maven tool window.

    mvn clean test        # run the tests
    mvn clean package     # build the jar (runs tests too)
    mvn spring-boot:run   # run the app

## Note on JDK versions

`pom.xml` overrides `byte-buddy.version` to 1.17.5. Spring Boot 3.1.6 ships an
older Byte Buddy that Mockito uses to build mocks, and it refuses to run on
JDK 23+ (`Java 24 (68) is not supported by the current version of Byte Buddy`).
The override keeps the tests green on both Java 17 and newer JDKs. If you pin the
project to a JDK 17 SDK, the override is harmless.
