package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FindingItem
import com.example.ui.components.CodeSnippetBlock
import com.example.ui.components.SeverityBadge
import com.example.ui.components.ToolConfirmedBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.UiState

@Composable
fun FindingsScreen(
    state: UiState,
    onFilterSeverity: (String) -> Unit,
    onSelectFinding: (FindingItem?) -> Unit,
    onProposePatch: (FindingItem) -> Unit
) {
    val filters = listOf("ALL", "CRITICAL", "HIGH", "MEDIUM", "LOW")
    val filteredFindings = remember(state.findings, state.severityFilter) {
        if (state.severityFilter == "ALL") state.findings
        else state.findings.filter { it.severity.equals(state.severityFilter, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("findings_screen")
            .padding(16.dp)
    ) {
        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Code Quality & Security",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${filteredFindings.size} findings reported across repository",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Severity Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                val isSelected = state.severityFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { onFilterSeverity(filter) },
                    label = { Text(filter, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberCyan,
                        selectedLabelColor = DeepNavy,
                        containerColor = DarkNavyCard,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) CyberCyan else Color(0xFF1E293B)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Findings List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredFindings) { finding ->
                FindingCard(
                    finding = finding,
                    onProposePatch = { onProposePatch(finding) }
                )
            }
        }
    }
}

@Composable
fun FindingCard(
    finding: FindingItem,
    onProposePatch: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = finding.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = finding.cweRule,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                SeverityBadge(severity = finding.severity)
            }

            Spacer(modifier = Modifier.height(8.dp))
            ToolConfirmedBadge(isConfirmed = finding.toolConfirmed, detector = finding.detector)

            Spacer(modifier = Modifier.height(10.dp))
            CodeSnippetBlock(
                filePath = finding.filePath,
                startLine = finding.startLine,
                code = finding.snippet
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = finding.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkNavySurface)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "POTENTIAL IMPACT:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CriticalRed,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = finding.impact,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "RECOMMENDED REMEDIATION:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGreen,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = finding.remediation,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { expanded = !expanded },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (expanded) "Hide Details" else "View Remediation Details",
                        fontSize = 12.sp,
                        color = CyberCyan
                    )
                }

                Button(
                    onClick = onProposePatch,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Propose Patch",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
