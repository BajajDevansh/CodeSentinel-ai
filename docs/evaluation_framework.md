# CodeSentinel Evaluation & Benchmark Methodology

## 1. Evaluation Dimensions

### 1.1 Retrieval-Augmented Generation (Hybrid RAG)
- **Metric**: Recall@K (K=3, 5) and Mean Reciprocal Rank (MRR).
- **Comparison**:
  - Baseline 1: Keyword-only (BM25)
  - Baseline 2: Dense-only (Vector cosine)
  - CodeSentinel: Hybrid RRF (Keyword + Dense + Graph Expansion)
- **Ground Truth**: Manually annotated dataset of 50 software engineering questions mapping to exact file lines and symbol signatures in standard open source repos (Spring PetClinic, FastAPI RealWorld).

### 1.2 Test Gap Coverage & Mutation Score
- **Metric**:
  - Line Coverage Delta: $\Delta Cov_{line} = Cov_{post} - Cov_{base}$
  - Branch Coverage Delta: $\Delta Cov_{branch} = Cov_{post} - Cov_{base}$
  - PIT Mutation Score: percentage of introduced code mutants killed by generated tests.
- **Verification Rule**: No coverage increase is reported unless validated via actual JaCoCo/pytest-cov execution reports.

### 1.3 Change Impact Precision and Recall
- **Ground Truth**: Verified regressions and affected components derived from Git commits and known issue bug-fixes.
- **Metric**:
  - Precision: $\frac{|\text{PredictedImpact} \cap \text{TrueImpact}|}{|\text{PredictedImpact}|}$
  - Recall: $\frac{|\text{PredictedImpact} \cap \text{TrueImpact}|}{|\text{TrueImpact}|}$
