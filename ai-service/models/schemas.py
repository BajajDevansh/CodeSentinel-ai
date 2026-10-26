from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any

class IngestRequest(BaseModel):
    repo_url: str
    branch: Optional[str] = "main"
    github_token: Optional[str] = None

class IngestResponse(BaseModel):
    status: str
    repo_id: str
    file_count: int
    lines_of_code: int
    symbol_count: int
    languages: Dict[str, int]

class Citation(BaseModel):
    file_path: str
    start_line: int
    end_line: int
    symbol_name: str
    snippet: str
    retrieval_method: str

class QARequest(BaseModel):
    repo_id: str
    query: str
    commit_sha: Optional[str] = "HEAD"

class QAResponse(BaseModel):
    answer: str
    grounded_in_evidence: bool
    citations: List[Citation]

class FindingItem(BaseModel):
    title: str
    category: str
    severity: str
    confidence: float
    tool_confirmed: bool
    detector: str
    file_path: str
    start_line: int
    end_line: int
    snippet: str
    explanation: str
    impact: str
    recommended_remediation: str
    cwe_rules: List[str]

class FindingsResponse(BaseModel):
    repo_id: str
    findings: List[FindingItem]

class DocGapItem(BaseModel):
    symbol_name: str
    symbol_type: str
    file_path: str
    line_number: int
    priority: str
    reason: str
    proposed_doc: str

class DocGapsResponse(BaseModel):
    repo_id: str
    gaps: List[DocGapItem]

class TestGapAnalysisResponse(BaseModel):
    repo_id: str
    baseline_coverage: float
    uncovered_targets: List[Dict[str, Any]]
    suggested_tests: List[Dict[str, Any]]

class ChangeImpactRequest(BaseModel):
    repo_id: str
    base_commit: str
    target_commit: str

class ChangeImpactResponse(BaseModel):
    repo_id: str
    regression_risk_score: float
    risk_category: str
    directly_affected_count: int
    transitively_affected_count: int
    risk_factors: List[Dict[str, Any]]
    affected_components: List[Dict[str, Any]]
    affected_tests: List[str]
    missing_tests: List[str]
