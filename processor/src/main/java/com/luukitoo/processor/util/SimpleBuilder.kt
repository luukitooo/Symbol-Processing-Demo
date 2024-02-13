package com.luukitoo.processor.util

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.reflect.KClass

object SimpleBuilder {

    fun dataClass(
        className: String,
        superClass: ClassName? = null,
        params: List<Pair<String, TypeName>>,
        kdoc: CodeBlock? = null
    ) = TypeSpec.classBuilder(className).apply {
        superClass?.let(this::superclass)
        kdoc?.let(this::addKdoc)
        addModifiers(KModifier.DATA)
        params.forEach {
            addProperty(
                PropertySpec.builder(it.first, it.second)
                    .initializer(it.first)
                    .build()
            )
        }
        primaryConstructor(
            FunSpec.constructorBuilder().apply {
                params.forEach {
                    addParameter(name = it.first, type = it.second)
                }
            }.build()
        )
    }.build()
}
