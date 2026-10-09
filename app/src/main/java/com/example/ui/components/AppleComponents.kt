package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppleBlue
import com.example.ui.theme.LocalAppleColors

/**
 * Apple-style grouped section header
 */
@Composable
fun AppleSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    caption: String? = null
) {
    val colors = LocalAppleColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                letterSpacing = 0.8.sp
            ),
            color = colors.textSecondary
        )
        if (caption != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = colors.textTertiary
            )
        }
    }
}

/**
 * Apple-style grouped card (18–22px corner radius)
 */
@Composable
fun AppleGroupedCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalAppleColors.current
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surface)
            .border(0.75.dp, colors.border, shape)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

/**
 * Apple Settings Row with iOS squircle icon, title, subtitle, and action
 */
@Composable
fun AppleSettingRow(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = Color.White,
    iconBgColor: Color = AppleBlue,
    trailingText: String? = null,
    isChecked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    showChevron: Boolean = onClick != null && isChecked == null,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppleColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = onClick != null || onCheckedChange != null,
                interactionSource = interactionSource,
                indication = null
            ) {
                if (isChecked != null && onCheckedChange != null) {
                    onCheckedChange(!isChecked)
                } else {
                    onClick?.invoke()
                }
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // iOS squircle icon container
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                color = if (isDestructive) Color(0xFFFF3B30) else colors.textPrimary
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        lineHeight = 16.sp
                    ),
                    color = colors.textSecondary
                )
            }
        }

        if (trailingText != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = trailingText,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                color = colors.textSecondary
            )
        }

        if (isChecked != null && onCheckedChange != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AppleBlue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = colors.surfaceSecondary,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        } else if (showChevron) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.textTertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Apple-style inset divider
 */
@Composable
fun AppleDivider(
    startIndent: Dp = 62.dp,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppleColors.current
    HorizontalDivider(
        modifier = modifier.padding(start = startIndent),
        thickness = 0.5.dp,
        color = colors.divider
    )
}

/**
 * Apple Status Pill (Compact capsule badge)
 */
@Composable
fun AppleStatusPill(
    text: String,
    tint: Color = AppleBlue,
    isFilled: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppleColors.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (isFilled) tint else tint.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            ),
            color = if (isFilled) Color.White else tint
        )
    }
}
