package com.zga.dazhong.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

public class RedisLockServiceImpl implements RedisLockService {
    private static final Logger LOGGER = LogManager.getLogger(RedisLockServiceImpl.class);

    private final RedisTemplate redisTemplate;

    public RedisLockServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 默认锁10分钟
     */
    private long defaultLockTimeMillis = 10 * 60 * 1000;
    /**
     * 默认等待间隔时间1s
     */
    private long defaultTryLockIntervalMillis = 1000;

    /**
     * 默认锁10分钟
     *
     * @param key
     * @return
     */
    @Override
    public boolean lock(String key) {
        return lock(key, defaultLockTimeMillis);
    }

    /**
     * 根据key加锁
     * 1. 尝试加锁，并设置value为当前时间+失效时间，如果存在，则加锁失败，不存在则加锁成功
     * 2. key已经存在，获取当前的锁失效时间，如果返回nil，则表明锁被删除，返回失败，如果非空，则继续
     * 3. 如果锁未过去，则返回获取锁失败，如果超时，则继续
     * 4. 设置key的value为当前时间+失效时间
     * 5. 如果返回nil，则表明锁之前被清理，获取锁成功；如果返回超时时间与前面获取锁的超时时间相同，则获取锁成功；如果不相同，则锁被其他实例获取，返回失败。
     *
     * @param key
     * @param lockMillis
     * @return
     */
    @Override
    public boolean lock(String key, long lockMillis) {
        if (redisTemplate.opsForValue().setIfAbsent(key, System.currentTimeMillis() + lockMillis)) {
            redisTemplate.expire(key, lockMillis + 3000, TimeUnit.MILLISECONDS);
            return true;
        }

        String lockExpireTime = String.valueOf(redisTemplate.opsForValue().get(key));
        if (StringUtils.isBlank(lockExpireTime) || "null".equals(lockExpireTime)) { //key被清理，锁被释放
            LOGGER.warn("{}获取锁失败，可以被清理或锁被释放", key);
            return false;
        }
        if (Long.parseLong(lockExpireTime) > System.currentTimeMillis()) { // 锁未过期
            LOGGER.warn("{}获取锁失败，锁未过期", key);
            return false;
        }

        String nowLowExpireTime = String.valueOf(redisTemplate.opsForValue().getAndSet(key, System.currentTimeMillis() + lockMillis));
        if (StringUtils.isBlank(nowLowExpireTime) || "null".equals(nowLowExpireTime)) {  // key不存在，已被清理，拿锁成功
            redisTemplate.expire(key, lockMillis + 3000, TimeUnit.MILLISECONDS);
            return true;
        } else if (lockExpireTime.equals(nowLowExpireTime)) {
            redisTemplate.expire(key, lockMillis + 3000, TimeUnit.MILLISECONDS);
            return true;
        }

        LOGGER.warn("{}获取锁失败", key);

        return false;
    }

    /**
     * 默认重试间隔时间为1s
     *
     * @param key
     * @return
     */
    @Override
    public boolean tryLock(String key) {
        return tryLockWithLockTime(key, defaultLockTimeMillis, defaultTryLockIntervalMillis);
    }

    /**
     * @param key
     * @param intervalMillis
     * @return
     */
    @Override
    public boolean tryLock(String key, long intervalMillis) {
        return tryLockWithLockTime(key, defaultLockTimeMillis, intervalMillis);
    }

    @Override
    public boolean tryLockWithLockTime(String key, long lockMillis) {
        return tryLockWithLockTime(key, lockMillis, defaultTryLockIntervalMillis);
    }

    /**
     * 加锁失败重试3次，重试间隔时间为：次数*intervalMillis
     *
     * @param key
     * @param lockMillis
     * @param intervalMillis
     * @return
     */
    @Override
    public boolean tryLockWithLockTime(String key, long lockMillis, long intervalMillis) {
        Assert.isTrue(intervalMillis > 0);
        Assert.isTrue(lockMillis > 0);

        int tryTimes = 0;
        while (tryTimes < 3) {
            boolean succeed = this.lock(key, lockMillis);
            if (succeed) {
                return succeed;
            }
            tryTimes++;
            LOGGER.debug("tryLock try:{},key:{}", tryTimes, key);
            try {
                Thread.sleep(intervalMillis * tryTimes);
            } catch (InterruptedException e) {
                LOGGER.warn("key:{} try lock fail", key, e);
            }
        }
        return false;
    }

    @Override
    public void unLock(String key) {
        // 10毫秒后过期
        redisTemplate.expire(key, 0, TimeUnit.MILLISECONDS);
    }
}
