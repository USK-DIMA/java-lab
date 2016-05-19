package ru.uskov.dmitry.model;

import java.util.concurrent.Future;


public class RequestInfo {

    private String threadName;

    private Future<String> future;

    private boolean completed;


    public RequestInfo(String threadName, Future<String> future, boolean status) {
        this.threadName = threadName;
        this.future = future;
        this.completed = status;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Future<String> getFuture() {
        return future;
    }

    public void setFuture(Future<String> future) {
        this.future = future;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
