package com.luukitoo.processor.base

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSBuiltIns
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.properties.Delegates

/**
 * Processor class with easy access to [SymbolProcessorEnvironment] and [KSBuiltIns]
 * with help of [EnvironmentUtils].
 * @property utils combines fields from [SymbolProcessorEnvironment] and [Resolver] for easy access.
 * */
abstract class KotlinSymbolProcessor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    protected var utils by Delegates.notNull<EnvironmentUtils>()
        private set

    override fun process(resolver: Resolver): List<KSAnnotated> {
        utils = EnvironmentUtils(
            resolver = resolver,
            logger = environment.logger,
            codeGenerator = environment.codeGenerator,
            versions = EnvironmentUtils.Versions(
                kotlinVersion = environment.kotlinVersion,
                apiVersion = environment.apiVersion,
                compilerVersion = environment.compilerVersion
            ),
            options = environment.options,
            types = resolver.builtIns
        )
        return handleProcessing()
    }

    abstract fun handleProcessing(): List<KSAnnotated>
}

data class EnvironmentUtils(
    val resolver: Resolver,
    val logger: KSPLogger,
    val codeGenerator: CodeGenerator,
    val versions: Versions,
    val options: Map<String, String>,
    val types: KSBuiltIns
) {

    data class Versions(
        val kotlinVersion: KotlinVersion,
        val apiVersion: KotlinVersion,
        val compilerVersion: KotlinVersion
    )
}