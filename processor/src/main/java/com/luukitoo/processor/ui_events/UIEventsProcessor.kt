package com.luukitoo.processor.ui_events

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.luukitoo.annotation.CustomEvent
import com.luukitoo.annotation.IgnoreEvent
import com.luukitoo.annotation.UIEvents
import com.luukitoo.processor.base.KotlinSymbolProcessor
import com.luukitoo.processor.util.classVisitorVoid
import com.luukitoo.processor.extension.asUpdateEventName
import com.luukitoo.processor.extension.forEachSymbolAnnotatedWith
import com.luukitoo.processor.extension.getAnnotationOrNull
import com.luukitoo.processor.extension.getStateProperties
import com.luukitoo.processor.extension.ifNotDataClass
import com.luukitoo.processor.extension.isAnnotation
import com.luukitoo.processor.ui_events.annotation_params.CustomEventParams
import com.luukitoo.processor.ui_events.annotation_params.UIEventsParams
import com.luukitoo.processor.util.SimpleBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * Most interesting stuff is happening here :P
 * */
class UIEventsProcessor(
    environment: SymbolProcessorEnvironment,
) : KotlinSymbolProcessor(environment) {

    override fun handleProcessing(): List<KSAnnotated> {
        utils.resolver.forEachSymbolAnnotatedWith(UIEvents::class) { annotatedSymbol ->
            annotatedSymbol.accept(classVisitor, Unit)
        }
        return emptyList()
    }

    private val classVisitor = classVisitorVoid { classDeclaration ->
        classDeclaration.ifNotDataClass {
            utils.logger.error(
                message = "@UIEvents must target data classes.",
                symbol = classDeclaration
            )
        }
        generateEvents(classDeclaration)
    }

    private fun generateEvents(classDeclaration: KSClassDeclaration) {
        val rootClassType = ClassName(
            classDeclaration.packageName.asString(),
            classDeclaration.simpleName.asString().plus("Events")
        )
        val updateEvents = createUpdateEvents(
            classDeclaration = classDeclaration,
            rootClassType = rootClassType
        )
        val customEvents = createCustomEvents(
            classDeclaration = classDeclaration,
            rootClassType = rootClassType
        )
        val emptyEvents = createEmptyEvents(
            classDeclaration = classDeclaration,
            rootClassType = rootClassType
        )
        val rootClass = createRootClass(
            type = rootClassType,
            classDeclaration = classDeclaration,
            events = updateEvents + customEvents + emptyEvents
        )
        generateFile(classDeclaration, rootClass)
    }

    private fun createUpdateEvents(
        classDeclaration: KSClassDeclaration,
        rootClassType: ClassName,
    ): List<TypeSpec> {
        val ksAnnotation = classDeclaration.getAnnotationOrNull(UIEvents::class)
        val autoGenerationEnabled = UIEventsParams.createFrom(ksAnnotation).autoGeneration
        return if (autoGenerationEnabled) {
            classDeclaration.getStateProperties().mapNotNull { propertyDeclaration ->
                if (propertyDeclaration.getAnnotationOrNull(IgnoreEvent::class) != null) {
                    return@mapNotNull null
                }
                SimpleBuilder.dataClass(
                    className = propertyDeclaration.asUpdateEventName(),
                    superClass = rootClassType,
                    params = listOf("newValue" to propertyDeclaration.type.toTypeName()),
                )
            }
        } else {
            emptyList()
        }
    }

    private fun createCustomEvents(
        classDeclaration: KSClassDeclaration,
        rootClassType: ClassName,
    ): List<TypeSpec> {
        val customEvents = classDeclaration.annotations.filter {
            it.isAnnotation(CustomEvent::class)
        }
        return customEvents.mapNotNull { ksAnnotation ->
            val params = CustomEventParams.createFrom(ksAnnotation) ?: return@mapNotNull null
            SimpleBuilder.dataClass(
                className = params.eventName,
                superClass = rootClassType,
                params = listOf(params.paramName to params.paramType)
            )
        }.toList()
    }

    private fun createEmptyEvents(
        classDeclaration: KSClassDeclaration,
        rootClassType: ClassName,
    ): List<TypeSpec> {
        val ksAnnotation = classDeclaration.getAnnotationOrNull(UIEvents::class)
        val params = UIEventsParams.createFrom(ksAnnotation)
        return params.emptyEvents.map {
            TypeSpec.objectBuilder(it)
                .addModifiers(KModifier.DATA)
                .superclass(rootClassType)
                .build()
        }
    }

    private fun createRootClass(
        type: ClassName,
        classDeclaration: KSClassDeclaration,
        events: List<TypeSpec>,
    ) = TypeSpec.classBuilder(type)
        .addModifiers(KModifier.SEALED)
        .addTypes(events)
        .addKdoc(
            CodeBlock.builder()
                .addStatement("Events class for [%T]", classDeclaration.toClassName())
                .build()
        ).build()

    private fun generateFile(
        classDeclaration: KSClassDeclaration,
        rootClass: TypeSpec,
    ) {
        classDeclaration.containingFile?.let { ksFile ->
            FileSpec.builder(
                packageName = classDeclaration.packageName.asString(),
                fileName = classDeclaration.simpleName.asString().plus("Events")
            ).addType(rootClass)
                .build()
                .writeTo(
                    codeGenerator = utils.codeGenerator,
                    aggregating = true,
                    originatingKSFiles = listOf(ksFile)
                )
        } ?: utils.logger.error(
            message = "containingFile not found.",
            symbol = classDeclaration
        )
    }
}
