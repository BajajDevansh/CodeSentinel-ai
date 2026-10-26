package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.model.ChatMessage
import com.example.ui.components.CodeSnippetBlock
import com.example.ui.theme.*
import com.example.ui.viewmodel.UiState

@Composable
fun ChatScreen(
    state: UiState,
    onSendMessage: (String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var selectedCitationContent by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    val suggestedQuestions = listOf(
        "How does authentication work?",
        "What are the test coverage gaps?",
        "What are the critical security findings?",
        "Explain the change impact & regression risk",
        "Which REST endpoints are exposed?"
    )

    LaunchedEffect(state.chatMessages.size) {
        if (state.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(state.chatMessages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding() // Critical: Keeps input row and messages above software keyboard
            .testTag("chat_screen")
    ) {
        // Chat History List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 12.dp)
        ) {
            items(state.chatMessages) { msg ->
                ChatBubble(
                    message = msg,
                    onOpenCitation = { snippet ->
                        selectedCitationContent = snippet
                    }
                )
            }

            if (state.isChatLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkNavyCard)
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = CyberCyan,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Traversing AST Knowledge Graph & Hybrid RAG...",
                            fontSize = 12.sp,
                            color = CyberCyan,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Suggested Prompt Chips (Tap immediately sends or populates prompt)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(suggestedQuestions) { prompt ->
                SuggestionChip(
                    onClick = {
                        textInput = ""
                        onSendMessage(prompt)
                    },
                    label = { Text(text = prompt, fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = DarkNavyCard,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = Color(0xFF1E293B)
                    )
                )
            }
        }

        // Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input"),
                placeholder = {
                    Text(
                        "Ask question (e.g. How does auth work?)",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = Color(0xFF334155),
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                maxLines = 3
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        onSendMessage(textInput.trim())
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(CyberCyan)
                    .testTag("send_chat_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = DeepNavy,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    // Modal to view cited source evidence
    if (selectedCitationContent != null) {
        AlertDialog(
            onDismissRequest = { selectedCitationContent = null },
            confirmButton = {
                TextButton(onClick = { selectedCitationContent = null }) {
                    Text("Close", color = CyberCyan, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Source, contentDescription = null, tint = CyberCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cited Source Evidence", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = selectedCitationContent ?: "",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color(0xFFE2E8F0),
                            lineHeight = 18.sp
                        )
                    }
                }
            },
            containerColor = DarkNavyCard
        )
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    onOpenCitation: (String) -> Unit
) {
    val isUser = message.sender == "USER"
    val align = if (isUser) Alignment.End else Alignment.Start
    val bgColor = if (isUser) ElectricBlue else DarkNavyCard
    val textColor = Color.White

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(if (isUser) 0.85f else 0.95f)
                .clip(RoundedCornerShape(14.dp))
                .background(bgColor)
                .border(
                    1.dp,
                    if (isUser) Color.Transparent else Color(0xFF1E293B),
                    RoundedCornerShape(14.dp)
                )
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isUser) Icons.Default.Person else Icons.Default.Memory,
                        contentDescription = null,
                        tint = if (isUser) Color.White.copy(alpha = 0.85f) else CyberCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isUser) "You" else "CodeSentinel AI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUser) Color.White.copy(alpha = 0.9f) else CyberCyan
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message.message,
                    color = textColor,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )

                if (message.citationsJson.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onOpenCitation(message.citationsJson) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Inspect Verified Citations & Line Spans",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
