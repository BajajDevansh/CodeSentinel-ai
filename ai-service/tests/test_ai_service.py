import pytest
from core.graph import RepositoryKnowledgeGraph
from core.retriever import HybridRetriever
from core.analyzer import StaticAnalyzer
from core.change_impact import ChangeImpactAnalyzer

def test_knowledge_graph_traversal():
    kg = RepositoryKnowledgeGraph()
    kg.add_symbol("ClassA", "CLASS", "path/A.java")
    kg.add_symbol("ClassB", "CLASS", "path/B.java")
    kg.add_relationship("ClassA", "ClassB", "CALLS")

    callers = kg.find_callers("ClassB")
    assert "ClassA" in callers

    impacted = kg.get_impacted_neighbors(["ClassB"], max_depth=1)
    assert "ClassA" in impacted

def test_static_analyzer_rules():
    code = 'String password = "super_secret_token_123456789";'
    findings = StaticAnalyzer.scan_file("Test.java", code)
    assert len(findings) == 1
    assert findings[0]["severity"] == "CRITICAL"
    assert "Hardcoded Secret" in findings[0]["title"]

def test_change_impact_scoring():
    kg = RepositoryKnowledgeGraph()
    kg.add_symbol("JwtAuth", "CLASS", "auth/JwtAuth.java")
    res = ChangeImpactAnalyzer.analyze(kg, ["JwtAuth"])
    assert res["regression_risk_score"] > 20.0
    assert "risk_factors" in res
    assert len(res["risk_factors"]) >= 2
