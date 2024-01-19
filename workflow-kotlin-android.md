## Birds Eye view (Overviews):
Building Android/Spring (Java/Kotlin) Apps from scratch should have some standardized workflow to build it quickly, more systemically, also maintain and running Tests. Here are some personalized workflow docs and why cases.


### Why Interface and Abstract Classes? How those help:
 If the values of properties and implementations of functions are not known, make the class abstract. For example, Vegetables have many properties common to all vegetables, but you can't create an instance of a non-specific vegetable, because you don't know, for example, its shape or color. So Vegetable is an abstract class that leaves it up to the subclasses to determine specific details about each vegetable.

Interfaces don’t have a constructor, one class can inherit multiple interfaces. Where only one abstract class can be inherited.

Abstract classes can’t be final because the main reason they exist is for other classes to extend them. However, their methods and properties can be final. In fact, they’re final by default.

### When to use an abstract class (For closely related classes, abstract Animal is closely related to Dog/Cat class)
- An abstract class is a good choice if we are using the inheritance concept since it provides a common base class implementation to derived classes.

- An abstract class is also good if we want to declare non-public members. In an interface, all methods must be public.

- If we want to add new methods in the future, then an abstract class is a better choice. Because if we add new methods to an interface, then all of the classes that already implemented that interface will have to be changed to implement the new methods.

- If we want to create multiple versions of our component, create an abstract class. Abstract classes provide a simple and easy way to version our components. By updating the base class, all inheriting classes are automatically updated with the change. Interfaces, on the other hand, cannot be changed once created. If a new version of an interface is required, we must create a whole new interface.

- Abstract classes have the advantage of allowing better forward compatibility. Once clients use an interface, we cannot change it; if they use an abstract class, we can still add behavior without breaking the existing code.

- If we want to provide common, implemented functionality among all implementations of our component, use an abstract class. Abstract classes allow us to partially implement our class, whereas interfaces contain no implementation for any members.


### When to use an interface (common functionality for unrelated classes):
- If the functionality we are creating will be useful across a wide range of disparate objects, use an interface. Abstract classes should be used primarily for objects that are closely related, whereas interfaces are best suited for providing a common functionality to unrelated classes

- Interfaces are a good choice when we think that the API will not change for a while.

- Interfaces are also good when we want to have something similar to multiple inheritances since we can implement multiple interfaces.

- If we are designing small, concise bits of functionality, use interfaces. If we are designing large functional units, use an abstract class.

### Map For Kotlin Interface, Abstract Class and JUnit Testing With Spring Boot:
Task: make the full map of the kotlin springboot repository (That is inside of the KotlinEveryday Spring Repository)


### Elvis Operator | (?:) :
_price.value = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
The elvis operator (`?:`) means that if the expression on the left is not null, then use it. Otherwise if the expression on the left is null, then use the expression to the right of the elvis operator (which is "0" in this case)

### Functional Interface || Lambda Interface || SAM:
It's like Java's Single Abstract Method Class. With kotlin's syntactic sugar, we can implement the abstract method just like a lambda fn.
```kotlin
/**
* Here the 3rd param is required to implement a functional interface or SAM named TabLayoutMediator.TabConfigurationStrategy
* the interface has only one abstract method `onConfigureTab(TabLayout.Tab tab, int position)` which we need to implement.
* using a lambda we can implement this
*/
TabLayoutMediator(tabLayout, viewPager) { tab, position ->
    tab.setIcon(getTabIcon(position))
    tab.text = getTabTitle(position)
}.attach()
```

### Window Manager and Presentation and Display + DisplayManager:
WindowManager - https://stackoverflow.com/questions/19846541/what-is-windowmanager-in-android


DisplayManager - https://stackoverflow.com/questions/64223814/how-to-use-displaymanager-to-get-displays-and-set-a-bool


### WindowCompact and Window
Helper for accessing features in Window. Like `WindowCompat.setDecorFitsSystemWindows(window, false)` to display window edge-to-edge

all the window related features are here https://developer.android.com/reference/android/view/Window.html

### map[key]?.invoke() and Pair with Fn as Value:
Kotlin's Map.get(Key?) is nullable, (observe the code below) to implement the `?` check, we need to explicitly call `invoke()` operator fn to call the `value` fn. let and it() pattern can also be used

```kotlin
const val MY_GARDEN_PAGE_INDEX = 0
const val PLANT_LIST_PAGE_INDEX = 1

private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        MY_GARDEN_PAGE_INDEX to { GardenFragment() },
        PLANT_LIST_PAGE_INDEX to { PlantListFragment() }
    )

override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
```

### Working with adb:
`adb --help` for all possible actions.
Docs https://developer.android.com/tools/adb

`adb shell [-e ESCAPE] [-n] [-Tt] [-x] [COMMAND...]` for interacting with the connected device. 

Call Activity Manager with `adb shell am <command>`

call Package Manager with `adb shell pm <command>` like `list packages -3` to show all third party packages, `list users`


To get the screen resolution using WindowManager -  `adb shell wm size`

To get the screen the density - `adb shell wm density`

override the density by adding the new density - `db shell wm density 160`




### Note on Terms:
1. Artifact: The compiled or source-compiled java file in the form of JRE, etc. (https://stackoverflow.com/questions/2487485/what-is-a-maven-artifact)
2. ClassPath: These are project dependency (in the form of Artifact/JRE) that can be linked inside gradle dependency. Also by Manually creating a directory `libs` that contains `jre` dependencies and can be connect using gradle dependency.
```kotlin
dependencies {
    // ...
    compile fileTree(dir:'libs', include: ['*.jar'])
    // ...
}
```
https://stackoverflow.com/questions/26521836/how-to-add-classpath-in-an-android-studio-project

3. Reflection (Kotlin Reflect): It helps learning the name or the type of a property or function at `runtime` when used with Reactive Programming (`Async`)


### Argument passing using Lambdas
```kotlin
fun main() {
    A { str ->
        println("The passed arg is $str")
    }
    
    // will print "The passed arg is ArgB"
    
    
    /* argument as `it` when there is only one argument */
    A {
        println(it) // will print "ArgB"
    }
}

fun A(funA: (String) -> Unit) {
    // B { arg -> funA(arg)}
    B (funB = funA)
}

fun B(funB: (String) -> Unit) {
    funB("ArgB")
}
```

### Function as return type (Function returning Function):
```kotlin
fun main() {
  // free delivery of order above 499
  val productPrice1 = 600; 
  // not eligible for free delivery
  val productPrice2 = 300; 
  val totalCost1 = totalCost(productPrice1)
  val totalCost2 = totalCost(productPrice2)
   
  println("Total cost for item 1 is ${totalCost1 (productPrice1) }")
  println ("Total cost for item 2 is ${totalCost2 (productPrice2) }")
}
 
fun totalCost(productCost: Int) : (Int) -> Int {
  if (productCost > 499) {
    return { x -> x }
  }
  else {
    return { x -> x + 50 }
  }
}

/** outputs
 * Total cost for item 1 is 600
 * Total cost for item 2 is 350
*/
```

### fn(*arr) | Variable number of arguments (varargs) injecting:
If you already have an array and want to pass its contents to the function that accepts varags param, use the spread operator (prefix the array with *)
```kotlin
val a = arrayOf(1, 2, 3)
val list = asList(-1, 0, *a, 4)

fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts) // ts is an Array
        result.add(t)
    return result
}
```

Docs: https://kotlinlang.org/docs/functions.html#variable-number-of-arguments-varargs

### Closure with different style like JS (not common, not best practice):
```kotlin
fun main() {
	val result = someFun("Hello")
    println(result({v -> v}, ":-)")) 
    // Hello :-)

    // or
    val result2 = someFun("World")({it}, ":-)")
    println(result2)
    // World :-)
}

fun someFun(a: String) = { b: (a: String) -> String, c: String ->
    val d = b(a)+ " " + c
    d
}

// Note "function types, like anonymous functions (i.e. lambda expressions and fun blocks without name) 
// are not allowed to specify default values for their parameters"
// like c: String = "Default Value" here is not allowed
```
### Enum constant's ordinal and Enum Entries:
`ordinal` in enum is the member's position or index
and `entries` are like all the members of the specified enum like `enum.entries`
```kotlin
fun main() {
    println("${IntArithmetics.PLUS.ordinal}, ${IntArithmetics.TIMES.ordinal}") // 0, 1
    println(IntArithmetics.entries) // [PLUS, TIMES]

    val a = 13
    val b = 31

    println(IntArithmetics.PLUS.sth(a,b)) // 44
    println(IntArithmetics.TIMES.sth(a,b)) // 403

    for (f in IntArithmetics.entries) {
        println("$f($a, $b) = ${f.sth(a,b)}")
    }
    // PLUS(13, 31) = 44
    // TIMES(13, 31) = 403
}

/**
 * Enum ordinal is the Enum's constant's position on the Enum
 * As any enum constants are instances of the surrounding Enum class
 * to declare a method and call it by enum.constant.method() we need to make it abstract
 * then the method needs to be overridden
 */

enum class IntArithmetics  {
    PLUS { // PLUS is an anonymous enum Constant class (or anonymous class constant)
        override fun sth(t: Int, u: Int): Int = t + u
    },
    TIMES { // Same as PLUS, its an instances of the surrounding Enum class
        override fun sth(t: Int, u: Int): Int = t * u
    };

    abstract fun sth(t: Int, u: Int): Int
}
```
### Enum's member function:
```kotlin
enum class IntArithmetics {
    PLUS, TIMES;
    fun sth(t: Int, u: Int): Int {
        return when (this) {
            PLUS -> t + u
            TIMES -> t * u
        }
    }
}


fun main() {
    val a = 6
    val b = 7
    println(IntArithmetics.PLUS.sth(a,b)) // 13
    println(IntArithmetics.TIMES.sth(a,b)) // 42
}
```


### `this then` and appending custom Modifiers (infix notation):
All modifiers (built-in or customs) are chainable and the order of chaining matters. customModifiers.otherModifiers (`prepending[beginning]`) and otherModifier.then(reusableModifier) `appending [end]` pattern are used

To create a custom modifier utilizing `Intrinsic` measurement, `this then object : LayoutModifier {}` pattern is common. its like `this(then(object : LayoutModifier {}))`

```kotlin
// creating a custom chainable modifier by utilizing intrinsic measurement by implementing LayoutModifier interface
// infix notation is used instead of this(then(object: LayoutModifier {}))
fun Modifier.myCustomModifier(/* ... */) = this then object : LayoutModifier {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        // Measure and layout here
        // ...
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        // Logic here
        // ...
    }

    // Other intrinsics related methods have a default value,
    // you can override only the methods that you need.
}
```

more on Intrinsic implementation: https://developer.android.com/jetpack/compose/layouts/intrinsic-measurements#intrinsics-in-action


### Note on `invoke`:
in kotlin invoke mimics the `()` after function/object/class 's `instance`. using it, we can call a functions's instance like a function, ex `val f = fun (){....}`, here f is an instance of the assigned function. Its same goes with other type (object/class) instances.

inside class/object definition, we declare `operator fun invoke(...){....}` to run the function using `instance(...)` signature.

`invoke` is often very useful when calling a Nullable instance. `let` is another alternative for nullable?.invoke(....) call

```kotlin
fun greetingFun(name: String) = "Hello, $name from greetingFun"
private var sth: ((v: String) -> Unit)? = null // Defining a nullable functional instance

fun main() {
    val greeting = { name: String -> "Hello, $name!" }
    println(greeting.invoke("World"))
    println(greeting("World!"))

    // println((::greeting)("World!")) // error, References to variables and parameters are unsupported
    // println((::greeting).invoke("World!")) // error, same

    println((::greetingFun)("World!")) // ok, as `greetingFun` is not a variable/var/val, its a function    
    // println(greetingFun.invoke("World!")) // error, will not work, 'greetingFun(...)' expected    
    println(::greetingFun.invoke("World!!")) // ok

    sth = { i -> println("Calling Nullable: $i") } // assigning a function to the nullable instance
    sth?.invoke("using invoke")
    sth?.let { it("Using let") }
    // if (sth != null) sth("Using Null Check") // will not work, ide will suggest for sth?.invoke(....)

    sth = fun (s: String) = println("\nNeat way to call a $s")
    sth?.invoke("Nullable")

    sth?.also { it.invoke("Functional Nullable Property") }
    sth?.let{ it(("Something Else")) }
    
    // with if check, making a local copy will work without invoke, but not practical
    if (sth != null) {
        val localCopySth = sth
        if (localCopySth != null) {
            localCopySth("Using Null Check")
        }
    }
}
```