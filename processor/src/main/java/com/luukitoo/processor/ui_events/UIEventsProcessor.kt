package com.luukitoo.processor.ui_events

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.luukitoo.annotation.UIEvents
import com.luukitoo.processor.base.ClassVisitor
import com.luukitoo.processor.base.KotlinSymbolProcessor
import com.luukitoo.processor.extension.forEachSymbolAnnotatedWith
import com.luukitoo.processor.extension.ifNotDataClass
import com.luukitoo.processor.ui_events.visitor.CreateCustomEventsClassVisitor
import com.luukitoo.processor.ui_events.visitor.CreateEmptyEventsClassVisitor
import com.luukitoo.processor.ui_events.visitor.CreateRootTypeClassVisitor
import com.luukitoo.processor.ui_events.visitor.CreateUpdateEventsClassVisitor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * [UIEvents] processor itself.
 * Code is generated here.
 * */
class UIEventsProcessor(
    environment: SymbolProcessorEnvironment,
) : KotlinSymbolProcessor(environment) {

    override fun handleProcessing(): List<KSAnnotated> {
        utils.resolver.forEachSymbolAnnotatedWith(UIEvents::class) { annotatedSymbol ->
            annotatedSymbol.accept(annotatedClassVisitor, Unit)
        }
        return emptyList()
    }

    private val annotatedClassVisitor = ClassVisitor.create { classDeclaration ->
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
        val updateEvents = classDeclaration.accept(
            visitor = CreateUpdateEventsClassVisitor(),
            data = rootClassType
        )
        val customEvents = classDeclaration.accept(
            visitor = CreateCustomEventsClassVisitor(),
            data = rootClassType
        )
        val emptyEvents = classDeclaration.accept(
            visitor = CreateEmptyEventsClassVisitor(),
            data = rootClassType
        )
        val rootClass = classDeclaration.accept(
            visitor = CreateRootTypeClassVisitor(),
            data = CreateRootTypeClassVisitor.Params(
                type = rootClassType,
                events = updateEvents + customEvents + emptyEvents
            )
        )
        generateFile(classDeclaration, rootClass)
    }

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
