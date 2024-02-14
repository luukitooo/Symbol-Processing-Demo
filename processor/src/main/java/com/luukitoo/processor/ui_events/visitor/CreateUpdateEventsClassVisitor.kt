package com.luukitoo.processor.ui_events.visitor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.luukitoo.annotation.IgnoreEvent
import com.luukitoo.annotation.UIEvents
import com.luukitoo.processor.base.ClassVisitor
import com.luukitoo.processor.extension.asUpdateEventName
import com.luukitoo.processor.extension.getAnnotationOrNull
import com.luukitoo.processor.extension.getStateProperties
import com.luukitoo.processor.ui_events.annotation_params.UIEventsParams
import com.luukitoo.processor.util.SimpleBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

class CreateUpdateEventsClassVisitor : ClassVisitor<ClassName, List<TypeSpec>>() {

    override fun visitClass(ksClass: KSClassDeclaration, param: ClassName): List<TypeSpec> {
        val ksAnnotation = ksClass.getAnnotationOrNull(UIEvents::class)
        val autoGenerationEnabled = UIEventsParams.createFrom(ksAnnotation).autoGeneration
        return if (autoGenerationEnabled) {
            ksClass.getStateProperties().mapNotNull { propertyDeclaration ->
                if (propertyDeclaration.getAnnotationOrNull(IgnoreEvent::class) != null) {
                    return@mapNotNull null
                }
                SimpleBuilder.dataClass(
                    className = propertyDeclaration.asUpdateEventName(),
                    superClass = param,
                    params = listOf("newValue" to propertyDeclaration.type.toTypeName()),
                )
            }
        } else {
            emptyList()
        }
    }
}