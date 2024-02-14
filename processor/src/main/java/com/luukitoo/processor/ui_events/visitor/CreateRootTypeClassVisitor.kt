package com.luukitoo.processor.ui_events.visitor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.luukitoo.processor.base.ClassVisitor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

class CreateRootTypeClassVisitor : ClassVisitor<CreateRootTypeClassVisitor.Params, TypeSpec>() {

    override fun visitClass(ksClass: KSClassDeclaration, param: Params): TypeSpec {
        return TypeSpec.classBuilder(param.type)
            .addModifiers(KModifier.SEALED)
            .addTypes(param.events)
            .addKdoc(
                CodeBlock.builder()
                    .addStatement("Events class for [%T]", ksClass.toClassName())
                    .build()
            ).build()
    }

    data class Params(
        val type: ClassName,
        val events: List<TypeSpec>
    )
}