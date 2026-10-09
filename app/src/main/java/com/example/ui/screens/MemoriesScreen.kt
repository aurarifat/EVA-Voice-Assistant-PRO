package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.TaskExecutionEngine
import com.example.data.model.MemoryItem
import com.example.data.model.TrainedTask
import com.example.data.repository.MemoryAndTaskRepository
import com.example.ui.components.appleBounceClick
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleIndigoDark
import com.example.ui.theme.AppleOrangeDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTheme

/**
 * 🍎 Maya AI Memories & Tasks — Apple Notes-Inspired Interface.
 * Clean, structured rounded cards with Apple segmented navigation, search, and backups.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoriesScreen(
    repository: MemoryAndTaskRepository,
    taskEngine: TaskExecutionEngine,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val memories by repository.memories.collectAsState()
    val trainedTasks by repository.trainedTasks.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showAddMemoryDialog by remember { mutableStateOf(false) }
    var showBackupDialog by remember { mutableStateOf(false) }
    var jsonBackupText by remember { mutableStateOf("") }

    val filteredMemories = memories.filter {
        searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(searchQuery, ignoreCase = true)
    }

    val filteredTasks = trainedTasks.filter {
        searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Memories & Tasks",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.4).sp
                        ),
                        color = AppleTheme.colors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AppleTheme.colors.surface)
                            .border(0.5.dp, AppleTheme.colors.cardBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppleTheme.colors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            jsonBackupText = repository.exportToJson()
                            showBackupDialog = true
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AppleTheme.colors.surface)
                            .border(0.5.dp, AppleTheme.colors.cardBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Backup,
                            contentDescription = "Backup & Restore",
                            tint = AppleTheme.colors.accent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppleTheme.colors.background
                )
            )
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddMemoryDialog = true },
                    containerColor = AppleTheme.colors.accent,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Memory")
                }
            }
        },
        containerColor = AppleTheme.colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Apple Segmented Control Tab Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppleTheme.colors.surfaceSecondary)
                    .border(0.5.dp, AppleTheme.colors.border, RoundedCornerShape(20.dp))
                    .padding(3.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AppleTabSegment(
                        label = "Memories (${memories.size})",
                        selected = selectedTab == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 0 }
                    )
                    AppleTabSegment(
                        label = "Trained Tasks (${trainedTasks.size})",
                        selected = selectedTab == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 1 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Apple Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search memories and tasks...", color = AppleTheme.colors.textMuted, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AppleTheme.colors.textMuted) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = AppleTheme.colors.surface,
                    unfocusedContainerColor = AppleTheme.colors.surface,
                    focusedTextColor = AppleTheme.colors.textPrimary,
                    unfocusedTextColor = AppleTheme.colors.textPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (selectedTab == 0) {
                if (filteredMemories.isEmpty()) {
                    AppleEmptyState(
                        icon = Icons.Default.Psychology,
                        title = "No Memories Stored",
                        message = "Maya automatically captures preferences and daily briefings during conversation."
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = filteredMemories,
                            key = { it.id }
                        ) { item ->
                            AppleMemoryCard(
                                item = item,
                                onDelete = { repository.deleteMemory(item.id) }
                            )
                        }
                    }
                }
            } else {
                if (filteredTasks.isEmpty()) {
                    AppleEmptyState(
                        icon = Icons.Default.Repeat,
                        title = "No Workflows Recorded",
                        message = "Teach Maya repetitive multi-step actions by voice to replay them anytime."
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = filteredTasks,
                            key = { it.id }
                        ) { task ->
                            AppleTrainedTaskCard(
                                task = task,
                                onPlay = {
                                    taskEngine.executeCustomPrompt(task.name)
                                },
                                onDelete = { repository.deleteTrainedTask(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Memory Dialog (Apple Rounded 24dp)
    if (showAddMemoryDialog) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Preference") }

        AlertDialog(
            onDismissRequest = { showAddMemoryDialog = false },
            title = { Text("New Memory", color = AppleTheme.colors.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Details / Preference") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            repository.addMemory(title, content, category)
                            showAddMemoryDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppleTheme.colors.accent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMemoryDialog = false }) {
                    Text("Cancel", color = AppleTheme.colors.textMuted)
                }
            },
            containerColor = AppleTheme.colors.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Backup & Restore Dialog
    if (showBackupDialog) {
        AlertDialog(
            onDismissRequest = { showBackupDialog = false },
            title = { Text("Backup & Restore", color = AppleTheme.colors.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Export your memories and trained workflows to JSON, or paste a backup to restore.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppleTheme.colors.textMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = jsonBackupText,
                        onValueChange = { jsonBackupText = it },
                        label = { Text("JSON Payload") },
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = repository.restoreFromJson(jsonBackupText)
                        if (success) {
                            Toast.makeText(context, "Restored successfully!", Toast.LENGTH_SHORT).show()
                            showBackupDialog = false
                        } else {
                            Toast.makeText(context, "Invalid JSON format", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppleGreenDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Restore", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackupDialog = false }) {
                    Text("Close", color = AppleTheme.colors.textMuted)
                }
            },
            containerColor = AppleTheme.colors.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun AppleTabSegment(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppleTheme.colors.surface else Color.Transparent,
        label = "tab_seg_bg"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) AppleTheme.colors.textPrimary else AppleTheme.colors.textMuted
        )
    }
}

@Composable
private fun AppleMemoryCard(item: MemoryItem, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AppleTheme.colors.surface)
            .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppleTheme.colors.accent.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.labelSmall.copy(color = AppleTheme.colors.accent)
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = AppleRedDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = AppleTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.content,
                style = MaterialTheme.typography.bodySmall,
                color = AppleTheme.colors.textMuted,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun AppleTrainedTaskCard(
    task: TrainedTask,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AppleTheme.colors.surface)
            .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(ApplePurpleDark.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            tint = ApplePurpleDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = task.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = AppleTheme.colors.textPrimary
                        )
                        Text(
                            text = "Ran ${task.runCount} times • ${task.source}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = AppleTheme.colors.textMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AppleTheme.colors.surfaceSecondary)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${task.steps.size} STEPS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AppleTheme.colors.accent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodySmall,
                color = AppleTheme.colors.textMuted,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPlay,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppleTheme.colors.accent),
                    modifier = Modifier
                        .height(36.dp)
                        .appleBounceClick(pressedScale = 0.94f),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Play Workflow", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }

                Row {
                    IconButton(onClick = {}, modifier = Modifier.size(34.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = AppleTheme.colors.textMuted,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(34.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = AppleRedDark,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppleEmptyState(icon: ImageVector, title: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(AppleTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppleTheme.colors.textMuted,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AppleTheme.colors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = AppleTheme.colors.textMuted,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 30.dp)
        )
    }
}
