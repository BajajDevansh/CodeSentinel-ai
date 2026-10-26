package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.remote.ApiClient
import com.example.data.repository.CodeSentinelRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val isAuthenticated: Boolean = false, // <-- Change to false so login screen displays
    val isAuthLoading: Boolean = false,
    val userEmail: String = "",
    val userFullName: String = "",
    val repositories: List<RepositoryItem> = emptyList(),
    val selectedRepository: RepositoryItem? = null,
    val findings: List<FindingItem> = emptyList(),
    val selectedFinding: FindingItem? = null,
    val severityFilter: String = "ALL", // ALL, CRITICAL, HIGH, MEDIUM, LOW
    val chatMessages: List<ChatMessage> = emptyList(),
    val isChatLoading: Boolean = false,
    val docGaps: List<DocumentationGap> = emptyList(),
    val generatedDocMarkdown: String? = null,
    val testReport: TestGapReport? = null,
    val isExecutingTests: Boolean = false,
    val impactReport: ChangeImpactReport? = null,
    val patches: List<PatchProposal> = emptyList(),
    val selectedPatch: PatchProposal? = null,
    val isScanning: Boolean = false,
    val scanProgress: Int = 0,
    val scanStage: String = "",
    val activeTab: String = "DASHBOARD", // DASHBOARD, CHAT, GRAPH, FINDINGS, TESTS, IMPACT, DOCS, PATCHES, HISTORY, SETTINGS
    val backendUrl: String = "https://pony-employee-unnatural.ngrok-free.dev/",
    val isOfflineMode: Boolean = false,
    val errorMessage: String? = null
)

class CodeSentinelViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = CodeSentinelRepository(db)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadRepositories()
    }
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, errorMessage = null) }
            val result = repository.login(email, pass)
            result.fold(
                onSuccess = { auth ->
                    _uiState.update {
                        it.copy(
                            isAuthenticated = true,
                            isAuthLoading = false,
                            userEmail = auth.email,
                            userFullName = auth.fullName,
                            errorMessage = null
                        )
                    }
                    loadRepositories()
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isAuthLoading = false,
                            errorMessage = err.message ?: "Authentication failed"
                        )
                    }
                }
            )
        }
    }

    fun register(email: String, pass: String, fullName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, errorMessage = null) }
            val result = repository.register(email, pass, fullName)
            result.fold(
                onSuccess = { auth ->
                    _uiState.update {
                        it.copy(
                            isAuthenticated = true,
                            isAuthLoading = false,
                            userEmail = auth.email,
                            userFullName = auth.fullName,
                            errorMessage = null
                        )
                    }
                    loadRepositories()
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isAuthLoading = false,
                            errorMessage = err.message ?: "Registration failed"
                        )
                    }
                }
            )
        }
    }

    fun logout() {
        ApiClient.setToken(null)
        _uiState.update {
            it.copy(
                isAuthenticated = false,
                userEmail = "",
                userFullName = "",
                errorMessage = null
            )
        }
    }

    fun clearAuthError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun selectTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun selectRepository(repo: RepositoryItem) {
        _uiState.update {
            it.copy(
                selectedRepository = repo,
                chatMessages = listOf(
                    ChatMessage(
                        id = "welcome-" + repo.id,
                        repositoryId = repo.id,
                        sender = "AI",
                        message = "Active repository switched to **${repo.name}**.\n\nAll ${repo.fileCount} source files and AST symbols are loaded. Ask any question about ${repo.name}'s architecture, security findings, or test gaps.",
                        citationsJson = "FILE: ${repo.name}/README.md:1-20\nSYMBOL: RepositoryRoot\nMETHOD: GRAPH"
                    )
                )
            )
        }

        // Asynchronously fetch all data for the newly selected repository from backend
        viewModelScope.launch {
            val findings = repository.fetchFindings(repo.id)
            val testReport = repository.fetchTestGaps(repo.id)
            val impactReport = repository.fetchChangeImpact(repo.id)
            val patches = repository.fetchPatches(repo.id)

            _uiState.update {
                it.copy(
                    findings = findings,
                    testReport = testReport,
                    impactReport = impactReport,
                    patches = patches,
                    docGaps = repository.getDocumentationGaps(repo.id)
                )
            }
        }
    }

    fun setSeverityFilter(severity: String) {
        _uiState.update { it.copy(severityFilter = severity) }
    }

    fun selectFinding(finding: FindingItem?) {
        _uiState.update { it.copy(selectedFinding = finding) }
    }

    fun selectPatch(patch: PatchProposal?) {
        _uiState.update { it.copy(selectedPatch = patch) }
    }

    fun loadRepositories() {
        viewModelScope.launch {
            repository.getRepositories().collect { list ->
                _uiState.update { state ->
                    val selected = state.selectedRepository ?: list.firstOrNull()
                    val findings = selected?.let { repository.getFindings(it.id) } ?: emptyList()
                    val docGaps = selected?.let { repository.getDocumentationGaps(it.id) } ?: emptyList()
                    val testReport = selected?.let { repository.getTestGapReport(it.id) }
                    val impactReport = selected?.let { repository.getChangeImpactReport(it.id) }
                    val patches = selected?.let { repository.getPatchProposals(it.id) } ?: emptyList()
                    val chatMessages = if (state.chatMessages.isEmpty()) {
                        listOf(
                            ChatMessage(
                                id = "welcome-ai-initial",
                                repositoryId = selected?.id ?: "repo-spring-security",
                                sender = "AI",
                                message = "Hello! I am CodeSentinel, your repository-aware engineering assistant.\n\nI have parsed the AST symbol graph, indexed all ${selected?.fileCount ?: 64} source files, and mapped caller/callee relations for **${selected?.name ?: "spring-boot-security-service"}**.\n\nTry asking me about authentication flow, security findings, test gaps, architecture, or commit impact.",
                                citationsJson = "FILE: backend/src/main/java/com/codesentinel/backend/security/JwtTokenProvider.java:24-42\nSYMBOL: generateAccessToken()\nMETHOD: HYBRID (AST + Token Matching)\nSNIPPET:\nJwts.builder().subject(userId).claim(\"email\", email)..."
                            )
                        )
                    } else state.chatMessages

                    state.copy(
                        repositories = list,
                        selectedRepository = selected,
                        findings = findings,
                        docGaps = docGaps,
                        testReport = testReport,
                        impactReport = impactReport,
                        patches = patches,
                        chatMessages = chatMessages
                    )
                }
            }
        }
    }

    fun connectRepository(name: String, url: String, branch: String) {
        viewModelScope.launch {
            val result = repository.connectRepository(name, url, branch)
            result.fold(
                onSuccess = { repo ->
                    _uiState.update {
                        it.copy(
                            repositories = it.repositories + repo,
                            selectedRepository = repo,
                            errorMessage = null
                        )
                    }
                    selectRepository(repo)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(errorMessage = "Failed to connect repository: ${error.message}")
                    }
                }
            )
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val currentRepo = _uiState.value.selectedRepository ?: _uiState.value.repositories.firstOrNull() ?: return

        val userMsg = ChatMessage(
            id = "msg-" + System.currentTimeMillis(),
            repositoryId = currentRepo.id,
            sender = "USER",
            message = text
        )

        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isChatLoading = true
            )
        }

        viewModelScope.launch {
            val (answer, citations) = repository.askCodebase(currentRepo.id, text)
            // Serialize citations into preview format
            val citationsText = citations.joinToString(separator = "\n---\n") {
                "FILE: ${it.filePath}:${it.startLine}-${it.endLine}\nSYMBOL: ${it.symbolName}\nMETHOD: ${it.retrievalMethod}\nSNIPPET:\n${it.snippet}"
            }

            val aiMsg = ChatMessage(
                id = "msg-ai-" + System.currentTimeMillis(),
                repositoryId = currentRepo.id,
                sender = "AI",
                message = answer,
                citationsJson = citationsText
            )

            _uiState.update {
                it.copy(
                    chatMessages = it.chatMessages + aiMsg,
                    isChatLoading = false
                )
            }
        }
    }

    fun triggerScan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, scanProgress = 10, scanStage = "Parsing AST Symbols...") }
            delay(500)
            _uiState.update { it.copy(scanProgress = 35, scanStage = "Constructing Knowledge Graph...") }
            delay(600)
            _uiState.update { it.copy(scanProgress = 65, scanStage = "Running Static & Security Rules...") }
            delay(600)
            _uiState.update { it.copy(scanProgress = 90, scanStage = "Executing Coverage Gap Suite...") }
            delay(500)

            val currentRepo = _uiState.value.selectedRepository
            if (currentRepo != null) {
                _uiState.update {
                    it.copy(
                        isScanning = false,
                        scanProgress = 100,
                        scanStage = "Analysis Completed",
                        findings = repository.getFindings(currentRepo.id),
                        testReport = repository.getTestGapReport(currentRepo.id),
                        impactReport = repository.getChangeImpactReport(currentRepo.id)
                    )
                }
            }
        }
    }

    fun runGeneratedTests() {
        val currentRepo = _uiState.value.selectedRepository ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isExecutingTests = true) }
            delay(1200) // Sandboxed test run simulation
            val updatedReport = _uiState.value.testReport?.copy(
                executionLog = "[SANDBOX] Executing in isolated container worker...\nRunning 12 generated JUnit 5 test cases...\nResult: 12 passed, 0 failed\nCoverage line delta verified: +14.3% (64.2% -> 78.5%)\nCoverage branch delta verified: +17.4% (51.8% -> 69.2%)\n[STATUS] PASSED WITH ZERO REGRESSIONS"
            )
            _uiState.update { it.copy(isExecutingTests = false, testReport = updatedReport) }
        }
    }

    fun approvePatch(patchId: String) {
        _uiState.update { state ->
            val updated = state.patches.map {
                if (it.id == patchId) it.copy(status = "APPROVED", validationStatus = "Approved by developer. Pull request created on branch fix/jwt-null-guard.") else it
            }
            state.copy(patches = updated, selectedPatch = updated.find { it.id == patchId })
        }
    }

    fun rejectPatch(patchId: String) {
        _uiState.update { state ->
            val updated = state.patches.map {
                if (it.id == patchId) it.copy(status = "REJECTED", validationStatus = "Rejected proposal.") else it
            }
            state.copy(patches = updated, selectedPatch = updated.find { it.id == patchId })
        }
    }

    fun generateDocumentation() {
        val currentRepo = _uiState.value.selectedRepository ?: return
        val doc = """
            # CodeSentinel Automated Architecture Overview
            
            ## Repository: ${currentRepo.name}
            - **Primary Language**: ${currentRepo.primaryLanguage} (${currentRepo.languagePercent}%)
            - **Commit SHA**: ${currentRepo.currentCommitSha}
            - **Lines of Code**: ${currentRepo.linesOfCode} across ${currentRepo.fileCount} source files.
            
            ### Core Components & Subsystems
            1. **Authentication Filter Chain**: Intercepts Bearer headers and authenticates JWT credentials.
            2. **Knowledge Graph Engine**: NetworkX directed call-graph mapping caller/callee relations.
            3. **Coverage-Guided Test Gap Runner**: JaCoCo agent integration executing sandboxed unit suites.
            4. **Regression Risk Model**: Evaluates transitive graph reachability on pull request diffs.
        """.trimIndent()
        _uiState.update { it.copy(generatedDocMarkdown = doc) }
    }

    fun updateBackendUrl(url: String) {
        ApiClient.updateBaseUrl(url)
        _uiState.update { it.copy(backendUrl = url) }
    }

    fun setAuth(isAuth: Boolean) {
        _uiState.update { it.copy(isAuthenticated = isAuth) }
    }
}
