import re
import ast
from typing import List, Dict, Any

class CodeSymbol:
    def __init__(self, name: str, symbol_type: str, file_path: str, start_line: int, end_line: int, docstring: str = "", code_snippet: str = ""):
        self.name = name
        self.symbol_type = symbol_type
        self.file_path = file_path
        self.start_line = start_line
        self.end_line = end_line
        self.docstring = docstring
        self.code_snippet = code_snippet

    def to_dict(self) -> Dict[str, Any]:
        return {
            "name": self.name,
            "type": self.symbol_type,
            "file_path": self.file_path,
            "start_line": self.start_line,
            "end_line": self.end_line,
            "docstring": self.docstring,
            "code_snippet": self.code_snippet
        }

class RepositoryParser:
    """Extracts symbols, methods, classes, and call relationships across Java, Kotlin, and Python."""

    @staticmethod
    def parse_python(file_path: str, source_code: str) -> List[CodeSymbol]:
        symbols = []
        try:
            tree = ast.parse(source_code)
            for node in ast.walk(tree):
                if isinstance(node, ast.ClassDef):
                    doc = ast.get_docstring(node) or ""
                    symbols.append(CodeSymbol(
                        name=node.name,
                        symbol_type="CLASS",
                        file_path=file_path,
                        start_line=node.lineno,
                        end_line=getattr(node, 'end_lineno', node.lineno),
                        docstring=doc
                    ))
                elif isinstance(node, ast.FunctionDef) or isinstance(node, ast.AsyncFunctionDef):
                    doc = ast.get_docstring(node) or ""
                    symbols.append(CodeSymbol(
                        name=node.name,
                        symbol_type="METHOD",
                        file_path=file_path,
                        start_line=node.lineno,
                        end_line=getattr(node, 'end_lineno', node.lineno),
                        docstring=doc
                    ))
        except SyntaxError:
            pass
        return symbols

    @staticmethod
    def parse_java_kotlin(file_path: str, source_code: str) -> List[CodeSymbol]:
        symbols = []
        lines = source_code.splitlines()

        # Class pattern: public class Foo / class Foo
        class_regex = re.compile(r'(?:public\s+|protected\s+|private\s+)?(?:data\s+|sealed\s+)?(class|interface|record)\s+([A-Za-z0-9_]+)')
        # Method pattern: fun foo(...) / public ReturnType methodName(...)
        method_regex = re.compile(r'(?:public\s+|protected\s+|private\s+|fun\s+)(?:static\s+)?(?:<.*?>\s+)?([A-Za-z0-9_<>,\[\]]+)\s+([A-Za-z0-9_]+)\s*\(')

        for i, line in enumerate(lines, 1):
            c_match = class_regex.search(line)
            if c_match:
                symbols.append(CodeSymbol(
                    name=c_match.group(2),
                    symbol_type="CLASS",
                    file_path=file_path,
                    start_line=i,
                    end_line=min(i + 30, len(lines)),
                    code_snippet=line.strip()
                ))
                continue

            m_match = method_regex.search(line)
            if m_match and not any(kw in line for kw in ["if", "while", "for", "switch", "catch"]):
                symbols.append(CodeSymbol(
                    name=m_match.group(2),
                    symbol_type="METHOD",
                    file_path=file_path,
                    start_line=i,
                    end_line=min(i + 15, len(lines)),
                    code_snippet=line.strip()
                ))

        return symbols
