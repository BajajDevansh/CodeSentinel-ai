package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.UiState

@Composable
fun SettingsScreen(
    state: UiState,
    onUpdateBackendUrl: (String) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var urlInput by remember { mutableStateOf(state.backendUrl) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen")
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "System Settings & Connectivity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Configure Spring Boot backend connection and diagnostic telemetry",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Backend URL Config Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "BACKEND SERVER URL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Spring Boot API Endpoint") },
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        onUpdateBackendUrl(urlInput.trim())
                        Toast.makeText(context, "API Base URL updated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save & Reconnect", color = DeepNavy, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Subsystem Diagnostics Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SUBSYSTEM HEALTH & INTEGRATION STATUS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGreen,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                ServiceStatusRow("Spring Boot 3.3.4 Modular Monolith", "CONNECTED (Virtual Threads)", CodeGreen)
                ServiceStatusRow("Python 3.12 FastAPI AI Service", "ACTIVE (AST + NetworkX)", CodeGreen)
                ServiceStatusRow("MongoDB Document Database", "HEALTHY (7 Collections)", CodeGreen)
                ServiceStatusRow("JaCoCo Isolated Test Worker", "STANDBY (Docker Sandbox)", CodeGreen)
            }
        }

        // User Profile
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ACTIVE DEVELOPER SESSION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = state.userFullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = state.userEmail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Roles: [ROLE_DEVELOPER, ROLE_ARCHITECT]", fontSize = 11.sp, color = CyberCyan)
            }
        }
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CriticalRed.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = CriticalRed)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out of Session", color = CriticalRed, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ServiceStatusRow(service: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = service, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
        Text(text = status, fontSize = 11.sp, color = statusColor, fontWeight = FontWeight.Bold)
    }
}
