package tqs.peticket.display;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tqs.peticket.display.cache.Cache;

public class CacheTest {

    private Cache cache;
    private Logger mockLogger;

    @BeforeEach
    public void setUp() throws Exception {
        cache = new Cache();
        mockLogger = mock(Logger.class);
        setMockLogger(cache, mockLogger);
    }

    private void setMockLogger(Cache cache, Logger mockLogger) throws Exception {
        Field loggerField = Cache.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(cache, mockLogger);
    }

    @Test
    public void testIncrementHits() {
        int initialHits = cache.getHits();
        cache.incrementHits();
        assertEquals(initialHits + 1, cache.getHits());
    }

    @Test
    public void testIncrementMisses() {
        int initialMisses = cache.getMisses();
        cache.incrementMisses();
        assertEquals(initialMisses + 1, cache.getMisses());
    }

    @Test
    public void testIncrementRequests() {
        int initialRequests = cache.getRequests();
        cache.incrementRequests();
        assertEquals(initialRequests + 1, cache.getRequests());
    }

    @Test
    public void testAddToCache() {
        cache.addToCache("testKey", 123);
        assertTrue(cache.containsItem("testKey"));
        assertEquals(123, cache.getFromCache("testKey"));
    }

    @Test
    public void testGetFromCacheHit() {
        cache.addToCache("testKey", 123);
        Integer value = cache.getFromCache("testKey");
        assertNotNull(value);
        assertEquals(123, value);
        verify(mockLogger).info("Retrieved {} from cache: +1 hit +1 request", "testKey");
        assertEquals(1, cache.getHits());
        assertEquals(1, cache.getRequests());
    }

    @Test
    public void testGetFromCacheMiss() {
        Integer value = cache.getFromCache("nonExistentKey");
        assertNull(value);
        verify(mockLogger).info("{} not found in cache: +1 miss +1 request", "nonExistentKey");
        assertEquals(1, cache.getMisses());
        assertEquals(1, cache.getRequests());
    }

    @Test
    public void testGetCacheSize() {
        cache.addToCache("testKey1", 123);
        cache.addToCache("testKey2", 456);
        assertEquals(2, cache.getCacheSize());
    }

    @Test
    public void testClearCache() {
        cache.addToCache("testKey", 123);
        cache.clearCache();
        assertEquals(0, cache.getCacheSize());
        assertFalse(cache.containsItem("testKey"));
    }

    @Test
    public void testContainsItem() {
        cache.addToCache("testKey", 123);
        assertTrue(cache.containsItem("testKey"));
        assertFalse(cache.containsItem("nonExistentKey"));
    }
}
