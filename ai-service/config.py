import os

class Settings:
    APP_NAME: str = "CodeSentinel AI Intelligence Engine"
    DEBUG: bool = os.getenv("DEBUG", "false").lower() == "true"
    GEMINI_API_KEY: str = os.getenv("GEMINI_API_KEY", "")
    DEFAULT_MODEL: str = os.getenv("GEMINI_MODEL", "gemini-2.5-flash")
    EMBEDDING_DIMENSION: int = 768
    ENABLE_GRAPH_RAG: bool = True

settings = Settings()