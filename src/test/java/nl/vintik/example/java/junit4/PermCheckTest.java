package nl.vintik.example.java.junit4;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PermCheckTest {

	@Test
	public void shouldIndicateArrayIsPermutation() {
		int[] testedArray = {4 , 1 , 3 , 2};
		assertEquals(1, PermCheck.solution(testedArray));
	}
	
	@Test
	public void shouldIndicateArrayIsNotPermutation() {
		int[] testedArray = {4 , 1 , 3 , 2, 6};
		assertEquals(0, PermCheck.solution(testedArray));
	}

}
