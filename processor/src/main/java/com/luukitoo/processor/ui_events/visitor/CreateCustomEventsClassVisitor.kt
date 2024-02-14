package com.luukitoo.processor.ui_events.visitor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.luukitoo.annotation.CustomEvent
import com.luukitoo.processor.base.ClassVisitor
import com.luukitoo.processor.extension.isAnnotation
import com.luukitoo.processor.ui_events.annotation_params.CustomEventParams
import com.luukitoo.processor.util.SimpleBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec

class CreateCustomEventsClassVisitor : ClassVisitor<ClassName, List<TypeSpec>>() {

    override fun visitClass(ksClass: KSClassDeclaration, param: ClassName): List<TypeSpec> {
        val customEvents = ksClass.annotations.filter {
            it.isAnnotation(CustomEvent::class)
        }
        return customEvents.mapNotNull { ksAnnotation ->
            val params = CustomEventParams.createFrom(ksAnnotation) ?: return@mapNotNull null
            SimpleBuilder.dataClass(
                className = params.eventName,
                superClass = param,
                params = listOf(params.paramName to params.paramType)
            )
        }.toList()
    }
}