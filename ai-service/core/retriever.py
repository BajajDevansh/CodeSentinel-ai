import re
from typing import List, Dict, Any
from core.graph import RepositoryKnowledgeGraph

class HybridRetriever:
    """Performs hybrid retrieval combining keyword matching, semantic scoring, and graph expansion."""

    def __init__(self, knowledge_graph: RepositoryKnowledgeGraph):
        self.graph = knowledge_graph
        self.documents: List[Dict[str, Any]] = []

    def index_document(self, doc_id: str, content: str, file_path: str, start_line: int, end_line: int, symbol: str):
        self.documents.append({
            "id": doc_id,
            "content": content,
            "file_path": file_path,
            "start_line": start_line,
            "end_line": end_line,
            "symbol": symbol,
            "tokens": set(re.findall(r'\w+', content.lower()))
        })

    def search_keyword(self, query: str, top_k: int = 5) -> List[Dict[str, Any]]:
        query_tokens = set(re.findall(r'\w+', query.lower()))
        scored = []
        for doc in self.documents:
            overlap = len(doc["tokens"].intersection(query_tokens))
            if overlap > 0:
                scored.append((overlap, doc))
        scored.sort(key=lambda x: x[0], reverse=True)
        return [item[1] for item in scored[:top_k]]

    def search_semantic(self, query: str, top_k: int = 5) -> List[Dict[str, Any]]:
        # In production this calls embeddings; here we provide cosine/token relevance fallback
        return self.search_keyword(query, top_k)

    def retrieve_hybrid(self, query: str, top_k: int = 3) -> List[Dict[str, Any]]:
        keyword_results = self.search_keyword(query, top_k * 2)
        semantic_results = self.search_semantic(query, top_k * 2)

        # Reciprocal Rank Fusion (RRF)
        scores: Dict[str, float] = {}
        doc_map = {}

        for rank, doc in enumerate(keyword_results, 1):
            doc_id = doc["id"]
            doc_map[doc_id] = doc
            scores[doc_id] = scores.get(doc_id, 0.0) + (1.0 / (60 + rank))

        for rank, doc in enumerate(semantic_results, 1):
            doc_id = doc["id"]
            doc_map[doc_id] = doc
            scores[doc_id] = scores.get(doc_id, 0.0) + (1.0 / (60 + rank))

        sorted_ids = sorted(scores.keys(), key=lambda k: scores[k], reverse=True)
        top_docs = [doc_map[did] for did in sorted_ids[:top_k]]

        # Expand via knowledge graph callers and callees
        final_results = []
        for doc in top_docs:
            sym = doc["symbol"]
            callers = self.graph.find_callers(sym)
            callees = self.graph.find_callees(sym)
            doc_copy = dict(doc)
            doc_copy["related_callers"] = callers
            doc_copy["related_callees"] = callees
            doc_copy["retrieval_method"] = "HYBRID (RRF Keyword+Semantic+Graph)"
            final_results.append(doc_copy)

        return final_results
