package nl.vintik.example.java.junit4;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RandomGeneratorTest {

    private RandomGenerator testedClass;

    @Before
    public void setup() {
        testedClass = new RandomGenerator();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpperBoundIsNotAPositiveNumber() {
        testedClass.getRandomInt(0);
    }

    @Test
    public void shouldGetRandomIntWhenUpperBoundIsValid() {
        final int upperBound = 2;
        assertTrue(testedClass.getRandomInt(upperBound) < upperBound);
    }
}