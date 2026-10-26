from typing import List, Dict, Any

class CoverageTestGapAnalyzer:
    """Correlates line and branch execution coverage with extracted AST symbols."""

    @staticmethod
    def identify_gaps(ast_methods: List[Dict[str, Any]], covered_lines: set) -> Dict[str, Any]:
        uncovered = []
        covered_count = 0

        for method in ast_methods:
            method_lines = set(range(method["start_line"], method["end_line"] + 1))
            overlap = method_lines.intersection(covered_lines)
            if len(overlap) == 0:
                risk = "HIGH" if "security" in method["file_path"].lower() or "auth" in method["name"].lower() else "MEDIUM"
                uncovered.append({
                    "class_name": method.get("class_name", "Unknown"),
                    "method_name": method["name"],
                    "file_path": method["file_path"],
                    "start_line": method["start_line"],
                    "end_line": method["end_line"],
                    "risk_level": risk
                })
            else:
                covered_count += 1

        total = len(ast_methods) if ast_methods else 1
        baseline_cov = round((covered_count / total) * 100.0, 1)

        # Generate targeted test templates for top uncovered methods
        suggested_tests = []
        for target in uncovered[:3]:
            suggested_tests.append({
                "test_name": f"test_{target['method_name']}_boundary_conditions",
                "target_method": target["method_name"],
                "target_file": target["file_path"],
                "test_code": f"""@Test\nvoid test_{target['method_name']}_nullInput() {{\n    assertThrows(IllegalArgumentException.class, () -> {{\n        service.{target['method_name']}(null);\n    }});\n}}"""
            })

        return {
            "baseline_coverage": baseline_cov,
            "uncovered_targets": uncovered,
            "suggested_tests": suggested_tests
        }
