package com.nx.core.cache;

import net.sf.ehcache.Ehcache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Neal on 10/20 020.
 */
public class SpringCacheManagerWrapper implements CacheManager {
    final static Logger log = LoggerFactory.getLogger(SpringCacheManagerWrapper.class);
    private org.springframework.cache.CacheManager cacheManager;

    /**
     * 设置spring cache manager
     *
     * @param cacheManager
     */
    public SpringCacheManagerWrapper(org.springframework.cache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        org.springframework.cache.Cache springCache = cacheManager.getCache(name);
        return new SpringCacheWrapper<>(springCache);
    }

    static class SpringCacheWrapper<K, V> implements Cache<K, V> {
        private org.springframework.cache.Cache springCache;

        SpringCacheWrapper(org.springframework.cache.Cache springCache) {
            this.springCache = springCache;
        }

        @Override
        public V get(K key) throws CacheException {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Getting object from cache [" + springCache.getName() + "] for key [" + key + "]");
                }
                if (key == null) {
                    return null;
                } else {
                    org.springframework.cache.Cache.ValueWrapper valueWrapper = springCache.get(key);
                    if (valueWrapper == null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Element for [" + key + "] is null.");
                        }
                        return null;
                    } else {
                        //noinspection unchecked
                        return (V) valueWrapper.get();
                    }
                }
            } catch (Throwable t) {
                log.error(t.getMessage());
                throw new CacheException(t);
            }
        }

        @Override
        public V put(K key, V value) throws CacheException {
            if (log.isDebugEnabled()) {
                log.debug("Putting object in cache [" + springCache.getName() + "] for key [" + key + "]");
            }
            try {
                V previous = get(key);
                springCache.put(key, value);
                return previous;
            } catch (Throwable t) {
                log.error(t.getMessage());
                throw new CacheException(t);
            }
        }

        @Override
        public V remove(K key) throws CacheException {
            if (log.isDebugEnabled()) {
                log.debug("Removing object from cache [" + springCache.getName() + "] for key [" + key + "]");
            }
            try {
                V previous = get(key);
                springCache.evict(key);
                return previous;
            } catch (Throwable t) {
                log.error(t.getMessage());
                throw new CacheException(t);
            }
        }

        @Override
        public void clear() throws CacheException {
            if (log.isDebugEnabled()) {
                log.debug("Clearing all objects from cache [" + springCache.getName() + "]");
            }
            try {
                springCache.clear();
            } catch (Throwable t) {
                log.error(t.getMessage());
                throw new CacheException(t);
            }
        }

        @Override
        public int size() {
            if (springCache.getNativeCache() instanceof Ehcache) {
                try {
                    return ((Ehcache) springCache.getNativeCache()).getSize();
                } catch (Throwable t) {
                    log.error(t.getMessage());
                    throw new CacheException(t);
                }
            }
            throw new UnsupportedOperationException("invoke spring cache abstract size method not supported");
        }

        @Override
        public Set<K> keys() {
            if (springCache.getNativeCache() instanceof Ehcache) {
                try {
                    List<K> keys = ((Ehcache) springCache.getNativeCache()).getKeys();
                    if (!CollectionUtils.isEmpty(keys)) {
                        return Collections.unmodifiableSet(new LinkedHashSet<>(keys));
                    } else {
                        return Collections.emptySet();
                    }
                } catch (Throwable t) {
                    log.error(t.getMessage());
                    throw new CacheException(t);
                }
            }
            throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");
        }

        @Override
        public Collection<V> values() {
            if (springCache.getNativeCache() instanceof Ehcache) {
                try {
                    List<K> keys = ((Ehcache) springCache.getNativeCache()).getKeys();
                    if (!CollectionUtils.isEmpty(keys)) {
                        List<V> values = new ArrayList<>(keys.size());
                        for (K key : keys) {
                            V value = get(key);
                            if (value != null) {
                                values.add(value);
                            }
                        }
                        return Collections.unmodifiableList(values);
                    } else {
                        return Collections.emptyList();
                    }
                } catch (Throwable t) {
                    log.error(t.getMessage());
                    throw new CacheException(t);
                }
            }
            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
        }
    }
}
