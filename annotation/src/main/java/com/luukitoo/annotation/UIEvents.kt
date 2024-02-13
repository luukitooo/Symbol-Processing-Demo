package com.luukitoo.annotation

import kotlin.reflect.KClass

/**
 * @param autoGeneration enables or disables update events auto generation.
 * @param emptyEvents creates events without parameters (data object).
 * */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class UIEvents(
    val autoGeneration: Boolean = true,
    val emptyEvents: Array<String> = []
)

/**
 * Ignores the generation of update event for property,
 * even if auto generation is enabled.
 * Not working without [UIEvents] annotation.
 * */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class IgnoreEvent

/**
 * Creates the custom event with name and parameter.
 * Not working without [UIEvents] annotation.
 * @param eventName defines the name of the custom event.
 * @param paramName defines the name of the custom event's parameter.
 * @param paramType defines the type of the custom event's parameter.
 * */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class CustomEvent(
    val eventName: String,
    val paramName: String,
    val paramType: KClass<*>
)