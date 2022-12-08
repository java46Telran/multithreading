package telran.multithreading;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class NumberGroups {
	private static final int DEFAULT_N_THREADS = 4;
	private int [][] groups;
	private int nThreads = DEFAULT_N_THREADS;
	public NumberGroups(int[][] groups) {
		super();
		this.groups = groups;
	}
	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}
	public long computeSum() {
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		List<Future<Long>> groupSums =
				Arrays.stream(groups).map(OneGroupSum::new)
				.map(executor::submit).toList();
		return groupSums.stream().mapToLong(value -> {
			try {
				return value.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new IllegalStateException();
			}
		}).sum();
	}
	
	
	
}
