package nl.vintik.example.java.junit4.spring;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Simple spring example
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CacheConfiguration.class)
public class CacheConfigurationTest {

    @Autowired
    private List<Cache> caches;

    @Test
    public void shouldBootUpSpringConfig() {
        assertNotEquals(0, caches.size());
    }

    @Test
    public void shouldOverrideCacheConfiguration() {
        assertEquals(50_000, CacheConfiguration.size("maxElementsInMemory|50000", 200));
        assertEquals(200, CacheConfiguration.size("maxElementsInMemories|50000", 200));
    }
}