package nl.vintik.example.java.junit4.parametarized;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * Parameterised test applies tests to  all defined features in Feature class
 */
@RunWith(Parameterized.class)
public class FeatureTest {

    private final Toggle toggle;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Toggle> data() {
        return Arrays.asList(Feature.values());
    }

    public FeatureTest(Toggle toggle) {
        this.toggle = toggle;
    }

    @Test
    public void shouldContainKeyswithLengthBetween5And35Inclusive() {
        assertTrue(toggle.name().length() >= 5);
        assertTrue(toggle.name().length() <= 35);
    }
}
