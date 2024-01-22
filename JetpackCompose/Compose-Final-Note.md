### Compose Frequently Used Functions:
- `rememberCoroutineScope` -> it creates a coroutine scope bound to the composable. that scope can be used anywhere to launch coroutines `scope.launch {}`. The scope will be destroyed when the composable that called it is removed from tree.

- `rememberAsState`

- `LazyListState` -> A state object that can be hoisted to control and observe scrolling. In most cases, this will be created via rememberLazyListState. It's kind of a eventListener on scrolling event of the composable that call/initialize (using `rememberLazyListState()`) it.
```kotlin
// initializing LazyListState
val lazyListState = rememberLazyListState() // returns a LazyListState

// will return true/false when the composable will start and stop scrolling
Log.d("rememberLazyListState", "lazyListState.isScrollInProgress => ${lazyListState.isScrollInProgress}").

// Most of the time this is used to capture LazyList Position
// can be used to store scrolling position on Shared Preferences and 
// restore that exact position (list items top index) when will app launch next time
```

- `rememberLazyListState` -> Creates a LazyListState that is remembered across compositions (re-compositions). 

- `rememberScaffoldState`

### @Composable vs @UIComposable, @ComposableTarget, ect:
There can be exception scenario, but usually
- @Composable only -> Custom Composable
- @Composable + @UIComposable + other -> Composables coming from Libs

### Compose use Uni-Directional/One-Way Data Flow vs Bi-Directional 2 way data binding:


### inline, noinline, crossinline:
- `inline` Fn: will include the lambda into call site (without creating a Function Object) when compiled. Lambdas inside inline fn are allowed for `non-local return (return will be the return of enclosed/calling fn)`. But the inline fn can not access private members/methods of the enclosing class unless those members/methods are declared `internal` and annotated with @PublishedApi.

- inline fn with `noinline` param lambda: the lambda will not be inlined and cannot `return` to the calling function. 

- inline fn with `crossinline`: the lambda will be inlined in compiled call site but cannot return as non-local. 

https://medium.com/android-news/inline-noinline-crossinline-what-do-they-mean-b13f48e113c2 

### `in` and `out` with Generic Classes and Functions:
https://kotlinlang.org/docs/generics.html

* mnemonic: `PECS` stands for `Producer-Extends`, `Consumer-Super`.

`out T` is for returning the Type from a function. mimics javas wildcard `<? extends T`>, where any class that T extends from (`upper bound`). Here T is called `covariance` in kt

`in T` is for consuming the type from a function, also like `<? super T>` in java, where all the child classes of T are allowed here. Or classes where T is the base/super class of those. Here T is called `contra variance` in kt

```kotlin
class A {}
class B : A {}
class C : B {}

// out example
interface Produce<out T> {
    fun next(): T
    // T must be returned, not for consuming
}

fun outDemo(b: Produce<B>) {
    val objects: Produce<A> = b // OK, classes B extends form
    val objects: Produce<Any> = b // OK, as Any is the base/super class of everything in kt
    // but C is not allowed here, as B is upper bound here
}

// in example
interface Consume<in T> {
    fun next(T): Int 
    // T must be consumed, not for returning
}

fun inDemo(b: Consume<B>) {
    val y: Consume<C> = b // OK
    // but A is not allowed here, as classes who's super class is B. B is the lower limit.
    // val y: Consume<Any> = b // Not OK
}
```

Kotlin type system -> https://medium.com/@m.sandovalcalvo/kotlin-type-system-unveiling-the-mystery-50613f0db893