package lxg.cjz.rpc.common.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/27
 * @description
 */
public class ClientThreadPool {
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor=new ThreadPoolExecutor(16,16,600L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(65536));
    }

    public static void submit(Runnable task){
        threadPoolExecutor.submit(task);
    }

    public static void shutdown(){
        threadPoolExecutor.shutdown();
    }

}
