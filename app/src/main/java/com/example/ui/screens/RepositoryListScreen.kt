package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RepositoryItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.UiState

@Composable
fun RepositoryListScreen(
    state: UiState,
    onSelectRepository: (RepositoryItem) -> Unit,
    onConnectRepository: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var repoName by remember { mutableStateOf("") }
    var repoUrl by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("main") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("repo_list_screen")
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Connected Repositories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${state.repositories.size} repositories available",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = DeepNavy)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Connect", color = DeepNavy, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.repositories) { repo ->
                val isSelected = state.selectedRepository?.id == repo.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectRepository(repo)
                            onBack()
                        }
                        .border(
                            1.dp,
                            if (isSelected) CyberCyan else Color(0xFF1E293B),
                            RoundedCornerShape(14.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = repo.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (isSelected) {
                                Text("ACTIVE", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = repo.gitUrl, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Lang: ${repo.primaryLanguage}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Coverage: ${repo.testCoverage}%", fontSize = 12.sp, color = CodeGreen, fontWeight = FontWeight.Bold)
                            Text("Risk: ${repo.regressionRisk}", fontSize = 12.sp, color = WarningAmber, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Connect GitHub Repository") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = repoName,
                        onValueChange = { repoName = it },
                        label = { Text("Repository Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = repoUrl,
                        onValueChange = { repoUrl = it },
                        label = { Text("Git Clone URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text("Default Branch") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (repoName.isNotBlank() && repoUrl.isNotBlank()) {
                            onConnectRepository(repoName, repoUrl, branch)
                            showDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                ) {
                    Text("Connect & Ingest", color = DeepNavy, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = DarkNavyCard
        )
    }
}
