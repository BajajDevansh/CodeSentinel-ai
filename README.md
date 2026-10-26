# CodeSentinel: AI-Powered Software Engineering Assistant

A production-grade, repository-aware software engineering platform that combines deterministic code-analysis tools, AST symbol parsing, directed call-graph algorithms, hybrid retrieval (Keyword + Dense + Graph Expansion), automated test gap closure, regression risk analysis, and a native Android application.

---

## 1. System Architecture

```
                       [ Android Native App (Kotlin, Compose, Room, M3) ]
                                                |
                                        REST (JWT Bearer)
                                                v
               [ Spring Boot 3.3.4 Backend (Java 21, Security, MongoDB, WebClient) ]
                        /                                             \
                       v                                               v
              [ MongoDB Database ]                         [ Python 3.12 AI Service ]
             (Users, Repos, Scans,                          (FastAPI, AST Parsing,
             Findings, Test Reports,                         NetworkX Call Graph,
             Change Impact History)                          Hybrid RRF Retriever,
                                                             Static & Risk Engine)
```

### Module Responsibilities

1. **Android Application (`app/`)**:
   - Modern Material 3 UI with Edge-to-Edge window insets and dark cyber theme.
   - MVVM architecture with Kotlin Coroutines, StateFlow, and Room DB caching for offline resilience.
   - Codebase Q&A with clickable source citations and cited snippet inspection.
   - Interactive Canvas Architecture & Call Graph Explorer with node highlight.
   - Coverage-Guided Test Gap Analyzer with measured coverage delta indicators.
   - Change Impact & Regression Risk Analyzer with explainable risk breakdown.
   - Unified code diff review with developer approval flow.

2. **Backend (`backend/`)**:
   - Spring Boot 3.3.4 modular monolith with Java 21 Virtual Threads.
   - Spring Security with stateless HMAC-SHA256 JWT authentication and refresh token rotation.
   - Spring Data MongoDB persistence with automated index bootstrap.
   - WebClient integration with Python intelligence services.
   - OpenAPI / Swagger 3.0 documentation (`/swagger-ui.html`).

3. **AI Intelligence Engine (`ai-service/`)**:
   - Python 3.12+ FastAPI microservice.
   - AST symbol parser extracting classes, methods, docstrings, and signatures across Java, Kotlin, and Python.
   - Directed Knowledge Graph (NetworkX) tracking `CALLS`, `IMPORTS`, `DEFINES`, and `TESTS` edges.
   - Hybrid Retriever combining exact token matching, dense similarity, and Reciprocal Rank Fusion (RRF).
   - Change Impact Analyzer calculating explainable regression risk scores based on graph reachability and security boundaries.

---

## 2. Monorepo Directory Layout

```
code-sentinel/
├── android/                        # Android application project & build files
│   ├── app/                        # Native Android app (Jetpack Compose M3, MVVM, Room DB)
│   │   ├── src/main/java/com/example/
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/               # Room entities, DAOs, Retrofit API client
│   │   │   ├── ui/                 # Material 3 screens, theme, components
│   │   │   └── ui/viewmodel/       # StateFlow MVVM ViewModel
│   │   └── build.gradle.kts
│   ├── build.gradle.kts            # Project Gradle build script
│   ├── settings.gradle.kts         # Gradle settings & version catalog
│   └── gradle.properties
├── backend/                        # Spring Boot 3.3.4 modular monolith (Java 21)
│   ├── src/
│   │   ├── main/java/com/codesentinel/backend/
│   │   │   ├── controller/         # Auth, Repo, Analysis, Chat, Tests, Impact, Patch
│   │   │   ├── service/            # Business logic & Python client
│   │   │   ├── security/           # JWT Provider, AuthFilter, SecurityConfig
│   │   │   ├── model/              # MongoDB document models
│   │   │   └── repository/         # Spring Data MongoDB interfaces
│   │   └── test/java/              # Unit & integration test suites
│   ├── pom.xml                     # Maven project descriptor
│   └── Dockerfile                  # Container build for backend
├── ai-service/                     # Python 3.12 FastAPI AI Service
│   ├── api/                        # Route handlers (/ingest, /qa, /impact, etc.)
│   ├── core/                       # AST parser, NetworkX graph, Hybrid RAG retriever
│   ├── models/                     # Pydantic schemas
│   ├── tests/                      # Pytest test cases
│   ├── requirements.txt            # Python dependencies
│   ├── config.py
│   ├── main.py
│   └── Dockerfile                  # Container build for AI service
├── infrastructure/                 # Multi-container orchestration & Mongo setup
│   ├── docker-compose.yml          # Multi-service composition
│   └── mongo-init.js               # MongoDB collection initialization & indexes
├── docs/                           # Architecture, OpenAPI & evaluation framework
│   ├── architecture.md
│   ├── openapi.yaml
│   └── evaluation_framework.md
├── sample-repos/                   # Fixtures for testing analyzers
└── README.md                       # Complete project documentation & setup guide
```

---

## 3. Quickstart & Deployment

### Option A: Launch Full Infrastructure via Docker Compose
```bash
cd infrastructure
docker compose up --build -d
```
Services will become available at:
- **Backend Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Backend OpenAPI Docs**: `http://localhost:8080/v3/api-docs`
- **Python AI Service Health**: `http://localhost:8000/health`
- **MongoDB**: `localhost:27017`

### Option B: Run Python AI Service Directly
```bash
cd ai-service
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

### Option C: Run Spring Boot Backend Directly
```bash
cd backend
mvn spring-boot:run
```

### Option D: Build and Run Android Application
Open the root directory in Android Studio. Build and deploy to an Android 12+ device or emulator:
```bash
gradle :app:assembleDebug
```

---

## 4. Running Automated Tests

- **Android Unit & Robolectric Tests**:
  ```bash
  gradle :app:testDebugUnitTest
  ```
- **Backend Spring Boot Tests**:
  ```bash
  cd backend && mvn test
  ```
- **Python AI Intelligence Tests**:
  ```bash
  cd ai-service && pytest
  ```
