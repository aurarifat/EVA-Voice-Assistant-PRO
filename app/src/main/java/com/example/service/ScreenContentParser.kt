package com.example.service

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.example.data.model.ScreenElement

/**
 * Parses and serializes on-screen content into structured representations
 * for Gemini AI understanding, autonomous navigation, and intent matching.
 */
data class ParsedScreenContent(
    val packageName: String,
    val windowTitle: String,
    val elements: List<ScreenElement>,
    val clickableElements: List<ScreenElement>,
    val inputFields: List<ScreenElement>,
    val fullTextSummary: String,
    val formattedPromptContext: String
)

object ScreenContentParser {

    /**
     * Recursively traverses an AccessibilityNodeInfo tree and extracts all interactive
     * and informative UI elements with coordinate bounding boxes.
     */
    fun parseScreen(root: AccessibilityNodeInfo?, currentPackage: String = ""): ParsedScreenContent {
        if (root == null) {
            return ParsedScreenContent(
                packageName = currentPackage,
                windowTitle = "",
                elements = emptyList(),
                clickableElements = emptyList(),
                inputFields = emptyList(),
                fullTextSummary = "No active window or screen elements accessible.",
                formattedPromptContext = "Current screen is blank or inaccessible."
            )
        }

        val allElements = mutableListOf<ScreenElement>()
        val textPieces = mutableListOf<String>()

        traverse(root, allElements, textPieces)

        val clickables = allElements.filter { it.isClickable }
        val inputs = allElements.filter { it.isEditable }

        val summary = textPieces.filter { it.isNotBlank() }.distinct().joinToString(" | ")

        // Format clean concise screen representation for the AI Agent
        val formattedContext = buildString {
            appendLine("CURRENT SCREEN CONTEXT:")
            appendLine("- Active App: ${currentPackage.ifBlank { root.packageName?.toString() ?: "Unknown" }}")
            appendLine("- Total Detected UI Elements: ${allElements.size}")
            appendLine("- Clickable Buttons/Targets (${clickables.size}):")
            clickables.take(15).forEach { el ->
                val label = el.text.ifBlank { el.viewId ?: "unnamed_button" }
                appendLine("  • \"$label\" at (${(el.boundsLeft + el.boundsRight) / 2}, ${(el.boundsTop + el.boundsBottom) / 2})")
            }
            if (inputs.isNotEmpty()) {
                appendLine("- Editable Text Fields (${inputs.size}):")
                inputs.forEach { input ->
                    appendLine("  • Field \"${input.text.ifBlank { input.viewId ?: "text_input" }}\"")
                }
            }
            appendLine("- On-Screen Text Snippet: \"${summary.take(300)}\"")
        }

        return ParsedScreenContent(
            packageName = currentPackage.ifBlank { root.packageName?.toString() ?: "" },
            windowTitle = root.packageName?.toString() ?: "",
            elements = allElements,
            clickableElements = clickables,
            inputFields = inputs,
            fullTextSummary = summary,
            formattedPromptContext = formattedContext
        )
    }

    private fun traverse(node: AccessibilityNodeInfo, elements: MutableList<ScreenElement>, textCollector: MutableList<String>) {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)

        val text = node.text?.toString()?.trim() ?: ""
        val desc = node.contentDescription?.toString()?.trim() ?: ""
        val primaryText = if (text.isNotBlank()) text else desc

        if (primaryText.isNotBlank()) {
            textCollector.add(primaryText)
        }

        val viewId = node.viewIdResourceName
        val isClickable = node.isClickable
        val isEditable = node.isEditable

        if (primaryText.isNotBlank() || isClickable || isEditable) {
            elements.add(
                ScreenElement(
                    id = "el_${elements.size + 1}",
                    text = primaryText,
                    viewId = viewId,
                    isClickable = isClickable,
                    isEditable = isEditable,
                    boundsLeft = bounds.left,
                    boundsTop = bounds.top,
                    boundsRight = bounds.right,
                    boundsBottom = bounds.bottom,
                    className = node.className?.toString() ?: "android.view.View"
                )
            )
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            traverse(child, elements, textCollector)
        }
    }
}
