package com.nx.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Neal on 2014/11/17.
 */
public class JedisTemplate<T> {

    private final JedisPool jedisPool;

    public JedisTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public T execute(JedisHandle<T> handle) {
        Jedis jedis = getNewJedis();
        T result = handle.execute(jedis);
        jedisPool.returnResource(jedis);
        return result;
    }

    public Jedis getNewJedis() {
        return jedisPool.getResource();
    }
}
