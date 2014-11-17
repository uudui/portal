package com.nx.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by Neal on 2014/11/17.
 */
public interface JedisHandle<T> {
    public T execute(Jedis jedis);
}
