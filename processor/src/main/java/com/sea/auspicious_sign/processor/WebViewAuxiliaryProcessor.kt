package com.sea.auspicious_sign.processor

import com.google.auto.service.AutoService
import com.sea.auspicious_sign.annotations.WebViewAuxiliary
import com.sea.auspicious_sign.core.WebViewAuxiliaryInitializer
import com.squareup.kotlinpoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class WebViewAuxiliaryProcessor : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var filer: Filer

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        mutableSetOf(WebViewAuxiliary::class.java.canonicalName)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        filer = processingEnv.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val auxiliaryElements = roundEnv.getElementsAnnotatedWith(WebViewAuxiliary::class.java)
            .filter { it.kind == ElementKind.CLASS }
            .mapNotNull { element ->
                val typeElement = element as TypeElement
                val implementsInterface = typeElement.interfaces.any {
                    it.toString() == WebViewAuxiliaryInitializer::class.java.canonicalName
                }
                if (!implementsInterface) {
                    messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "${element.simpleName} is annotated with @WebViewAuxiliary but does not implement WebViewAuxiliaryInitializer",
                        element
                    )
                    return@mapNotNull null
                }
                typeElement
            }

        if (auxiliaryElements.isEmpty()) return false

        val fileSpec = generateRegistryFile(auxiliaryElements)
        fileSpec.writeTo(filer)
        return true
    }

    private fun generateRegistryFile(elements: List<TypeElement>): FileSpec {
        val classBuilder = TypeSpec.objectBuilder("AuxiliaryRegistry")
            .addFunction(
                FunSpec.builder("initializeAll")
                    .addParameter("activity", ClassName("androidx.appcompat.app", "AppCompatActivity"))
                    .addCode(elements.joinToString("\n") { element ->
                        val className = ClassName.get(element)
                        val instanceName = element.simpleName.toString().decapitalize()
                        """
                        val $instanceName = ${className.canonicalName}()
                        $instanceName.initialize(activity)
                        """.trimIndent()
                    })
                    .returns(Unit::class)
                    .build()
            )
            .build()

        return FileSpec.builder("com.sea.auspicious_sign.generated", "AuxiliaryRegistry")
            .addType(classBuilder)
            .build()
    }
}