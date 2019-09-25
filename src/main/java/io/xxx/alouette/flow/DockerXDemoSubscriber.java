package io.xxx.alouette.flow;

import lombok.Getter;

import java.util.concurrent.Flow;

public class DockerXDemoSubscriber<T> implements Flow.Subscriber<T> {

    @Getter
    private String name;
    @Getter
    private Flow.Subscription subscription;
    private final long bufferSize;
    long count;

    public DockerXDemoSubscriber(long bufferSize, String name) {
        this.bufferSize = bufferSize;
        this.name = name;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        // count = bufferSize - bufferSize / 2;
        // 在消费一半的时候重新请求
        (this.subscription = subscription).request(bufferSize);
        System.out.println("开始onSubscribe订阅");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(T item) {
        // if (--count <= 0) subscription.request(count = bufferSize - bufferSize / 2);
        System.out.println(" ###### " + Thread.currentThread().getName() + " name: " + name + " item: " + item);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Completed");
    }
}
