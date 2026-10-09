package com.example.data.repository

import com.example.data.model.ActionType
import com.example.data.model.MemoryItem
import com.example.data.model.SkillItem
import com.example.data.model.TaskStep
import com.example.data.model.TrainedTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class MemoryAndTaskRepository {

    private val _memories = MutableStateFlow<List<MemoryItem>>(
        listOf(
            MemoryItem(
                id = "mem_1",
                title = "Preferred Music Language",
                content = "Prefers Hindi retro and Coke Studio acoustic songs when playing music.",
                category = "Preference"
            ),
            MemoryItem(
                id = "mem_2",
                title = "Work Notification Priority",
                content = "Always prioritize WhatsApp client messages over general social updates.",
                category = "Work"
            ),
            MemoryItem(
                id = "mem_3",
                title = "Default YouTube Quality",
                content = "Prefer 1080p playback and skip sponsor segments automatically if possible.",
                category = "Automation"
            ),
            MemoryItem(
                id = "mem_4",
                title = "Daily Routine",
                content = "Ask for daily briefing at 8:30 AM every weekday.",
                category = "Personal"
            )
        )
    )
    val memories: StateFlow<List<MemoryItem>> = _memories.asStateFlow()

    private val _trainedTasks = MutableStateFlow<List<TrainedTask>>(
        listOf(
            TrainedTask(
                id = "task_yt_hindi",
                name = "YouTube Hindi Songs Player",
                description = "Opens YouTube, locates search, enters 'top Hindi songs' and plays 2nd result.",
                source = "YouTube",
                triggerPhrase = "Play top Hindi songs on YouTube",
                steps = listOf(
                    TaskStep("s1", 1, "Listen and understand target application", ActionType.WAIT_FOR_SPEECH),
                    TaskStep("s2", 2, "Launch YouTube Application", ActionType.OPEN_APP, targetApp = "com.google.android.youtube"),
                    TaskStep("s3", 3, "Inspect current YouTube home screen UI elements", ActionType.READ_SCREEN),
                    TaskStep("s4", 4, "Locate YouTube search button or top bar input", ActionType.LOCATE_ELEMENT, targetElement = "Search Button"),
                    TaskStep("s5", 5, "Tap Search Button via Accessibility Node", ActionType.ACCESSIBILITY_CLICK, targetElement = "Search Button"),
                    TaskStep("s6", 6, "Enter query 'top Hindi songs' into input field", ActionType.TYPE_TEXT, targetElement = "Search Input"),
                    TaskStep("s7", 7, "Analyze search result video cards on screen", ActionType.READ_SCREEN),
                    TaskStep("s8", 8, "Detect 2nd video card at screen coordinates (X: 720, Y: 1380)", ActionType.COORDINATE_TAP, coordinateX = 720, coordinateY = 1380),
                    TaskStep("s9", 9, "Confirm video playback and complete task", ActionType.SPEAK_REPLY)
                ),
                runCount = 14
            ),
            TrainedTask(
                id = "task_gemini_search",
                name = "Gemini Search Workflow",
                description = "Opens browser, connects to Gemini, enters query and captures response summary.",
                source = "Gemini",
                triggerPhrase = "Research topic with Gemini",
                steps = listOf(
                    TaskStep("gs1", 1, "Process research query", ActionType.WAIT_FOR_SPEECH),
                    TaskStep("gs2", 2, "Open Browser / Search Engine", ActionType.OPEN_APP, targetApp = "com.android.chrome"),
                    TaskStep("gs3", 3, "Find web URL or prompt box", ActionType.LOCATE_ELEMENT, targetElement = "Search / Prompt Box"),
                    TaskStep("gs4", 4, "Inject query prompt into input", ActionType.TYPE_TEXT),
                    TaskStep("gs5", 5, "Submit query and wait for stream", ActionType.ACCESSIBILITY_CLICK, targetElement = "Send Button"),
                    TaskStep("gs6", 6, "Extract generated summary to Maya Memory", ActionType.SPEAK_REPLY)
                ),
                runCount = 28
            ),
            TrainedTask(
                id = "task_chatgpt_search",
                name = "ChatGPT Deep Query",
                description = "Executes multi-step query on ChatGPT app or web interface.",
                source = "ChatGPT",
                triggerPhrase = "Ask ChatGPT for solution",
                steps = listOf(
                    TaskStep("cg1", 1, "Parse user voice question", ActionType.WAIT_FOR_SPEECH),
                    TaskStep("cg2", 2, "Switch to ChatGPT", ActionType.OPEN_APP, targetApp = "com.openai.chatgpt"),
                    TaskStep("cg3", 3, "Focus chat text area", ActionType.ACCESSIBILITY_CLICK, targetElement = "Message input"),
                    TaskStep("cg4", 4, "Type text query", ActionType.TYPE_TEXT),
                    TaskStep("cg5", 5, "Tap Send and monitor output", ActionType.ACCESSIBILITY_CLICK, targetElement = "Submit button")
                ),
                runCount = 9
            ),
            TrainedTask(
                id = "task_gemini_image",
                name = "Gemini Image Generator",
                description = "Automates AI image generation prompt workflow.",
                source = "Gemini",
                triggerPhrase = "Generate AI image with Gemini",
                steps = listOf(
                    TaskStep("gi1", 1, "Analyze image generation prompt", ActionType.WAIT_FOR_SPEECH),
                    TaskStep("gi2", 2, "Formulate prompt with style tags", ActionType.TYPE_TEXT),
                    TaskStep("gi3", 3, "Tap Generate button", ActionType.COORDINATE_TAP, coordinateX = 540, coordinateY = 960),
                    TaskStep("gi4", 4, "Save output to Gallery", ActionType.ACCESSIBILITY_CLICK, targetElement = "Save Button")
                ),
                runCount = 6
            ),
            TrainedTask(
                id = "task_gemini_video",
                name = "Gemini Video Workflow",
                description = "Prepares and triggers video generation pipeline.",
                source = "Gemini",
                triggerPhrase = "Create AI video workflow",
                steps = listOf(
                    TaskStep("gv1", 1, "Plan video storyboard", ActionType.WAIT_FOR_SPEECH),
                    TaskStep("gv2", 2, "Open Video AI studio", ActionType.OPEN_APP, targetApp = "com.google.android.apps.bard"),
                    TaskStep("gv3", 3, "Enter prompt sequence", ActionType.TYPE_TEXT),
                    TaskStep("gv4", 4, "Start video render", ActionType.ACCESSIBILITY_CLICK, targetElement = "Render Video")
                ),
                runCount = 3
            )
        )
    )
    val trainedTasks: StateFlow<List<TrainedTask>> = _trainedTasks.asStateFlow()

    private val _skills = MutableStateFlow<List<SkillItem>>(
        listOf(
            SkillItem("sk_1", "group-report", "WhatsApp", "Generates concise analytical summaries of busy WhatsApp group chats.", "Groups"),
            SkillItem("sk_2", "pro-whatsapp", "WhatsApp", "Composes polished, formal WhatsApp business communications.", "Chat"),
            SkillItem("sk_3", "youtube-script", "YouTube", "Creates complete structured YouTube video scripts with hooks and outlines.", "Movie"),
            SkillItem("sk_4", "youtube-title-thumbnail", "YouTube", "Generates high-CTR video titles and thumbnail concept designs.", "Image"),
            SkillItem("sk_5", "youtube-upload", "YouTube", "Prepares metadata, tags, and automation for YouTube uploads.", "Upload"),
            SkillItem("sk_6", "social-posting", "Social Media", "Schedules and crafts cross-platform posts for Instagram, X, Facebook.", "Share"),
            SkillItem("sk_7", "web-design", "Web", "Drafts modern UI layouts, responsive wireframes, and design specs.", "Web"),
            SkillItem("sk_8", "coding-agent", "Coding", "Writes, debugs, and refactors Kotlin, Python, JS, and HTML code.", "Code"),
            SkillItem("sk_9", "code-publish", "Coding", "Automates git commits, build scripts, and web publishing pipelines.", "Publish"),
            SkillItem("sk_10", "daily-briefing", "Productivity", "Aggregates weather, agenda, top news, and pending tasks in one voice briefing.", "Today")
        )
    )
    val skills: StateFlow<List<SkillItem>> = _skills.asStateFlow()

    fun addMemory(title: String, content: String, category: String) {
        val newMem = MemoryItem(
            id = "mem_" + UUID.randomUUID().toString().take(6),
            title = title,
            content = content,
            category = category
        )
        _memories.value = listOf(newMem) + _memories.value
    }

    fun deleteMemory(id: String) {
        _memories.value = _memories.value.filter { it.id != id }
    }

    fun addTrainedTask(task: TrainedTask) {
        _trainedTasks.value = listOf(task) + _trainedTasks.value
    }

    fun deleteTrainedTask(id: String) {
        _trainedTasks.value = _trainedTasks.value.filter { it.id != id }
    }

    fun exportToJson(): String {
        val root = JSONObject()
        val memArray = JSONArray()
        _memories.value.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("title", it.title)
            obj.put("content", it.content)
            obj.put("category", it.category)
            obj.put("timestamp", it.timestamp)
            memArray.put(obj)
        }
        root.put("memories", memArray)

        val taskArray = JSONArray()
        _trainedTasks.value.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("name", it.name)
            obj.put("description", it.description)
            obj.put("source", it.source)
            obj.put("triggerPhrase", it.triggerPhrase)
            obj.put("runCount", it.runCount)
            taskArray.put(obj)
        }
        root.put("trained_tasks", taskArray)
        root.put("exported_at", System.currentTimeMillis())
        root.put("agent", "Maya AI v1.0")
        return root.toString(2)
    }

    fun restoreFromJson(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)
            val memArray = root.optJSONArray("memories")
            if (memArray != null) {
                val list = mutableListOf<MemoryItem>()
                for (i in 0 until memArray.length()) {
                    val obj = memArray.getJSONObject(i)
                    list.add(
                        MemoryItem(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            title = obj.getString("title"),
                            content = obj.getString("content"),
                            category = obj.optString("category", "Personal"),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                        )
                    )
                }
                _memories.value = list
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
