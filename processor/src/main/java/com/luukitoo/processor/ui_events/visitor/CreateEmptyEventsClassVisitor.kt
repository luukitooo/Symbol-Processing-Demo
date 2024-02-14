package com.luukitoo.processor.ui_events.visitor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.luukitoo.annotation.UIEvents
import com.luukitoo.processor.base.ClassVisitor
import com.luukitoo.processor.extension.getAnnotationOrNull
import com.luukitoo.processor.ui_events.annotation_params.UIEventsParams
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

class CreateEmptyEventsClassVisitor : ClassVisitor<ClassName, List<TypeSpec>>() {

    override fun visitClass(ksClass: KSClassDeclaration, param: ClassName): List<TypeSpec> {
        val ksAnnotation = ksClass.getAnnotationOrNull(UIEvents::class)
        val params = UIEventsParams.createFrom(ksAnnotation)
        return params.emptyEvents.map {
            TypeSpec.objectBuilder(it)
                .addModifiers(KModifier.DATA)
                .superclass(param)
                .build()
        }
    }
}