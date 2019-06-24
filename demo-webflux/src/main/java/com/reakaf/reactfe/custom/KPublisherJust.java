package com.reakaf.reactfe.custom;

import reactor.core.Fuseable;


public class KPublisherJust<T> extends KPublisher<T> implements Fuseable.ScalarCallable<T>, Fuseable {
    private T restaurant;

    public KPublisherJust(T restaurant) {
        super();
        this.restaurant = restaurant;
    }

    @Override
    public T call() throws Exception {
        return restaurant;
    }
}
