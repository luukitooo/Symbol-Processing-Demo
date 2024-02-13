package com.luukitoo.processor.extension

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.reflect.KClass

/**
 * Checks if [KSAnnotation] is correct annotation.
 * @param annotation concrete annotation as [KClass].
 * @return true if [KSAnnotation] and [KClass] are from the same annotations, else false.
 * */
fun <T : Any> KSAnnotation.isAnnotation(annotation: KClass<T>): Boolean {
    return this.annotationType.toTypeName() == annotation.asTypeName()
}

/**
 * @param name annotation parameter's name.
 * @return value of annotation's parameter as [Any] if it was found, else null.
 * */
fun KSAnnotation.getParam(name: String): Any? {
    return arguments.find {
        it.name?.asString() == name
    }?.value
}

/**
 * @param name annotation parameter's name.
 * @return annotation's parameter as [Boolean] if it was found, else null.
 * */
fun KSAnnotation.getBoolean(name: String): Boolean? {
    return getParam(name).toString().toBooleanStrictOrNull()
}

/**
 * @param name annotation parameter's name.
 * @return annotation's parameter as [String] if it was found, else null.
 * */
fun KSAnnotation.getString(name: String): String? {
    return getParam(name)?.toString()
}

/**
 * @param name annotation parameter's name.
 * @return annotation's [KClass] type parameter as [ClassName] if it was found, else null.
 * */
fun KSAnnotation.getKClass(name: String): ClassName? {
    return (getParam(name) as? KSType)?.toClassName()
}

/**
 * @param name annotation parameter's name.
 * @return annotation's parameter as [Array] of [String] if it was found, else null.
 * */
fun KSAnnotation.getStringArray(name: String): Array<String>? {
    return (getParam(name) as? ArrayList<*>)
        ?.map(Any::toString)
        ?.toTypedArray()
}
