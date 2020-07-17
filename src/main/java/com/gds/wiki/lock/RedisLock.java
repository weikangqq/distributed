package com.gds.wiki.lock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * Created by wiki on 2020/7/17 16:55
    redis 锁实现
 */
@Slf4j
@Component
@Data
public class RedisLock {
    /**
     *RedisTemplate ** 对象名 必须为redisTemplate 不然报错
     */
    @Autowired
     private RedisTemplate redisTemplate;

    /**
     * 没抢到锁及时返回 fail
     * @param key
     * @param iRedisLock
     * @return
     */
       public boolean LockCallBack(String key,IRedisLock iRedisLock){
           Lock lock=new Lock();
           lock.setKey(key);
           lock.setIfLock(false);
         try {
             if(lock(lock).getIfLock()){
                 log.info("["+key+"]抢锁成功~");
                 iRedisLock.success(lock);
                 return true;
             }else {
                 log.info("["+key+"]抢锁失败~");
                 iRedisLock.fail(lock);
             };
         } catch (InterruptedException e) {
             e.printStackTrace();
         } finally {
             unLock(lock);
         }
           return false;
       }



    /**
     * 加锁~
     * @param lock
     * @return
     */
    public  Lock  lock(Lock lock){
           /**
            * setIfAbsent redis 2.7 处理 setnx 个expired 方法合并  处理死锁
            *
            */
           if  (redisTemplate.opsForValue().setIfAbsent(lock.getKey(),System.currentTimeMillis(), 10l, TimeUnit.SECONDS)){
               lock.setIfLock(true);
           };
          return lock;
        }

    /**
     * 释放锁~
     * @param lock
     * @return
     */
    public   Lock unLock(Lock lock){
           if (lock.getIfLock()==true&&redisTemplate.delete(lock.getKey())){
               lock.setIfLock(false);
           };
            return lock;
        }
}
