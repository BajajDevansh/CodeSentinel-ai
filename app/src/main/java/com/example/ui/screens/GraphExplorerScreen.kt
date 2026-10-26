package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.UiState
import kotlin.math.sqrt

data class GraphVisualNode(
    val id: String,
    val name: String,
    val type: String, // CONTROLLER, SERVICE, SECURITY, REPOSITORY, TEST
    val xRatio: Float,
    val yRatio: Float,
    val color: Color,
    val incoming: List<String>,
    val outgoing: List<String>
)

@Composable
fun GraphExplorerScreen(state: UiState) {
    val nodes = remember {
        listOf(
            GraphVisualNode("auth_ctrl", "AuthController", "CONTROLLER", 0.25f, 0.20f, CyberCyan, emptyList(), listOf("auth_srv", "jwt_prov")),
            GraphVisualNode("jwt_filter", "JwtAuthFilter", "SECURITY", 0.75f, 0.20f, CriticalRed, emptyList(), listOf("jwt_prov")),
            GraphVisualNode("auth_srv", "AuthService", "SERVICE", 0.30f, 0.48f, ElectricBlue, listOf("auth_ctrl"), listOf("user_repo", "jwt_prov")),
            GraphVisualNode("jwt_prov", "JwtTokenProvider", "SECURITY", 0.65f, 0.50f, WarningAmber, listOf("auth_ctrl", "jwt_filter", "auth_srv"), emptyList()),
            GraphVisualNode("user_repo", "UserRepository", "REPOSITORY", 0.30f, 0.78f, CodeGreen, listOf("auth_srv"), emptyList()),
            GraphVisualNode("test_suite", "AuthServiceTest", "TEST", 0.75f, 0.78f, Color(0xFFA855F7), emptyList(), listOf("auth_srv"))
        )
    }

    var selectedNode by remember { mutableStateOf<GraphVisualNode?>(nodes.find { it.id == "jwt_prov" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("graph_screen")
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Architecture & Call Graph",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Interactive AST dependency topology",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "6 Nodes | 7 Edges",
                    fontSize = 11.sp,
                    color = CyberCyan,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Canvas Graph
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF070B14))
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = constraints.maxWidth.toFloat()
                val canvasHeight = constraints.maxHeight.toFloat()

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { tapOffset ->
                                val hit = nodes.find { node ->
                                    val nx = node.xRatio * canvasWidth
                                    val ny = node.yRatio * canvasHeight
                                    val dist = sqrt((tapOffset.x - nx) * (tapOffset.x - nx) + (tapOffset.y - ny) * (tapOffset.y - ny))
                                    dist <= 40f
                                }
                                if (hit != null) {
                                    selectedNode = hit
                                }
                            }
                        }
                ) {
                    // Draw Edges
                    nodes.forEach { source ->
                        val sx = source.xRatio * canvasWidth
                        val sy = source.yRatio * canvasHeight

                        source.outgoing.forEach { targetId ->
                            val target = nodes.find { it.id == targetId }
                            if (target != null) {
                                val tx = target.xRatio * canvasWidth
                                val ty = target.yRatio * canvasHeight
                                val isHighlighted = selectedNode?.id == source.id || selectedNode?.id == target.id

                                drawLine(
                                    color = if (isHighlighted) CyberCyan else Color(0xFF1E293B),
                                    start = Offset(sx, sy),
                                    end = Offset(tx, ty),
                                    strokeWidth = if (isHighlighted) 3.dp.toPx() else 1.5.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    // Draw Nodes
                    nodes.forEach { node ->
                        val nx = node.xRatio * canvasWidth
                        val ny = node.yRatio * canvasHeight
                        val isSelected = selectedNode?.id == node.id

                        // Outer ring glow
                        if (isSelected) {
                            drawCircle(
                                color = node.color.copy(alpha = 0.35f),
                                radius = 26.dp.toPx(),
                                center = Offset(nx, ny)
                            )
                        }

                        // Core Node
                        drawCircle(
                            color = node.color,
                            radius = 16.dp.toPx(),
                            center = Offset(nx, ny)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Selected Node Inspection Drawer
        selectedNode?.let { node ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, node.color.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(node.color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = node.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = node.type,
                            color = node.color,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Callers (Incoming): ${if (node.incoming.isEmpty()) "None (Entrypoint)" else node.incoming.joinToString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Dependencies (Outgoing): ${if (node.outgoing.isEmpty()) "Leaf component" else node.outgoing.joinToString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
