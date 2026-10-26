import re
from typing import List, Dict, Any

class StaticAnalyzer:
    """Deterministic static analysis rules for code smells and security vulnerabilities."""

    RULES = [
        {
            "id": "SEC-001",
            "title": "Hardcoded Secret or Token",
            "category": "VULNERABILITY",
            "severity": "CRITICAL",
            "regex": re.compile(r'(?:api[_-]?key|secret|password|token)\s*[:=]\s*["\']([a-zA-Z0-9_\-]{16,})["\']', re.IGNORECASE),
            "explanation": "A high-entropy secret literal was found hardcoded in source code.",
            "impact": "Credentials may leak into version control and enable unauthorized access.",
            "remediation": "Extract secret into environment variables or secrets manager."
        },
        {
            "id": "SEC-002",
            "title": "SQL Injection via String Concatenation",
            "category": "VULNERABILITY",
            "severity": "HIGH",
            "regex": re.compile(r'(?:SELECT|INSERT|UPDATE|DELETE).*\+.*(?:request|param|input)', re.IGNORECASE),
            "explanation": "SQL statement is constructed via dynamic string concatenation with untrusted input.",
            "impact": "Allows attackers to bypass authentication or extract sensitive database contents.",
            "remediation": "Use parameterized queries or ORM criteria builders."
        },
        {
            "id": "SMELL-001",
            "title": "Empty Exception Catch Block",
            "category": "CODE_SMELL",
            "severity": "MEDIUM",
            "regex": re.compile(r'catch\s*\([A-Za-z0-9_]+\s+[A-Za-z0-9_]+\)\s*\{\s*\}'),
            "explanation": "Catch block silently swallows exceptions without logging or rethrowing.",
            "impact": "Hides critical runtime errors and impedes system debugging.",
            "remediation": "Log exception stack trace or wrap into domain exception."
        },
        {
            "id": "SMELL-002",
            "title": "Potential Null Dereference",
            "category": "BUG",
            "severity": "HIGH",
            "regex": re.compile(r'\.get\([^\)]+\)\.(?:toLowerCase|length|toString)'),
            "explanation": "Method call chained immediately on map lookup or nullable get without presence check.",
            "impact": "Can throw NullPointerException under unexpected inputs.",
            "remediation": "Use Optional, null-safe check (?.), or Objects.requireNonNull()."
        }
    ]

    @classmethod
    def scan_file(cls, file_path: str, content: str) -> List[Dict[str, Any]]:
        findings = []
        lines = content.splitlines()

        for rule in cls.RULES:
            for i, line in enumerate(lines, 1):
                if rule["regex"].search(line):
                    findings.append({
                        "title": rule["title"],
                        "category": rule["category"],
                        "severity": rule["severity"],
                        "confidence": 0.95,
                        "tool_confirmed": True,
                        "detector": "CodeSentinel RuleEngine",
                        "file_path": file_path,
                        "start_line": i,
                        "end_line": i,
                        "snippet": line.strip(),
                        "explanation": rule["explanation"],
                        "impact": rule["impact"],
                        "recommended_remediation": rule["remediation"],
                        "cwe_rules": [rule["id"]]
                    })
        return findings
