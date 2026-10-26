package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RepositoryItem
import com.example.ui.components.MetricCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.UiState

@Composable
fun RepositoryOverviewScreen(
    state: UiState,
    onNavigateTab: (String) -> Unit,
    onTriggerScan: () -> Unit
) {
    val repo = state.selectedRepository ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("overview_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Repo Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(CyberCyan, ElectricBlue)),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Source,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = repo.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CodeGreen.copy(alpha = 0.15f))
                                .border(1.dp, CodeGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = repo.status,
                                color = CodeGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = repo.gitUrl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatLabel(label = "BRANCH", value = repo.defaultBranch)
                        StatLabel(label = "COMMIT", value = repo.currentCommitSha)
                        StatLabel(label = "FILES", value = repo.fileCount.toString())
                        StatLabel(label = "LOC", value = repo.linesOfCode.toString())
                    }
                }
            }
        }

        // Scan Progress / Trigger Bar
        item {
            if (state.isScanning) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkNavySurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = state.scanStage,
                                color = CyberCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${state.scanProgress}%",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { state.scanProgress / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = CyberCyan,
                            trackColor = Color(0xFF1E293B)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onTriggerScan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("run_analysis_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Trigger Full Codebase Scan",
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Metrics Grid (Coverage, Risk, Findings, Gaps)
        item {
            Text(
                text = "CORE REPOSITORY HEALTH",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Test Coverage",
                    value = "${state.testReport?.baselineLineCoverage ?: repo.testCoverage}%",
                    subtitle = "Baseline suite",
                    icon = Icons.Default.Checklist,
                    accentColor = CodeGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Regression Risk",
                    value = "${state.impactReport?.regressionRiskScore ?: repo.regressionRisk}",
                    subtitle = "${state.impactReport?.riskCategory ?: "Moderate"} impact",
                    icon = Icons.Default.Warning,
                    accentColor = WarningAmber,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Code Quality",
                    value = "${state.findings.size}",
                    subtitle = "${repo.criticalIssues} Critical finding",
                    icon = Icons.Default.Shield,
                    accentColor = if (repo.criticalIssues > 0) CriticalRed else CyberCyan,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Doc Gaps",
                    value = "${state.docGaps.size}",
                    subtitle = "Missing symbol docs",
                    icon = Icons.Default.Description,
                    accentColor = ElectricBlue,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Differentiating Pipelines Navigation List
        item {
            Text(
                text = "ENGINEERING ASSISTANT MODULES",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureNavCard(
                    title = "Codebase Q&A with Citations",
                    subtitle = "Hybrid RAG retrieving verified AST methods and line spans",
                    icon = Icons.Default.Chat,
                    accent = CyberCyan,
                    onClick = { onNavigateTab("CHAT") }
                )
                FeatureNavCard(
                    title = "Coverage-Guided Test Gap Analyzer",
                    subtitle = "Synthesize unit tests for uncovered branches with sandbox execution",
                    icon = Icons.Default.TaskAlt,
                    accent = CodeGreen,
                    onClick = { onNavigateTab("TESTS") }
                )
                FeatureNavCard(
                    title = "Change Impact & Regression Risk",
                    subtitle = "Traverse commit diffs across caller/callee knowledge graph",
                    icon = Icons.Default.CallSplit,
                    accent = WarningAmber,
                    onClick = { onNavigateTab("IMPACT") }
                )
                FeatureNavCard(
                    title = "Architecture & Call Graph Explorer",
                    subtitle = "Interactive Canvas visualization of classes, methods, and relationships",
                    icon = Icons.Default.AccountTree,
                    accent = ElectricBlue,
                    onClick = { onNavigateTab("GRAPH") }
                )
                FeatureNavCard(
                    title = "Security & Code Quality Findings",
                    subtitle = "Deterministic static analysis with AI triage and remediation",
                    icon = Icons.Default.BugReport,
                    accent = CriticalRed,
                    onClick = { onNavigateTab("FINDINGS") }
                )
                FeatureNavCard(
                    title = "Refactoring Patch Proposals",
                    subtitle = "Review unified diffs with sandboxed compilation and approval flow",
                    icon = Icons.Default.Build,
                    accent = CyberCyan,
                    onClick = { onNavigateTab("PATCHES") }
                )
            }
        }
    }
}

@Composable
private fun StatLabel(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun FeatureNavCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(1.dp, accent.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
