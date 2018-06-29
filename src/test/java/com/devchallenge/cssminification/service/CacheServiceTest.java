package com.devchallenge.cssminification.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CacheServiceTest {

    @InjectMocks
    private CacheService cacheService = new CacheService();

    @Mock
    private CacheManager cacheManager;

    @Test
    public void removeAllCache() {
        //setup
        String cacheName = "cacheName";
        Cache mockedCache = Mockito.mock(Cache.class);
        when(cacheManager.getCache(cacheName)).thenReturn(mockedCache);

        //execute
        cacheService.removeCache(cacheName);

        //verify
        verify(mockedCache, times(1)).clear();
        verify(mockedCache, times(0)).evict(any(String.class));
    }

    @Test
    public void removeCertainKey() {
        //setup
        String cacheName = "cacheName";
        String key = "key";
        Cache mockedCache = Mockito.mock(Cache.class);
        when(cacheManager.getCache(cacheName)).thenReturn(mockedCache);

        //execute
        cacheService.removeCache(cacheName, key);

        //verify
        verify(mockedCache, times(0)).clear();
        verify(mockedCache, times(1)).evict(key);
    }

    @Test
    public void testArgument() {

        Cache mockedCache = Mockito.mock(Cache.class);
        Hey hey = new Hey("hello!");
        Cache.ValueWrapper result = new SimpleValueWrapper(null);
        when(mockedCache.get(refEq(hey))).thenReturn(result);

        Cache.ValueWrapper vw = mockedCache.get(new Hey("hello!"));

        System.out.println("xz");

    }

    private static class Hey {

        private String v;


        @java.beans.ConstructorProperties({"v"})
        public Hey(String v) {
            this.v = v;
        }

        public String getV() {
            return this.v;
        }

        public void setV(String v) {
            this.v = v;
        }

       /* public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Hey)) return false;
            final Hey other = (Hey) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$v = this.getV();
            final Object other$v = other.getV();
            if (this$v == null ? other$v != null : !this$v.equals(other$v)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $v = this.getV();
            result = result * PRIME + ($v == null ? 43 : $v.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Hey;
        }

        public String toString() {
            return "CacheServiceTest.Hey(v=" + this.getV() + ")";
        }*/
    }
}