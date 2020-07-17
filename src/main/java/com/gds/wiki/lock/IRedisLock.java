package com.gds.wiki.lock;

/**
 * Created by wiki on 2020/7/17 16:49
 *
 * 用于业务类中抢锁时构造成功或者失败的接口
 *
 * 参数Lock 提供给业务中异常时 获取锁 并提前释放
 *
 */

public interface IRedisLock {

    public void success (Lock lock) throws InterruptedException;


    public void fail(Lock lock);
}
