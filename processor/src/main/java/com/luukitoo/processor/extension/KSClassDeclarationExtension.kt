package com.luukitoo.processor.extension

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Modifier

/**
 * @param block is a lambda function, which is called if [KSClassDeclaration] is not a data class.
 * */
fun KSClassDeclaration.ifNotDataClass(block: (KSClassDeclaration) -> Unit) {
    if (modifiers.none { it == Modifier.DATA }) {
        block.invoke(this)
    }
}

/**
 * @return properties of a class, excluding extension and mutable ones.
 * */
fun KSClassDeclaration.getStateProperties(): List<KSPropertyDeclaration> {
    return getAllProperties()
        .filter { it.extensionReceiver == null }
        .filter { !it.isMutable }
        .toList()
}
