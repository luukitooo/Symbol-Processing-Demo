package com.luukitoo.processor.extension

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.reflect.KClass

/**
 * Loops through all symbols annotated with defined annotation.
 * */
fun <T: Any> Resolver.forEachSymbolAnnotatedWith(
    annotation: KClass<T>,
    block: (KSAnnotated) -> Unit
) {
    getSymbolsWithAnnotation(
        annotation.qualifiedName.orEmpty()
    ).forEach(block)
}
