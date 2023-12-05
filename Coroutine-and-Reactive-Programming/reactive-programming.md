### What is Reactive Programming and ReactiveX:
In Reactive Programming, the consumer code blocks react/change to the data as it comes/updates 

ReactiveX (Reactive Extensions) is a project, which provides implementations for this reactive programming concept for different programming languages.

ReactiveX uses ideas form Observer-pattern, Iterator-pattern and functional programming concepts.


* ReactiveX : An API for asynchronous programming with observable streams.

* RxJava and RxAndroid : RxAndroid is an Android Friendly Layer on top of the RxJava main layer.

RxAndroid : https://github.com/ReactiveX/RxAndroid

RxJava : https://github.com/ReactiveX/RxJava

ReactiveX : https://reactivex.io/

### RxJava Overview:
There are 4 main concepts, 

`Observable` : Emits data | Instances of Observable Class. Some Override Methods are `onSubscribe`, `onNext`, `onComplete` and `onError`

`Observer` : Consume data emitted by the Observable | Implementation of Observer Interface. It needs to `Subscribe` to an Observable.  

`Schedulers` : Handle Multi Threading. For android `Schedulers.io()` and `AndroidSchedulers.mainThread()` are frequently used.

`Operators` : Converts Data Streams Before Observers (to feed by Observers). Operators are chainable with one another. There are more than 70 operators available. Most Common Operators are `Map`, `FlatMap`, `SwitchMap`, `From`, `Just`, `Range`, `Debounce`, `Buffer` etc.

* Subscriptions: A connection between  Observable-Observer is called a Subscription. Observable-Observer Subscription can vary by sub classes,  like `Single-SingleObserver`, `Completable-CompletableObserver`, `Maybe-MaybeObserver`, `Flowable-Observer`

* Memory Leaks Handling Interfaces : `Disposable` and `CompositeDisposable`