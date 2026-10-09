package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CharacterStyle
import com.example.data.repository.MayaPreferences
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTheme

/**
 * 🍎 Maya AI Appearance & 3D Character — Apple-Inspired Visual Customization.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    preferences: MayaPreferences,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentCharacter by preferences.selectedCharacter.collectAsState()
    var orbSizeSlider by remember { mutableFloatStateOf(220f) }
    var cornerRadiusSlider by remember { mutableFloatStateOf(20f) }

    val characters = CharacterStyle.entries.toTypedArray()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Appearance & Avatar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppleTheme.colors.background
                )
            )
        },
        containerColor = AppleTheme.colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "NEURAL 3D AVATAR SHOWCASE",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppleTheme.colors.textMuted,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Apple Hero Panel (28dp radius)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(28.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.maya_character_main),
                    contentDescription = "Maya 3D Character",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Bottom Overlay Card
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xEE121214))
                        .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentCharacter.displayName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(currentCharacter.colorHex).copy(alpha = 0.25f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = currentCharacter.tier,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(currentCharacter.colorHex),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Apple Neural Engine Render Active",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFFAAAAAA)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Characters Selection Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(characters) { charStyle ->
                    val isSelected = currentCharacter == charStyle
                    val isLocked = charStyle.tier == "LOCKED"

                    Box(
                        modifier = Modifier
                            .width(134.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) AppleTheme.colors.surfaceSecondary else AppleTheme.colors.surface
                            )
                            .border(
                                if (isSelected) 1.5.dp else 0.5.dp,
                                if (isSelected) AppleTheme.colors.accent else AppleTheme.colors.cardBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                if (isLocked) {
                                    Toast.makeText(context, "Locked: Upgrade to access ${charStyle.displayName}", Toast.LENGTH_SHORT).show()
                                } else {
                                    preferences.setCharacterStyle(charStyle)
                                }
                            }
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(charStyle.colorHex))
                                )
                                if (isLocked) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = AppleRedDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = AppleTheme.colors.accent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = charStyle.displayName,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = AppleTheme.colors.textPrimary
                            )

                            Text(
                                text = charStyle.tier,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = if (isLocked) AppleRedDark else AppleTheme.colors.textMuted
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Orb & Theme Customization Controls
            Text(
                text = "SYSTEM CONTROLS & GEOMETRY",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppleTheme.colors.textMuted,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(22.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Orb Dynamic Scale",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = AppleTheme.colors.textPrimary
                        )
                        Text(
                            text = "${orbSizeSlider.toInt()} dp",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppleTheme.colors.accent
                        )
                    }
                    Slider(
                        value = orbSizeSlider,
                        onValueChange = { orbSizeSlider = it },
                        valueRange = 160f..280f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = AppleTheme.colors.accent,
                            inactiveTrackColor = AppleTheme.colors.surfaceSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Card Corner Curvature",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = AppleTheme.colors.textPrimary
                        )
                        Text(
                            text = "${cornerRadiusSlider.toInt()} dp",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = ApplePurpleDark
                        )
                    }
                    Slider(
                        value = cornerRadiusSlider,
                        onValueChange = { cornerRadiusSlider = it },
                        valueRange = 12f..32f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = ApplePurpleDark,
                            inactiveTrackColor = AppleTheme.colors.surfaceSecondary
                        )
                    )
                }
            }
        }
    }
}
