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

`Observable` : Emits data | Instances of Observable Class. It needs to `Subscribe` to an Observer to be observed using `observable.subscribe(observer)` as final binding stage. 

`Observer` : Consume data emitted by the Observable | Implementation of Observer Interface. Some Override Methods are `onSubscribe`, `onNext`, `onComplete` and `onError`

`Schedulers` : Handle Multi Threading. For android `Schedulers.io()` and `AndroidSchedulers.mainThread()` are frequently used.

`Operators` : Help to make/convert data types (or data streams) as Observable (ie, `Observable.just()`) [Converts Data Streams Before Observers (to feed by Observers)]. Operators are chainable with one another. There are more than 70 operators available. Most Common Operators are `Map`, `FlatMap`, `SwitchMap`, `From`, `Just`, `Range`, `Debounce`, `Buffer` etc.

* Subscriptions: A connection between  Observable-Observer is called a Subscription. Observable-Observer Subscription can vary by sub classes,  like `Single-SingleObserver`, `Completable-CompletableObserver`, `Maybe-MaybeObserver`, `Flowable-Observer`

* Memory Leaks Handling Interfaces : `Disposable` and `CompositeDisposable`

### Minimal Implementation:
```java
/**
 * this will update the text view to the greeting text when the app launches
*/
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "RxAndroidCustom";
    private String greeting = "Hello from RxJava and RxAndroid";
    private Observable<String> myObservable;
    private Observer<String> myObserver;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvGreeting);

        myObservable = Observable.just(greeting);

        myObserver = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext: ");
                textView.setText(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };

        myObservable.subscribe(myObserver);
    }
}
```

### Concurrency and Multithreading with Schedulers:
A `Scheduler` can be identified as a thread pool managing one or more threads.

`Schedulers.io()` -> Used for non CPU intensive tasks like Database and filesystem iterations, network communication etc. Can have limitless thread pool.

`AndroidSchedulers.mainThread()` -> This is the main thread (UI Thread). Provided to RxJava from RxAndroid.

`Schedulers.newThread()` -> creates a new thread for each unit of work scheduled.

`Schedulers.single()` -> it spawns a single thread and execute tasks synchronously (one after another following the given order)

`Schedulers.trampoline()` -> executes tasks following first in first out basics. Used for recurring tasks

`Schedulers.from(Executor executor)` -> creates and returns a custom scheduler backed by a specific executor.

```kotlin
protected void onCreate(Bundle savedInstanceState) {
        //...
        myObservable = Observable.just(greeting);
        myObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());;
        //...
}
```
### Disposable and Disposable Observer:
When we don't need the Observable to be observed, we can dispose the subscription using `Disposable.dispose()`. A Disposable instance is available from the Observer's onSubscribe method. We will dispose the subscription on lifecycle destroyed on the `onDestroy` override.

Using Disposable Observer, the subscription will be destroyed automatically using `new DisposableObserver<T>()` and directly call myObserver.dispose() in the onDestroy override of the Activity.

```kotlin
public class MainActivity extends AppCompatActivity {
    //....
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //...

        myObserver = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }
            //...
        };

        myObservable.subscribe(myObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
```

### Composite Disposable :
When there is more than one observer, it's better to use Composite Disposable to dispose all the subscription using `CompositeDisposable.add(myDisposableObserver)` and `CompositeDisposable.clear()`
```
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "RxAndroidCustom";
    private String greeting = "Hello from RxJava and RxAndroid";
    private Observable<String> myObservable;
    private DisposableObserver<String> myObserver;

    private CompositeDisposable compositeDisposable;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvGreeting);

        myObservable = Observable.just(greeting);
        myObservable.subscribeOn(Schedulers.io());

        compositeDisposable = new CompositeDisposable();

        myObserver = new DisposableObserver<String>() {
            //...
        };

        compositeDisposable.add(myObserver);
        myObservable.subscribe(myObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
```

### Chaining in RxJava :
```kotlin
/**
* Chaining in RxJava
*/
compositeDisposable.add(
        myObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(myObserver)      
);
```
### Operators: