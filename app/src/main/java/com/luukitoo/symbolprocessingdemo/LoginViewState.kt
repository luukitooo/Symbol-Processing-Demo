package com.luukitoo.symbolprocessingdemo

import com.luukitoo.annotation.CustomEvent
import com.luukitoo.annotation.IgnoreEvent
import com.luukitoo.annotation.UIEvents

// you can create as many custom events as you need.
@CustomEvent(
    eventName = "MyCustomEvent",
    paramName = "myParameter",
    paramType = LoginViewState.MyCustomEventParams::class
)
// base annotation to start code generation.
@UIEvents(
    // this is for creating events without parameters. (data objects)
    emptyEvents = ["Login"]
)
data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val rememberUser: Boolean = false,
    // this property's update event won't be created.
    @IgnoreEvent val loggedIn: Boolean = false,
) {

    data class MyCustomEventParams(
        val firstParam: String,
        val secondParam: Int,
    )
}


/**
 * @Generated_Code_Example:
 *
 * public sealed class LoginViewStateEvents {
 *   public data class UpdateEmail(
 *     public val newValue: String,
 *   ) : LoginViewStateEvents()
 *
 *   public data class UpdatePassword(
 *     public val newValue: String,
 *   ) : LoginViewStateEvents()
 *
 *   public data class UpdateRepeatPassword(
 *     public val newValue: String,
 *   ) : LoginViewStateEvents()
 *
 *   public data class UpdateRememberUser(
 *     public val newValue: Boolean,
 *   ) : LoginViewStateEvents()
 *
 *   public data class MyCustomEvent(
 *     public val myParameter: LoginViewState.MyCustomEventParams,
 *   ) : LoginViewStateEvents()
 *
 *   public data object Login : LoginViewStateEvents()
 * }
 *
 * */