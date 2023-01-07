## Overview:

Cupcake app
=================================

This app contains an order flow for cupcakes with options for quantity, flavor, and pickup date.
The order details get displayed on an order summary screen and can be shared to another app to
send the order.

This app demonstrates multiple fragments in an activity, a shared ViewModel across fragments,
data binding, LiveData, and the Jetpack Navigation component.

### Topics:
Activities, fragments, intents, data binding, navigation components, and the basics of architecture components.

### Using AppCompatActivity Constructor Inheritance in MainActivity:
MainActivity can inherit from parameterized constructor of the AppCompatActivity

* This code uses a parameterized constructor AppCompatActivity(@LayoutRes int contentLayoutId) 
* which takes in a layout that will be inflated as part of super.onCreate(savedInstanceState)
```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main)
/**
 * Same as below.
    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        }
    }
 * This code uses a parameterized constructor AppCompatActivity(@LayoutRes int contentLayoutId) 
 * which takes in a layout that will be inflated as part of super.onCreate(savedInstanceState)
 */
```