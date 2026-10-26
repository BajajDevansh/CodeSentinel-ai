# CodeSentinel Architecture & System Design

## 1. System Overview
CodeSentinel is an AI-powered software engineering platform designed for codebase comprehension, automated testing gap closure, security hardening, and regression risk analysis.

```
+-------------------------------------------------------------------+
|               CodeSentinel Native Android Application             |
|   (Jetpack Compose M3, MVVM, Room DB, Retrofit, Coroutines/Flow)  |
+---------------------------------+---------------------------------+
                                  | REST / HTTPS (JWT)
                                  v
+-------------------------------------------------------------------+
|               Spring Boot 3.3.4 Modular Monolith Backend          |
|  - Spring Security (JWT Auth)      - Analysis Orchestration Engine|
|  - Repository Ownership Guard      - Sandboxed Execution Runner   |
|  - OpenAPI / Swagger Docs          - WebSocket Realtime Events    |
+-------------------+-------------------------------+---------------+
                    | MongoDB Driver                | WebClient (HTTP)
                    v                               v
+-----------------------------+  +----------------------------------+
|      MongoDB Database       |  |     Python 3.12 FastAPI AI       |
|  - Users & Repositories     |  |  - AST Symbol Parser (Java/Py)   |
|  - Analysis Jobs & Findings |  |  - NetworkX Knowledge Graph      |
|  - Test Coverage Reports    |  |  - Hybrid RAG (BM25 + Dense RRF) |
|  - Change Impact History    |  |  - Regression Risk Model         |
+-----------------------------+  +----------------------------------+
```

## 2. Differentiating Core Pipelines

### Pipeline A: Coverage-Guided Test Gap Analyzer
1. Source tree indexed into symbol ASTs (Classes, Methods, Branch points).
2. Existing test suites run in an isolated Docker container with JaCoCo / pytest-cov coverage agents.
3. Uncovered execution branches mapped back to AST nodes.
4. Risk prioritization formula assigns weights based on caller depth and security sensitivity.
5. High-risk targets synthesize unit test cases with boundary input permutations.
6. Generated tests execute in sandbox; coverage delta (e.g., +14.3% line coverage) is measured and validated.

### Pipeline B: Change Impact & Regression Risk Analyzer
1. Git diff parsed into modified symbol tokens.
2. Knowledge Graph queried using BFS/DFS propagation (Caller/Callee transitive closure).
3. Explainable Risk Score calculated:
   `RiskScore = (DirectSymbols * 12.0) + (TransitiveCallers * 6.5) + SecurityBoundaryPenalty`
4. Associated test cases discovered and recommended for validation before pull request merge.
