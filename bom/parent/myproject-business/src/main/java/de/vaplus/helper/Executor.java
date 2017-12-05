package de.vaplus.helper;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.LockType;

@Singleton
@Lock(LockType.READ)
public class Executor {

    @Asynchronous
    public <T> Future<T> submit(Callable<T> task) throws Exception {
        return new AsyncResult<T>(task.call());
    }

}