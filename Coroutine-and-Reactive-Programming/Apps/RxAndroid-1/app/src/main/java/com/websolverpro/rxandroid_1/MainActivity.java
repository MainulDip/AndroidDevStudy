package com.websolverpro.rxandroid_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
//        myObservable.subscribeOn(Schedulers.io());

        compositeDisposable = new CompositeDisposable();

        myObserver = new DisposableObserver<String>() {

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

//        compositeDisposable.add(myObserver);
//        myObservable.observeOn(AndroidSchedulers.mainThread());
//        myObservable.subscribe(myObserver);

        /**
         * Chaining in RxJava
         */
        compositeDisposable.add(
                myObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(myObserver)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}