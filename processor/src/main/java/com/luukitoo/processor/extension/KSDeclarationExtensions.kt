package com.luukitoo.processor.extension

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import kotlin.reflect.KClass

/**
 * @return annotation if current class is it's target, else null.
 * */
fun <T : Any> KSDeclaration.getAnnotationOrNull(annotation: KClass<T>): KSAnnotation? {
    return annotations.find {
        it.isAnnotation(annotation)
    }
}
