import networkx as nx
from typing import List, Dict, Any, Set

class RepositoryKnowledgeGraph:
    """Builds and queries graph representation of codebase modules, classes, methods, and tests."""

    def __init__(self):
        self.graph = nx.DiGraph()

    def add_symbol(self, symbol_id: str, symbol_type: str, file_path: str, metadata: Dict[str, Any] = None):
        self.graph.add_node(
            symbol_id,
            type=symbol_type,
            file_path=file_path,
            metadata=metadata or {}
        )

    def add_relationship(self, source_id: str, target_id: str, relation_type: str):
        # relation_type: CALLS, IMPORTS, DEFINES, TESTS
        self.graph.add_edge(source_id, target_id, relation=relation_type)

    def find_callers(self, symbol_id: str) -> List[str]:
        if symbol_id not in self.graph:
            return []
        return [pred for pred, _, data in self.graph.in_edges(symbol_id, data=True) if data.get('relation') == 'CALLS']

    def find_callees(self, symbol_id: str) -> List[str]:
        if symbol_id not in self.graph:
            return []
        return [succ for _, succ, data in self.graph.out_edges(symbol_id, data=True) if data.get('relation') == 'CALLS']

    def get_impacted_neighbors(self, symbol_ids: List[str], max_depth: int = 2) -> Set[str]:
        """Breadth-first search to traverse downstream callers and dependencies."""
        impacted = set()
        queue = [(sym, 0) for sym in symbol_ids if sym in self.graph]

        while queue:
            current, depth = queue.pop(0)
            if current in impacted or depth > max_depth:
                continue
            impacted.add(current)

            for pred in self.graph.predecessors(current):
                if pred not in impacted:
                    queue.append((pred, depth + 1))

        return impacted

    def get_related_tests(self, symbol_id: str) -> List[str]:
        tests = []
        for node in self.graph.nodes():
            if ("test" in node.lower() or "Test" in node) and nx.has_path(self.graph, node, symbol_id):
                tests.append(node)
        return tests
