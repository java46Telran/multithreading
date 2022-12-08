package telran.multithreading.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import telran.multithreading.NumberGroups;

class NumberGroupsTest {
private static final int N_GROUPS = 100000;
private static final int N_NUMBERS_IN_GROUP = 100;
int [][] groupsPerformance = getGroups(N_GROUPS, N_NUMBERS_IN_GROUP);
	@Test
	void functionalTest() {
		int[][] groups = {
				{1, 2, 3},
				{4, 5, 6}
		};
		assertEquals(21, new NumberGroups(groups).computeSum());
		
	}
	@Test
	void performanceTest () {
		NumberGroups numberGroups = new NumberGroups(groupsPerformance);
		numberGroups.setnThreads(15000);
		numberGroups.computeSum();
	}
	int[][] getGroups(int nGroups, int nNumbersInGroup) {
		return Stream.generate(() -> getGroup(nNumbersInGroup))
				.limit(nGroups).toArray(int[][]::new);
	}
	private int [] getGroup(int nNumbers) {
		return Stream.generate(() -> ThreadLocalRandom.current().nextInt())
				.limit(nNumbers).mapToInt(x->x).toArray();
	}

}
