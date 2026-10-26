package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CodeSentinelViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CodeSentinelViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                var showRepoList by remember { mutableStateOf(false) }

                // Hardware back press handler
                if (!uiState.isAuthenticated) {
                    LoginScreen(
                        backendUrl = uiState.backendUrl,
                        isLoading = uiState.isAuthLoading,
                        errorMessage = uiState.errorMessage,
                        onUpdateBackendUrl = { url -> viewModel.updateBackendUrl(url) },
                        onLogin = { email, pass -> viewModel.login(email, pass) },
                        onRegister = { email, pass, name -> viewModel.register(email, pass, name) },
                        onClearError = { viewModel.clearAuthError() }
                    )
                }
                else {
                    // Hardware back press handler
                    BackHandler(enabled = showRepoList || uiState.activeTab != "DASHBOARD") {
                        if (showRepoList) {
                            showRepoList = false
                        } else if (uiState.activeTab != "DASHBOARD") {
                            viewModel.selectTab("DASHBOARD")
                        }
                    }


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { showRepoList = !showRepoList }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = CyberCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = uiState.selectedRepository?.name ?: "CodeSentinel",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${uiState.selectedRepository?.primaryLanguage ?: "Kotlin"} • ${uiState.selectedRepository?.defaultBranch ?: "main"}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Switch Repo",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { viewModel.triggerScan() },
                                    modifier = Modifier.testTag("appbar_scan_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Rescan",
                                        tint = CyberCyan
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                titleContentColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .testTag("bottom_nav_bar"),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            NavItem(
                                label = "Overview",
                                icon = Icons.Default.Dashboard,
                                isSelected = uiState.activeTab == "DASHBOARD" && !showRepoList,
                                onClick = {
                                    showRepoList = false
                                    viewModel.selectTab("DASHBOARD")
                                }
                            )
                            NavItem(
                                label = "Q&A",
                                icon = Icons.Default.Chat,
                                isSelected = uiState.activeTab == "CHAT" && !showRepoList,
                                onClick = {
                                    showRepoList = false
                                    viewModel.selectTab("CHAT")
                                }
                            )
                            NavItem(
                                label = "Test Gaps",
                                icon = Icons.Default.TaskAlt,
                                isSelected = uiState.activeTab == "TESTS" && !showRepoList,
                                onClick = {
                                    showRepoList = false
                                    viewModel.selectTab("TESTS")
                                }
                            )
                            NavItem(
                                label = "Impact",
                                icon = Icons.Default.CallSplit,
                                isSelected = uiState.activeTab == "IMPACT" && !showRepoList,
                                onClick = {
                                    showRepoList = false
                                    viewModel.selectTab("IMPACT")
                                }
                            )
                            NavItem(
                                label = "Findings",
                                icon = Icons.Default.BugReport,
                                isSelected = uiState.activeTab == "FINDINGS" && !showRepoList,
                                onClick = {
                                    showRepoList = false
                                    viewModel.selectTab("FINDINGS")
                                }
                            )
                            NavItem(
                                label = "Config",
                                icon = Icons.Default.Settings,
                                isSelected = uiState.activeTab == "SETTINGS" && !showRepoList,
                                onClick = {
                                    showRepoList = false
                                    viewModel.selectTab("SETTINGS")
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (showRepoList) {
                            RepositoryListScreen(
                                state = uiState,
                                onSelectRepository = { repo ->
                                    viewModel.selectRepository(repo)
                                    showRepoList = false
                                },
                                onConnectRepository = { name, url, branch ->
                                    viewModel.connectRepository(name, url, branch)
                                    showRepoList = false
                                },
                                onBack = { showRepoList = false }
                            )
                        } else {
                            when (uiState.activeTab) {
                                "DASHBOARD" -> RepositoryOverviewScreen(
                                    state = uiState,
                                    onNavigateTab = { tab -> viewModel.selectTab(tab) },
                                    onTriggerScan = { viewModel.triggerScan() }
                                )
                                "CHAT" -> ChatScreen(
                                    state = uiState,
                                    onSendMessage = { text -> viewModel.sendChatMessage(text) }
                                )
                                "GRAPH" -> GraphExplorerScreen(state = uiState)
                                "FINDINGS" -> FindingsScreen(
                                    state = uiState,
                                    onFilterSeverity = { sev -> viewModel.setSeverityFilter(sev) },
                                    onSelectFinding = { finding -> viewModel.selectFinding(finding) },
                                    onProposePatch = { finding ->
                                        viewModel.selectTab("PATCHES")
                                    }
                                )
                                "TESTS" -> TestIntelligenceScreen(
                                    state = uiState,
                                    onRunTests = { viewModel.runGeneratedTests() }
                                )
                                "IMPACT" -> ChangeImpactScreen(state = uiState)
                                "DOCS" -> DocumentationScreen(
                                    state = uiState,
                                    onGenerateDocs = { viewModel.generateDocumentation() }
                                )
                                "PATCHES" -> PatchReviewScreen(
                                    state = uiState,
                                    onApprovePatch = { id -> viewModel.approvePatch(id) },
                                    onRejectPatch = { id -> viewModel.rejectPatch(id) }
                                )
                                "SETTINGS" -> SettingsScreen(
                                    state = uiState,
                                    onUpdateBackendUrl = { url -> viewModel.updateBackendUrl(url) },
                                    onLogout = { viewModel.logout() }
                                )
                                else -> RepositoryOverviewScreen(
                                    state = uiState,
                                    onNavigateTab = { tab -> viewModel.selectTab(tab) },
                                    onTriggerScan = { viewModel.triggerScan() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) CyberCyan else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) CyberCyan else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = CyberCyan.copy(alpha = 0.15f)
        )
    )
}
}
