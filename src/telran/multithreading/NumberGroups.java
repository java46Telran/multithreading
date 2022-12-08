package telran.multithreading;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
		List<OneGroupSum> groupSums = Arrays.stream(groups)
				.map(group -> new OneGroupSum(group)).toList();
		startGroups(groupSums, executor);
		waitingGroups(executor);
		return groupSums.stream().mapToLong(OneGroupSum::getRes).sum();
	}
	private void startGroups(List<OneGroupSum> groupSums, ExecutorService executor) {
		groupSums.forEach(executor::execute);
		
	}
	private void waitingGroups(ExecutorService executor){
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			
		}
	}
	
	
}
