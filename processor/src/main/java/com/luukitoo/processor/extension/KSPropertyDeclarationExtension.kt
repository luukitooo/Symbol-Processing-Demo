package com.luukitoo.processor.extension

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

fun KSPropertyDeclaration.asUpdateEventName(): String {
    return "Update".plus(
        simpleName.asString().replaceFirstChar {
            it.uppercaseChar()
        }
    )
}
