package com.reakaf.reactfe.custom;

import org.reactivestreams.Subscription;

public class KSubscription implements Subscription {
    @Override
    public void request(long l) {
        System.out.println("request called");
    }

    @Override
    public void cancel() {
        System.out.println("cancel called");
    }
}
