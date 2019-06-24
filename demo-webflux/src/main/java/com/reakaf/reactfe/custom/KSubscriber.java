package com.reakaf.reactfe.custom;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class KSubscriber<T> implements Subscriber<T> {

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("onSubscribe Called");
    }

    @Override
    public void onNext(T t) {
        System.out.println("onNext Called");
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("OnError Called");
    }

    @Override
    public void onComplete() {
        System.out.println("OnComplete Called");
    }
}
