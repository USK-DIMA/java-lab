package ru.uskov.dmitry.model;

import java.util.concurrent.Future;


public class RequestInfo {

    private String threadName;

    private Future<String> future;

    private Status status;


    public RequestInfo(String threadName, Future<String> future) {
        this.threadName = threadName;
        this.future = future;
        this.status = Status.EXECUTE;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        EXECUTE, COMPLIT, RES_ERR
    }

}
