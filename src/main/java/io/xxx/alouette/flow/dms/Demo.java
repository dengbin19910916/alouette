package io.xxx.alouette.flow.dms;

import io.xxx.alouette.flow.DockerXDemoPublisher;
import io.xxx.alouette.flow.DockerXDemoSubscriber;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Demo {

    public static void main(String[] args) {
//        ExecutorService executorService = ForkJoinPool.commonPool();
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try (DockerXDemoPublisher<Integer> publisher = new DockerXDemoPublisher<>(executorService)) {
            demoSubscribe(publisher, "One");
            demoSubscribe(publisher, "Two");
            demoSubscribe(publisher, "Three");
            IntStream.range(1, 5).forEach(publisher::submit);
        } finally {
            try {
                executorService.shutdown();
                int shutdownDelaySec = 1;
                System.out.println(".........等待 " + shutdownDelaySec + " 秒后结束服务.........");
                executorService.awaitTermination(shutdownDelaySec, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println("捕获到 executorService.awaitTermination()方法的异常: " + e.getClass().getName());
            } finally {
                System.out.println("调用 executorService.shutdownNow()结束服务...");
                List<Runnable> l = executorService.shutdownNow();
                System.out.println("还剩 " + l.size() + " 个任务等待执行，服务已关闭 ");
            }
        }
    }

    private static void demoSubscribe(DockerXDemoPublisher<Integer> publisher, String subscriberName) {
        DockerXDemoSubscriber<Integer> subscriber = new DockerXDemoSubscriber<>(4L, subscriberName);
        publisher.subscribe(subscriber);
    }
}
