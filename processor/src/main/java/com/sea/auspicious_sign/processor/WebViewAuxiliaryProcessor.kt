package com.sea.auspicious_sign.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

class WebViewAuxiliaryProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("com.sea.auspicious_sign.annotations.WebViewAuxiliary")
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .toList()

        if (symbols.isEmpty()) return emptyList()

        // 验证每个符号是否实现了 WebViewAuxiliaryInitializer
        val validClasses = symbols.filter { clazz ->
            clazz.superTypes.any { superType ->
                superType.resolve().declaration.qualifiedName?.asString() == "com.sea.auspicious_sign.core.WebViewAuxiliaryInitializer"
            }
        }

        if (validClasses.isNotEmpty()) {
            generateRegistry(validClasses)
        } else {
            environment.logger.warn("No valid auxiliary classes found")
        }
        return emptyList()
    }

    private fun generateRegistry(classes: List<KSClassDeclaration>) {
        val packageName = "com.sea.auspicious_sign.generated"
        val className = "AuxiliaryRegistry"

        val code = buildString {
            appendLine("package $packageName")
            appendLine()
            appendLine("import android.app.Activity")
            appendLine("import kotlinx.coroutines.CoroutineScope")
            appendLine("import kotlinx.coroutines.launch")
            appendLine()
            appendLine("object $className {")
            appendLine("    suspend fun initializeAll(activity: Activity) {")
            for (clazz in classes) {
                val fullName = clazz.qualifiedName!!.asString()
                val instanceName = clazz.simpleName.asString().decapitalize()
                appendLine("        val $instanceName = $fullName()")
                appendLine("        $instanceName.initialize(activity)")
            }
            appendLine("    }")
            appendLine("}")
        }

        try {
            val file = environment.codeGenerator.createNewFile(
                dependencies = Dependencies(false),
                packageName = packageName,
                fileName = className
            )
            file.bufferedWriter().use { writer ->
                writer.write(code)
            }
            environment.logger.info("Generated $className successfully")
        } catch (e: Exception) {
            environment.logger.error("Failed to generate file: ${e.message}")
        }
    }
}

class WebViewAuxiliaryProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return WebViewAuxiliaryProcessor(environment)
    }
}