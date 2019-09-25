package io.xxx.alouette.flow;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.Future;

public class DockerXDemoPublisher<T> implements Flow.Publisher<T>, AutoCloseable {

    private final ExecutorService executor; // daemon-based
    private CopyOnWriteArrayList<DockerXDemoSubscription<T>> list = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<DockerXDemoSubscriber<T>> list2 = new CopyOnWriteArrayList<>();

    public void submit(T item) {
        System.out.println("********* 开始发布元素 item: " + item + "*********");
        list.forEach(e -> e.future = executor.submit(() -> e.subscriber.onNext(item)));
//        list2.forEach(e -> e.onNext(item));
    }

    public DockerXDemoPublisher(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void close() {
        list.forEach(e -> e.future = executor.submit(e.subscriber::onComplete));
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        subscriber.onSubscribe(new DockerXDemoSubscription<>(subscriber, executor));
        list.add(new DockerXDemoSubscription<>(subscriber, executor));
//        list2.add((DockerXDemoSubscriber<T>) subscriber);
    }

    static class DockerXDemoSubscription<T> implements Flow.Subscription {

        private final Flow.Subscriber<? super T> subscriber;
        private final ExecutorService executor;
        private Future<?> future;
        private T item;
        private boolean completed;

        DockerXDemoSubscription(Flow.Subscriber<? super T> subscriber, ExecutorService executor) {
            this.subscriber = subscriber;
            this.executor = executor;
        }

        @Override
        public void request(long n) {
            if (n != 0 && !completed) {
                if (n < 0) {
                    IllegalArgumentException e = new IllegalArgumentException();
                    executor.execute(() -> subscriber.onError(e));
                } else {
                    future = executor.submit(() -> subscriber.onNext(item));
                }
            } else {
                subscriber.onComplete();
            }
        }

        @Override
        public void cancel() {
            completed = true;
            if (future != null && !future.isCancelled()) {
                future.cancel(true);
            }
        }
    }
}
