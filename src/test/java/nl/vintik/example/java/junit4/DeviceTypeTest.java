package nl.vintik.example.java.junit4;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeviceTypeTest {

    @Test
    public void shouldIndicateSEODeviceTypeWhenSeo() {
        assertTrue("Expected SEO is Seo", DeviceType.SEO.isSeo());
        assertTrue("Expected SEO MOBILE is Seo", DeviceType.SEO_MOBILE.isSeo());
    }

    @Test
    public void shouldIndicateNotSEODeviceTypeWhenDesktop() {
        assertFalse("Expected SEO is Seo", DeviceType.DESKTOP.isSeo());
    }
}