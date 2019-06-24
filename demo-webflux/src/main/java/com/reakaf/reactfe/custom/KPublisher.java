package com.reakaf.reactfe.custom;

import com.reakaf.reactfe.Restaurant;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class KPublisher<T> implements Publisher<T> {
    public static KPublisher<Restaurant> just(Restaurant restaurant) {
        return new KPublisherJust(restaurant);
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        System.out.println("subscribe called");
    }

    public <T> KPublisher<T> log() {
        System.out.println("log called");
        return null;
    }


}


