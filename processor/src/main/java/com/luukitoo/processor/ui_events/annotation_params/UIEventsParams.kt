package com.luukitoo.processor.ui_events.annotation_params

import com.google.devtools.ksp.symbol.KSAnnotation
import com.luukitoo.annotation.UIEvents
import com.luukitoo.processor.extension.getBoolean
import com.luukitoo.processor.extension.getStringArray

/**
 * Parameters of [UIEvents] annotation.
 * */
class UIEventsParams(
    val autoGeneration: Boolean = true,
    val emptyEvents: Array<String> = emptyArray()
) {
    companion object {
        fun createFrom(ksAnnotation: KSAnnotation?): UIEventsParams {
            if (ksAnnotation == null) return UIEventsParams()
            return UIEventsParams(
                autoGeneration = ksAnnotation.getBoolean("autoGeneration")!!,
                emptyEvents = ksAnnotation.getStringArray("emptyEvents")!!
            )
        }
    }
}
