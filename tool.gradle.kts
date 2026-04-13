// tool.gradle.kts - 自定义工具任务
// 使用默认文件 developer_doc/总览.md
//        ./gradlew createMissingFiles
//
// 指定其他 Markdown 文件
//    ./gradlew createMissingFiles -PmdFile="developer_doc/其他文档.md"

tasks.register("createMissingFiles") {
    doLast {
        val mdFileName = project.findProperty("mdFile") as? String ?: "developer_doc/总览.md"
        val mdFile = file(mdFileName)
        if (!mdFile.exists()) {
            println("❌ 未找到文件: ${mdFile.absolutePath}")
            return@doLast
        }

        val linkRegex = Regex("""\[.*?\]\((.*?)\)""")
        val lines = mdFile.readLines()
        val targetPaths = mutableSetOf<String>()

        val mdParent = mdFile.parentFile
        for (line in lines) {
            linkRegex.findAll(line).forEach { match ->
                val link = match.groupValues[1]
                // 忽略空、锚点、外部链接
                if (link.isBlank() || link.startsWith("#") || link.startsWith("http")) {
                    return@forEach
                }
                // 解析相对路径（相对于 md 文件所在目录）
                val targetFile = if (link.startsWith("/") || link.contains(":")) {
                    file(link)
                } else {
                    file(mdParent.resolve(link).normalize().absolutePath)
                }
                targetPaths.add(targetFile.absolutePath)
            }
        }

        var createdCount = 0
        for (path in targetPaths) {
            val f = File(path)
            if (f.exists()) {
                println("⏭️ 已存在: ${f.relativeTo(rootDir)}")
            } else {
                f.parentFile?.mkdirs()
                // 根据扩展名写 TODO 注释
                val comment = when (f.extension) {
                    "kt", "java" -> "// TODO: 实现该类的职责\n"
                    "xml" -> "<!-- TODO: 配置资源 -->\n"
                    "gradle", "kts" -> "// TODO: 配置依赖或任务\n"
                    "properties" -> "# TODO: 配置属性\n"
                    "md" -> "<!-- TODO: 补充文档内容 -->\n"
                    else -> "// TODO: 实现\n"
                }
                f.writeText(comment)
                println("✅ 已创建: ${f.relativeTo(rootDir)}")
                createdCount++
            }
        }
        println("\n🎉 完成！共创建 $createdCount 个文件，其余已存在。")
    }
}