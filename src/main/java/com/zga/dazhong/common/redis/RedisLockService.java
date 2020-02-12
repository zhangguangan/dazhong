package com.zga.dazhong.common.redis;

public interface RedisLockService {
    public boolean lock(String key);

    public boolean lock(String key, long lockMillis);

    public boolean tryLock(String key);

    public boolean tryLockWithLockTime(String key, long lockMillis);

    public boolean tryLock(String key, long intervalMillis);

    public boolean tryLockWithLockTime(String key, long lockMillis, long intervalMillis);

    public void unLock(String key);
}