package com.gds.wiki.module;

import com.gds.wiki.lock.IRedisLock;
import com.gds.wiki.lock.Lock;
import com.gds.wiki.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by wiki on 2020/7/17 17:42
 */
@RestController
@RequestMapping("redis")
@Slf4j
public class TestController {
    @Autowired
    RedisLock redisLock;

    @GetMapping(value = "/lock")
    public String lock(){
          redisLock.LockCallBack("111", new IRedisLock() {
              @Override
              public void success(Lock lock) throws InterruptedException {
                  log.info("拿到锁了开始做业务");
                  Thread.sleep(100000);
                  log.info("业务结束");
              }

              @Override
              public void fail(Lock lock) {
                  log.info("没拿到锁了");
              }
          });

        return "";
    }

    @GetMapping(value = "/tqlock")
    public String tqlock(){
        redisLock.LockCallBack("111", new IRedisLock() {
            @Override
            public void success(Lock lock) throws InterruptedException {
                log.info("拿到锁了开始做业务");
                Thread.sleep(200);
                log.info("异常 提前释放 继续业务~~~");
                redisLock.unLock(lock);
                Thread.sleep(10000);
            }

            @Override
            public void fail(Lock lock) {
                log.info("没拿到锁了");
            }
        });

        return "";
    }

}
