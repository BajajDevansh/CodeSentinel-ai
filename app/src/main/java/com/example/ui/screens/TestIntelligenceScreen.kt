package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GeneratedTestCase
import com.example.data.model.UncoveredTarget
import com.example.ui.components.CodeSnippetBlock
import com.example.ui.components.MetricCard
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.UiState

@Composable
fun TestIntelligenceScreen(
    state: UiState,
    onRunTests: () -> Unit
) {
    val report = state.testReport ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("test_gap_screen")
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Coverage-Guided Test Gap Analyzer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Identifies uncovered branches and validates coverage in sandbox",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Coverage Comparison Card (Baseline vs Post-Gen)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CodeGreen.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "MEASURED COVERAGE DELTA (JACOCO VERIFIED)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGreen,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Line Coverage", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = "${report.baselineLineCoverage}% -> ${report.postGenerationLineCoverage}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "+${"%.1f".format(report.postGenerationLineCoverage - report.baselineLineCoverage)}% Increase",
                                color = CodeGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Branch Coverage", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = "${report.baselineBranchCoverage}% -> ${report.postGenerationBranchCoverage}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "+${"%.1f".format(report.postGenerationBranchCoverage - report.baselineBranchCoverage)}% Increase",
                                color = CodeGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { (report.postGenerationLineCoverage / 100f).toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = CodeGreen,
                        trackColor = Color(0xFF1E293B)
                    )
                }
            }
        }

        // Action: Run tests in Sandbox
        item {
            Button(
                onClick = onRunTests,
                enabled = !state.isExecutingTests,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("execute_tests_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CodeGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (state.isExecutingTests) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = DeepNavy, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Executing in Container Sandbox...", color = DeepNavy, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Execute in Sandbox & Verify Coverage", color = DeepNavy, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Execution Log Terminal Output
        item {
            Text(
                text = "SANDBOX EXECUTION LOG",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF070B14))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = report.executionLog,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFF4ADE80),
                    lineHeight = 16.sp
                )
            }
        }

        // Uncovered Methods Target List
        item {
            Text(
                text = "UNCOVERED METHODS PRIORITIZED BY RISK (${report.uncoveredTargets.size})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
        }

        items(report.uncoveredTargets) { target ->
            UncoveredTargetCard(target)
        }

        // Generated Test Cases Preview
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "SYNTHESIZED TEST CASES (${report.generatedTests.size})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
        }

        items(report.generatedTests) { testCase ->
            GeneratedTestCard(testCase)
        }
    }
}

@Composable
fun UncoveredTargetCard(target: UncoveredTarget) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = target.methodName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                SeverityBadge(severity = target.riskLevel)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${target.filePath}:${target.startLine}-${target.endLine}",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GeneratedTestCard(testCase: GeneratedTestCase) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = testCase.testName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan,
                    fontFamily = FontFamily.Monospace
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CodeGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("VERIFIED PASS", color = CodeGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            CodeSnippetBlock(
                filePath = testCase.targetFile,
                startLine = 1,
                code = testCase.testCode
            )
        }
    }
}
