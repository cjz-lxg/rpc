package lxg.cjz.rpc.common.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class IdFactory {

    private static final AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static Long getId() {
        return REQUEST_ID_GEN.incrementAndGet();
    }

}
