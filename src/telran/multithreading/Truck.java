package telran.multithreading;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

public class Truck extends Thread{
	private int load;
	private int nLoads;
	private static long elevator1;
	private static long elevator2;
	private static final Lock mutex1 = new ReentrantLock();
	private static final Lock mutex2 = /*new ReentrantLock(); //*/mutex1;
	private static AtomicInteger waitingCounter = new AtomicInteger(0);
	public Truck(int load, int nLoads) {
		this.load = load;
		this.nLoads = nLoads;
	}
	@Override
	public void run() {
		for (int i = 0; i < nLoads; i++) {
			loadElevator1(load);
			loadElevator2(load);
		}
	}
	 static private void loadElevator2(int load) {
		 waitingForUnlock(mutex2); 
			{
				elevator2 += load;
			}
		mutex2.unlock();
		
	}
	private static void waitingForUnlock(Lock mutex) {
		while (!mutex.tryLock()) {
			waitingCounter.incrementAndGet();
		}
	}
	static private void loadElevator1(int load) {
		 waitingForUnlock(mutex1); 
			{
				elevator1 += load;
			}
		mutex1.unlock();
		
	}
	public static long getElevator1() {
		return elevator1;
	}
	public static long getElevator2() {
		return elevator2;
	}
	public static int getWaitingCounter() {
		return waitingCounter.get();
	}
}
