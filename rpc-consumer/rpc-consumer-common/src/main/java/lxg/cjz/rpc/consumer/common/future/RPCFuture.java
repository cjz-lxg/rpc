package lxg.cjz.rpc.consumer.common.future;

import lxg.cjz.rpc.common.threadpool.ClientThreadPool;
import lxg.cjz.rpc.consumer.common.callback.AsyncRPCCallback;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/20
 * @description
 */
public class RPCFuture extends CompletableFuture<Object> {
    private static final Logger logger = LoggerFactory.getLogger(RPCFuture.class);
    private Sync sync;
    private RpcProtocol<RpcRequest> requestRpcProtocol;
    private RpcProtocol<RpcResponse> responseRpcProtocol;
    private long startTime;
    private long responseTimeThreshold = 5000;
    private List<AsyncRPCCallback> pendingCallbacks = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    public RPCFuture addCallback(AsyncRPCCallback callback) {
        lock.lock();
        try {
            if (isDone()) {
                runCallBack(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    public void invokeCallbacks() {
        lock.lock();
        try {
            for (final AsyncRPCCallback pendingCallback : pendingCallbacks) {
                runCallBack(pendingCallback);
            }
        }finally {
            lock.unlock();
        }
    }

    private void runCallBack(final AsyncRPCCallback callback) {
        final RpcResponse response = this.responseRpcProtocol.getBody();
        ClientThreadPool.submit(()->{
            if (!response.isException()) {
                callback.onSuccess(response.getResult());
            } else {
                callback.onException(new RuntimeException("Response error", new Throwable(response.getException())));
            }
        });
    }

    public RPCFuture(RpcProtocol<RpcRequest> requestRpcProtocol) {
        sync = new Sync();
        this.requestRpcProtocol = requestRpcProtocol;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (this.responseRpcProtocol != null) {
            return this.responseRpcProtocol.getBody().getResult();
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (responseRpcProtocol != null) {
                return responseRpcProtocol.getBody().getResult();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: " + this.requestRpcProtocol.getHeader().getRequestId()
                    + ". Request class name: " + this.requestRpcProtocol.getBody().getClassName()
                    + ". Request method: " + this.requestRpcProtocol.getBody().getMethodName());
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    public void done(RpcProtocol<RpcResponse> responseRpcProtocol) {
        this.responseRpcProtocol = responseRpcProtocol;
        sync.release(1);
        invokeCallbacks();
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            logger.warn("Service response time is too slow. Request id = " + responseRpcProtocol.getHeader().getRequestId() + ". Response Time = " + responseTime + "ms");
        }
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;

        //future status
        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int acquires) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int release) {
            if (getState() == pending) {
                return compareAndSetState(pending, done);
            }
            return false;
        }

        public boolean isDone() {
            return getState() == done;
        }
    }

}
