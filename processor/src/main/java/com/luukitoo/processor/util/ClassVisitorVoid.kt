package com.luukitoo.processor.util

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

/**
 * Creates visitor of type [KSVisitorVoid]
 * which can be used for visiting annotated symbol as [KSClassDeclaration]
 * @param visitClass is called for visited [KSClassDeclaration]
 * */
fun classVisitorVoid(visitClass: (KSClassDeclaration) -> Unit): KSVisitorVoid {
    return object : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            visitClass.invoke(classDeclaration)
        }
    }
}
