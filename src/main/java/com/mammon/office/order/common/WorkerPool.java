package com.mammon.office.order.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WorkerPool {

    public static ThreadPoolExecutor worker;

    static {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 60L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(60));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        worker = executor;
    }

    public static void execute(Runnable runnable) {
        try {
            worker.execute(new WorkHandler(runnable));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public static void shutdown() {
        if (worker != null) {
            worker.shutdown();
            while (!worker.isTerminated()) {
                log.warn("waiting shutdown... ");
            }
        }
    }

    private static class WorkHandler implements Runnable {

        private Runnable self;

        public WorkHandler(Runnable runnable) {
            this.self = runnable;
        }

        @Override
        public void run() {
            try {
                this.self.run();
            } catch (Throwable cause) {
                log.warn("Unhandler Throwable", cause);
            }
        }
    }
}
