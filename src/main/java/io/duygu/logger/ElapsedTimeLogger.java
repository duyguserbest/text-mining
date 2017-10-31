package io.duygu.logger;

import io.duygu.preprocess.OperationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by dserbest on 31.10.2017.
 */
public class ElapsedTimeLogger {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String TOOK = " took: ";

    public static final String SECONDS = " seconds.";

    public static void log(OperationType type, Runnable runnable) {
        logRunnable(type.getLogPrefix(), runnable);
    }

    public static void log(OperationType type, Runnable runnable, Object... args) {
        logRunnable(String.format(type.getLogPrefix(), args), runnable);
    }

    public static <T> T log(OperationType type, Callable<T> callable) {
        return logCallable(type.getLogPrefix(), callable);
    }

    public static <T> T log(OperationType type, Callable<T> callable, Object... args) {
        return logCallable(String.format(type.getLogPrefix(), args), callable);
    }

    private static void logRunnable(String logPrefix, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long completionTime = (System.currentTimeMillis() - start) / 1000;
        LOGGER.info(logPrefix + TOOK + completionTime + SECONDS);
    }

    private static <T> T logCallable(String logPrefix, Callable<T> callable) {
        long start = System.currentTimeMillis();
        T call = null;
        try {
            call = callable.call();
        } catch (Exception e) {
            LOGGER.error(e);
            System.exit(1);
        }
        long completionTime = (System.currentTimeMillis() - start) / 1000;
        LOGGER.info(logPrefix + TOOK + completionTime + SECONDS);
        return call;
    }

}
