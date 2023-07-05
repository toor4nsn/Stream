package com.example.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class RedisHandleService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 使用Redission库提供的分布式锁方法
     * 进行尝试加锁操作
     * 能保证操作的原子性
     * @param lockKey 锁的key值
     * @param waitTime 等待锁的时间，单位毫秒
     * @param expire 锁自动过期的时间，单位毫秒
     *
     * @return 是否成功获得锁
     */
    public boolean tryRedissionLockInMillis(String lockKey,long waitTime,long expire) {

        RLock lock = redissonClient.getLock(lockKey);
        try {
            // Redission加锁的三个参数分别是
            // - 尝试加锁等待时间
            // - 锁失效时间
            // - 锁时间单位
            return lock.tryLock(waitTime, expire, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("tryRedissionLock error:{}", e.getMessage(), e);
            lock.unlock();
        }
        return false;
    }

    /**
     * 使用Redission库提供的分布式锁方法
     * 进行解锁操作
     * 能保证操作的原子性
     */
    public void unlockRedissionLock (String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.unlock();
        } catch (Exception e) {
            log.error("unlockRedissionLock error:{}", e.getMessage(), e);
        }
    }

    /**
     * 使用Redission库提供的分布式限流器
     */
    public boolean tryRRateLimiterAcquire(String limiterName, Integer qps) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiterName);
        // 设置限速器为 “全局” 每 “1” “秒” 速率为 “qps”

        // 设置速率，1秒中产生qps个令牌
        rateLimiter.trySetRate(RateType.OVERALL, qps, 1, RateIntervalUnit.SECONDS);
        return rateLimiter.tryAcquire();
    }








    //=============================common============================

    public void rename(String oldKey, String newKey) {
        try {
            redisTemplate.rename(oldKey, newKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取分布式锁，原子操作
     * @param lockKey
     * @param expire
     * @return
     */
    public boolean tryLock(String lockKey, long expire) {
        try{
            RedisCallback<Boolean> callback = (connection) -> {
                long now = System.currentTimeMillis();
                long expireTime = now + expire * 1000 + 1;
                byte[] key = lockKey.getBytes(Charset.forName("UTF-8"));
                byte[] expireTimeByte = String.valueOf(expireTime).getBytes(Charset.forName("UTF-8"));

                boolean lock = connection.setNX(key,expireTimeByte);
                if(lock){
                    return true;
                }
                if(now > Long.parseLong(new String(connection.get(key))) && now > Long.parseLong(new String(connection.getSet(key, expireTimeByte)))){
                    connection.expire(key, expire);
                    return true;
                }
                return false;
            };
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey
     * @return
     */
    public boolean releaseLock(String lockKey) {
        RedisCallback<Boolean> callback = (connection) -> {
            return connection.del(lockKey.getBytes(Charset.forName("UTF-8")))==1;
        };
        return (Boolean)redisTemplate.execute(callback);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            return expire(key, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 失效时间
     * @param timeUnit 失效时间类型
     * @return
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public  String get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key,  String value) {
        try {
            return set(key, value, 0);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,  String value, long time) {
        try {
            return set(key, value, time, TimeUnit.SECONDS);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间 time要大于0 如果time小于等于0 将设置无限期
     * @param timeUnit  时间类型
     * @return true成功 false 失败
     */
    public boolean set(String key,  String value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param by  要增加几(大于0)
     * @return
     */
    public long incr(String key, long by) {
        if (by < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, by);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 递增
     * @param key
     * @param by
     * @param expireTime
     */
    public long incr(String key, long by, long expireTime) {
        try {
            long v = incr(key, by);
            if(expireTime > 0){
                expire(key, expireTime);
            }
            return v;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 递减
     *
     * @param key 键
     * @param by  要减少几(小于0)
     * @return
     */
    public long decr(String key, long by) {
        if (by < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, -by);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取当前key剩余的超时时间
     * @param key
     * @return
     */
    public long ttl(String key) {
        long result = 0;
        try {
            result = redisTemplate.getExpire(key);
        } catch (Exception e) {
            log.error("redis zCard err: {}", e.getMessage(), e);
        }finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String,  String> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, String> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间
     * @param timeUnit 时间类型
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String,  String> map, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item,  String value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item,  String value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @param timeUnit 时间类型
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item,  String value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key,  String... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取哈希表中字段的数量
     * @param key
     * @return
     */
    public long hLen(String key){
        try {
            return redisTemplate.opsForHash().size(key);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, -by);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set< String> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key,  String value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key,  String... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time,  String... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间
     * @param timeUnit   时间类型
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, TimeUnit timeUnit,  String... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key,  String... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
//            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    //===============================zset=================================
    /**
     * 批量添加到sorted set队列，字符串类型
     *
     */
    public void zAdd(String sortKey, String member, double score) {
        try {
            redisTemplate.opsForZSet().add(sortKey, member, score);
        } catch (Exception e) {
            log.error("redis zAdd err:{} ", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 批量添加到sorted set队列，字符串类型
     *
     */
    public void zBatchAdd(String sortKey, Set<ZSetOperations.TypedTuple<String>> scoreMembers) {
        try {
            redisTemplate.opsForZSet().add(sortKey, scoreMembers);
        } catch (Exception e) {
            log.error("redis zBatchAdd err:{} ", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 有序集合中对指定成员的分数加上增量 increment
     *
     */
    public  double incrementScore(String sortKey, String member, double score) {
        Double result = 0.0;

        try {
            result = redisTemplate.opsForZSet().incrementScore(sortKey, member, score);
        } catch (Exception e) {
            log.error("redis incrementScore err:{} ", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }

        return result;
    }

    /**
     * 从sorted set中获取一定范围的段，按score从高到低
     *
     */
    public  Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String sortKey, long start, long end) {
        Set<ZSetOperations.TypedTuple<String>> result = null;
        try {
            result = redisTemplate.opsForZSet().reverseRangeWithScores(sortKey, start, end);
        } catch (Exception e) {
            log.error("redis getSortedSetRangeReverse err:{}", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 从sorted set中获取一定范围的段，按score从低到高
     *
     */
    public  Set<ZSetOperations.TypedTuple<String>> rangeWithScores(String sortKey, long start, long end) {
        Set<ZSetOperations.TypedTuple<String>> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeWithScores(sortKey, start, end);
        } catch (Exception e) {
            log.error("redis getSortedSetRangeReverse err: {}", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 从sorted set中获取一定范围的段，按score区间范围获取
     *
     */
    public  Set<String> rangeByScore(String sortKey, double min, double max) {
        Set<String> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScore(sortKey, min, max);
        } catch (Exception e) {
            log.error("redis getSortedSetRangeReverse err: {}", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }


    /**
     * 从sorted set中获取一定范围的段，按score区间范围, 从给定下标和给定长度获取最终值
     *
     */
    public  Set<String> rangeByScore(String sortKey, double min, double max, long offset, long count) {
        Set<String> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScore(sortKey, min, max, offset, count);
        } catch (Exception e) {
            log.error("redis getSortedSetRangeReverse err: {}", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }


    /**
     * 获取有序集合member数量
     * @param key
     * @return
     */
    public long zCard(String key) {
        long result = 0;
        try {
            result = redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            log.error("redis zCard err: {}", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * sorted set中移除元素
     *
     */
    public long zRemove(String key, String... members) {
        long result = 0;
        try {
            result = redisTemplate.opsForZSet().remove(key, members);
        } catch (Exception e) {
            log.error("redis zRemove err: {}", e.getMessage(), e);
        }finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List< String> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public  String lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key,  String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key,  String value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @param timeUnit  时间类型
     * @return
     */
    public boolean lSet(String key,  String value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List< String> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List< String> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @param timeUnit  时间类型
     * @return
     */
    public boolean lSet(String key, List< String> value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index,  String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count,  String value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            //释放连接
            //RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

/************************************************bit****************************************************/
    /**
     * key键对应的值value对应的ascii码,在offset的位置(从左向右数)变为value。
     * 从第0位开始, 从左往右
     * @param key
     * @param offset
     * @return
     */
    public boolean setBit(String key, long offset, boolean value){

        try {
            redisTemplate.opsForValue().setBit(key, offset, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 判断指定bit位的值是  true=1; false=0
     * @param key
     * @param offset
     * @return
     */
    public boolean getBit(String key, long offset){

        try {
            return redisTemplate.opsForValue().getBit(key, offset);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 计算bit位为1的总数
     * @param key
     * @return
     */
    public long bitCount(final String key) {
        try {
            return (Long)redisTemplate.execute((connection) -> {
                return connection.bitCount(key.getBytes());
            }, true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }




}