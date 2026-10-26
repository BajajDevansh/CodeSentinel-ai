from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from config import settings
from api.routes import router

app = FastAPI(
    title=settings.APP_NAME,
    version="1.0.0",
    description="Python FastAPI service providing AST parsing, Hybrid RAG, Knowledge Graph traversal, and Risk Analyzers"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(router)

@app.get("/health", tags=["System"])
async def health():
    return {
        "status": "UP",
        "service": settings.APP_NAME,
        "graph_nodes": 3,
        "version": "1.0.0"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
