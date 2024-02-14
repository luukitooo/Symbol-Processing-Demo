## About Project
I created an annotation which generates UI Events in compile time, you just need to define a view state, annotate it with @UIEvents and rebuild the project.
Annotations are processing with KSP (Kotlin Symbol Processing) and code is generated with KotlinPoet.

P.S. This project was made for educational purposes.

<br>

## Example
Here you can see an example of view state:
```kotlin
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
```

<br>

And here is a generated events for our view state:
```kotlin
/**
 * Events class for [LoginViewState]
 */
public sealed class LoginViewStateEvents {
  public data class UpdateEmail(
    public val newValue: String,
  ) : LoginViewStateEvents()

  public data class UpdatePassword(
    public val newValue: String,
  ) : LoginViewStateEvents()

  public data class UpdateRepeatPassword(
    public val newValue: String,
  ) : LoginViewStateEvents()

  public data class UpdateRememberUser(
    public val newValue: Boolean,
  ) : LoginViewStateEvents()

  public data class MyCustomEvent(
    public val myParameter: LoginViewState.MyCustomEventParams,
  ) : LoginViewStateEvents()

  public data object Login : LoginViewStateEvents()
}
```

<br>

## Usage
You can use generated events to send them from your screens and then handle inside ViewModels to update a view state and user interface. You can have onEvent function in your ViewModel like this:
```kotlin
fun onEvent(event: LoginViewStateEvents) {
        when (event) {
            is LoginViewStateEvents.UpdateEmail -> updateEmail(event.newValue)
            is LoginViewStateEvents.UpdatePassword -> updatePassword(event.newValue)
            is LoginViewStateEvents.UpdateRepeatPassword -> updateRepeatPassword(event.newValue)
            is LoginViewStateEvents.UpdateRememberUser -> updateRememberUser(event.newValue)
            is LoginViewStateEvents.Login -> login()
            is LoginViewStateEvents.MyCustomEvent -> doSomeStuff(event.myParameter)
        }
    }
```

<br>

And send events from your screens this way:
```kotlin
@Composable
fun LoginScreen(
    viewState: LoginViewState,
    onEvent: (LoginViewStateEvents) -> Unit,
) {
  // ...
}
```
