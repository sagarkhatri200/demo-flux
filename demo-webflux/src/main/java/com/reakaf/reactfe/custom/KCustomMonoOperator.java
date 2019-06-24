package com.reakaf.reactfe.custom;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;
import reactor.core.publisher.Operators;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.Nullable;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class KCustomMonoOperator<T> extends MonoOperator<T, T> {
    private Scheduler timedScheduler;
    private String seed;

    public KCustomMonoOperator(Mono<? extends T> source, String seed) {
        super(source);
        System.out.println("Thread " + Thread.currentThread().getId() + ", Constructing Custom Operator");
        this.seed = seed;
        timedScheduler = Schedulers.parallel();
    }

    @Override
    public void subscribe(CoreSubscriber<? super T> coreSubscriber) {
        System.out.println("Thread " + Thread.currentThread().getId() + ", Creating and Subscribing Custom Operator");
        this.source.subscribe(new KCustomMonoOperator.DelayElementSubscriber(coreSubscriber, this.timedScheduler, this.seed));
    }

    static final class DelayElementSubscriber<T> extends Operators.MonoSubscriber<T, T> {
        final long delay;
        final Scheduler scheduler;
        final TimeUnit unit;
        private final String seed;
        Subscription s;
        volatile Disposable task;
        boolean done;

        DelayElementSubscriber(CoreSubscriber<? super T> actual, Scheduler scheduler, String seed) {
            super(actual);
            this.scheduler = scheduler;
            this.delay = 1000;
            this.unit = TimeUnit.MILLISECONDS;
            this.seed = seed;
        }

        @Nullable
        public Object scanUnsafe(Attr key) {
            if (key == Attr.TERMINATED) {
                return this.done;
            } else if (key == Attr.PARENT) {
                return this.s;
            } else {
                return key == Attr.RUN_ON ? this.scheduler : super.scanUnsafe(key);
            }
        }

        public void cancel() {
            System.out.println("Thread " + Thread.currentThread().getId() + ", cancel ...");
            super.cancel();
            if (this.task != null) {
                this.task.dispose();
            }

            if (this.s != Operators.cancelledSubscription()) {
                this.s.cancel();
            }

        }

        public void onSubscribe(Subscription s) {
            System.out.println("Thread " + Thread.currentThread().getId() + ", subscribe ...");
            if (Operators.validate(this.s, s)) {
                this.s = s;
                this.actual.onSubscribe(this);
                s.request(9223372036854775807L);
            }

        }

        public void onNext(T t) {
            System.out.println("Thread " + Thread.currentThread().getId() + ", next ...");
            if (this.done) {
                Operators.onNextDropped(t, this.actual.currentContext());
            } else {
                this.done = true;

                try {
                    this.task = this.scheduler.schedule(() -> {
                        this.complete(t);
                    }, this.delay, this.unit);
                } catch (RejectedExecutionException var3) {
                    throw Operators.onRejectedExecution(var3, this, null, t, this.actual.currentContext());
                }
            }
        }

        public void onComplete() {
            System.out.println("Thread " + Thread.currentThread().getId() + ", complete ...");
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable t) {
            System.out.println("Thread " + Thread.currentThread().getId() + ", error ...");
            if (this.done) {
                Operators.onErrorDropped(t, this.actual.currentContext());
            } else {
                this.done = true;
                this.actual.onError(t);
            }
        }
    }
}
