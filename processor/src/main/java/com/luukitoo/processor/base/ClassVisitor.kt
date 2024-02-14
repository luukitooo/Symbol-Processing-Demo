package com.luukitoo.processor.base

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSEmptyVisitor

/**
 * Creates [KSEmptyVisitor] for class declarations.
 * @throws Exception if [KSNode] is not a [KSClassDeclaration]
 * */
abstract class ClassVisitor<Param, Result> : KSEmptyVisitor<Param, Result>() {

    override fun defaultHandler(node: KSNode, data: Param): Result {
        return visitClass(node as KSClassDeclaration, data)
    }

    abstract fun visitClass(ksClass: KSClassDeclaration, param: Param): Result

    companion object {

        /**
         * Lambda function which creates [ClassVisitor] anonymous class under the hood and returns it.
         * Can be used if you need to create visitor easily without creating separate class.
         * */
        fun <Result> create(
            visitor: (KSClassDeclaration) -> Result
        ): ClassVisitor<Unit, Result> {
            return object : ClassVisitor<Unit, Result>() {
                override fun visitClass(ksClass: KSClassDeclaration, param: Unit): Result {
                    return visitor.invoke(ksClass)
                }
            }
        }
    }
}