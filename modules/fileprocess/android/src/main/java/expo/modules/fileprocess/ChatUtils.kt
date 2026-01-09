package expo.modules.fileprocess

import java.io.File
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import org.json.JSONObject
import org.json.JSONArray

/**
 * 流式读取聊天文件，返回结构化对象列表
 * 利用 Expo Modules 新架构（JSI）直接传递对象，避免 JSON 序列化开销
 */
fun readChatFileStreaming(filePath: String): List<Map<String, Any?>> {
    val cleanPath = if (filePath.startsWith("file://")) filePath.substring(7) else filePath
    val file = File(cleanPath)
    
    if (!file.exists()) {
        throw Exception("File does not exist: $cleanPath")
    }
    
    val result = mutableListOf<Map<String, Any?>>()
    
    BufferedReader(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)).use { reader ->
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            val trimmedLine = line?.trim()
            if (!trimmedLine.isNullOrEmpty()) {
                try {
                    val jsonObj = JSONObject(trimmedLine)
                    result.add(jsonObj.toMap())
                } catch (e: Exception) {
                    // 跳过无效行
                }
            }
            // 每行解析完后，line 变量会被下一次循环覆盖
            // JVM 会自动回收不再引用的字符串
        }
    }
    
    return result
}

/**
 * JSONObject 转换为 Map 的扩展函数
 */
private fun JSONObject.toMap(): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    val keys = this.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        map[key] = when (val value = this.get(key)) {
            is JSONObject -> value.toMap()
            is JSONArray -> value.toList()
            JSONObject.NULL -> null
            else -> value
        }
    }
    return map
}

/**
 * JSONArray 转换为 List 的扩展函数
 */
private fun JSONArray.toList(): List<Any?> {
    val list = mutableListOf<Any?>()
    for (i in 0 until this.length()) {
        list.add(when (val value = this.get(i)) {
            is JSONObject -> value.toMap()
            is JSONArray -> value.toList()
            JSONObject.NULL -> null
            else -> value
        })
    }
    return list
}

/**
 * 获取聊天文件列表信息
 * 只读取每个文件的头部和第一条消息，不加载全部内容
 * 直接返回结构化数据，利用 Expo Modules 新架构自动序列化
 */
fun getChatFilesInfo(directoryPath: String): List<Map<String, Any>> {
    val cleanPath = if (directoryPath.startsWith("file://")) directoryPath.substring(7) else directoryPath
    val dir = File(cleanPath)
    
    if (!dir.exists() || !dir.isDirectory) {
        return emptyList()
    }
    
    val result = mutableListOf<Map<String, Any>>()
    val jsonlFiles = dir.listFiles { _, name -> name.endsWith(".jsonl") } ?: emptyArray()
    
    for (file in jsonlFiles) {
        try {
            val fileInfo = mutableMapOf<String, Any>()
            
            // 文件名 - 直接获取
            fileInfo["file_name"] = file.name
            
            // 文件大小 - 元数据操作，不读取内容
            fileInfo["file_size"] = file.length()
            
            // 最后修改时间 - 元数据操作，不读取内容
            fileInfo["last_modified"] = file.lastModified()
            
            // 消息总数和预览 - 流式读取
            var lineCount = 0
            var previewMessage = ""
            
            BufferedReader(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)).use { reader ->
                var line: String?
                var lineIndex = 0
                
                while (reader.readLine().also { line = it } != null) {
                    val trimmedLine = line?.trim()
                    if (!trimmedLine.isNullOrEmpty()) {
                        lineCount++
                        
                        // 只在第二行（第一条消息）时解析 JSON 获取预览
                        if (lineIndex == 1) {
                            try {
                                val msgObj = JSONObject(trimmedLine)
                                val mes = msgObj.optString("mes", "")
                                val maxLength = 400
                                previewMessage = if (mes.length > maxLength) {
                                    "..." + mes.substring(mes.length - maxLength)
                                } else {
                                    mes
                                }
                            } catch (e: Exception) {
                                previewMessage = "无法加载预览"
                            }
                        }
                        lineIndex++
                    }
                }
            }
            
            // 消息数 = 总行数 - 1（减去头信息行）
            fileInfo["message_count"] = if (lineCount > 0) lineCount - 1 else 0
            fileInfo["preview_message"] = previewMessage
            
            result.add(fileInfo)
            
        } catch (e: Exception) {
            // 跳过处理失败的文件
        }
    }
    
    return result
}

/**
 * 流式保存聊天记录 - Native 侧自己读取 header
 * 利用 Expo Modules 新架构（JSI）直接接收对象，避免 JSON 序列化开销
 * JS 侧只需传递文件路径和消息数组，无需先读取文件获取 header
 *
 * @param filePath 文件路径（必须是已存在的聊天文件）
 * @param messages 消息列表（List<Map> 对象）
 */
fun saveChatStreaming(filePath: String, messages: List<Map<String, Any?>>) {
    val cleanPath = if (filePath.startsWith("file://")) filePath.substring(7) else filePath
    val targetFile = File(cleanPath)
    
    if (!targetFile.exists()) {
        throw Exception("File does not exist: $cleanPath")
    }
    
    // 读取文件第一行获取 header
    val headerLine: String
    BufferedReader(InputStreamReader(FileInputStream(targetFile), StandardCharsets.UTF_8)).use { reader ->
        headerLine = reader.readLine()?.trim()
            ?: throw Exception("File is empty, cannot read header: $cleanPath")
    }
    
    // 验证 header 是有效的 JSON
    try {
        JSONObject(headerLine)
    } catch (e: Exception) {
        throw Exception("Invalid header format in file: $cleanPath")
    }
    
    // 创建临时文件（与目标文件在同一目录，确保在同一文件系统上）
    val tempFile = File(targetFile.parent, "${targetFile.name}.tmp")
    
    try {
        // 写入临时文件
        BufferedWriter(OutputStreamWriter(FileOutputStream(tempFile), StandardCharsets.UTF_8)).use { writer ->
            // 写入原有的 header
            writer.write(headerLine)
            
            // 逐条将消息 Map 转换为 JSON 字符串并写入
            for (message in messages) {
                writer.newLine()
                writer.write(mapToJsonString(message))
            }
        }
        
        // 写入成功后，执行原子替换
        if (targetFile.exists()) {
            targetFile.delete()
        }
        
        val renamed = tempFile.renameTo(targetFile)
        if (!renamed) {
            // renameTo 失败时的备用方案：复制内容
            tempFile.copyTo(targetFile, overwrite = true)
            tempFile.delete()
        }
        
    } catch (e: Exception) {
        // 写入失败，删除临时文件，保留原文件不变
        if (tempFile.exists()) {
            tempFile.delete()
        }
        throw Exception("Failed to save chat file: ${e.message}")
    }
}

/**
 * 将 Map 转换为 JSON 字符串
 * 递归处理嵌套的 Map 和 List
 */
private fun mapToJsonString(map: Map<String, Any?>): String {
    val jsonObject = JSONObject()
    for ((key, value) in map) {
        jsonObject.put(key, convertToJsonValue(value))
    }
    return jsonObject.toString()
}

/**
 * 将值转换为 JSON 兼容的类型
 */
private fun convertToJsonValue(value: Any?): Any? {
    return when (value) {
        null -> JSONObject.NULL
        is Map<*, *> -> {
            val jsonObject = JSONObject()
            @Suppress("UNCHECKED_CAST")
            for ((k, v) in value as Map<String, Any?>) {
                jsonObject.put(k, convertToJsonValue(v))
            }
            jsonObject
        }
        is List<*> -> {
            val jsonArray = JSONArray()
            for (item in value) {
                jsonArray.put(convertToJsonValue(item))
            }
            jsonArray
        }
        is Number, is String, is Boolean -> value
        else -> value.toString()
    }
}

/**
 * 追加消息到聊天文件（高效追加，不读取整个文件）
 */
fun appendMessageToChat(filePath: String, messageJson: String) {
    val cleanPath = if (filePath.startsWith("file://")) filePath.substring(7) else filePath
    val file = File(cleanPath)
    
    if (!file.exists()) {
        throw Exception("File does not exist: $cleanPath")
    }
    
    // 使用追加模式打开文件，显式指定 UTF-8 编码
    BufferedWriter(OutputStreamWriter(FileOutputStream(file, true), StandardCharsets.UTF_8)).use { writer ->
        writer.newLine()
        writer.write(messageJson)
    }
}