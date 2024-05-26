package com.example.system.util;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private final ValueOperations<String, Object> valueOperations;

    private final ListOperations<String, Object> listOperations;

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.listOperations = redisTemplate.opsForList();
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value) {
        valueOperations.set(key, value);
    }

    public void setWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        valueOperations.set(key, value, timeout, unit);
    }

    public Object get(String key) {
        return valueOperations.get(key);
    }

    // 向列表左侧添加元素
    public void addToListLeft(String key, Object value) {
        listOperations.leftPush(key, value);
    }

    // 向列表右侧添加元素
    public void addToListRight(String key, Object value) {
        listOperations.rightPush(key, value);
    }

    public void addAllToListRight(String key, Collection<Object> collection) {
        listOperations.rightPushAll(key, collection);
    }

    // 获取列表指定范围的元素
    public List<Object> getListRange(String key, long start, long end) {
        return listOperations.range(key, start, end);
    }

    // 获取列表长度
    public Long getListSize(String key) {
        return listOperations.size(key);
    }

    // 移除并返回列表第一个元素
    public Object removeFromListLeft(String key) {
        return listOperations.leftPop(key);
    }

    // 移除并返回列表最后一个元素
    public Object removeFromListRight(String key) {
        return listOperations.rightPop(key);
    }

    public void deleteOne(String key) {
        redisTemplate.delete(key);
    }

    public void deleteList(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁的键名
     * @param requestId  请求标识，用于识别加锁的客户端
     * @param expireTime 锁的过期时间（秒）
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS));
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁的键名
     * @param requestId 请求标识，用于识别加锁的客户端
     * @return 是否成功释放锁
     */
    public boolean releaseLock(String lockKey, String requestId) {
        // Lua脚本保证原子性
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = stringRedisTemplate.execute((RedisCallback<Long>) (connection) -> connection.eval(luaScript.getBytes(), ReturnType.INTEGER, 1,
                lockKey.getBytes(), requestId.getBytes()));
        return result != null && result == 1;
    }

    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取前缀带有pattern的所有key
     * @param pattern 前缀
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern.concat("*"));
    }

    public void executePipelined(SessionCallback<Object> sessionCallback) {
        redisTemplate.executePipelined(sessionCallback);
    }

    public DataType type(String key) {
        return redisTemplate.type(key);
    }

}
