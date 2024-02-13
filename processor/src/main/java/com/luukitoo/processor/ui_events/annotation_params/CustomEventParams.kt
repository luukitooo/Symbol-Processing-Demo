package com.luukitoo.processor.ui_events.annotation_params

import com.google.devtools.ksp.symbol.KSAnnotation
import com.luukitoo.annotation.CustomEvent
import com.luukitoo.processor.extension.getKClass
import com.luukitoo.processor.extension.getString
import com.squareup.kotlinpoet.ClassName

/**
 * Parameters of [CustomEvent] annotation.
 * */
class CustomEventParams(
    val eventName: String,
    val paramName: String,
    val paramType: ClassName
) {
    companion object {
        fun createFrom(ksAnnotation: KSAnnotation?): CustomEventParams? {
            if (ksAnnotation == null) return null
            return CustomEventParams(
                eventName = ksAnnotation.getString("eventName")!!,
                paramName = ksAnnotation.getString("paramName")!!,
                paramType = ksAnnotation.getKClass("paramType")!!
            )
        }
    }
}