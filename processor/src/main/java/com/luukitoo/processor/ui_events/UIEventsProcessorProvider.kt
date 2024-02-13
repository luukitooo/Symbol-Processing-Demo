package com.luukitoo.processor.ui_events

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Provides symbol processor and runs it in compile time
 * with help of [AutoService] annotation.
 * */
@AutoService(SymbolProcessorProvider::class)
class UIEventsProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return UIEventsProcessor(environment)
    }
}