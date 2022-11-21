package telran.multithreading.games;

import java.time.Instant;

public class Runner extends Thread {
	private Race race;
	private int runnerId;
	private Instant finishTime;
	public int getRunnerId() {
		return runnerId;
	}
	public Runner(Race race, int runnerId) {
		this.race = race;
		this.runnerId = runnerId;
	}
	@Override
	public void run() {
		int sleepRange = race.getMaxSleep() - race.getMinSleep() + 1;
		int minSleep = race.getMinSleep();
		int distance = race.getDistance();
		for (int i = 0; i < distance; i++) {
			try {
				sleep((long) (minSleep + Math.random() * sleepRange));
			} catch (InterruptedException e) {
				
			}
			System.out.println(runnerId);
		}
		
		synchronized (race) {
			finishTime = Instant.now();
			finishRace();
		}
	}
	private void finishRace() {
		race.getResultsTable().add(this);

	}
	public Instant getFinsishTime() {
		return finishTime;
	}
}
