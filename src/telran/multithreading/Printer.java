package telran.multithreading;

public class Printer extends Thread {
	private static final long SLEEPING_TIME = 100;
	int printerNumber;
	Printer next;
	private int inLine;
	static int overall;
	static int nPartitions;

	public Printer(int printerNumber) {
		this.printerNumber = printerNumber;
		inLine = overall / nPartitions;
	}

	public void setNext(Printer next) {
		this.next = next;
	}

	public static void setOverall(int overall) {
		Printer.overall = overall;
	}

	public static void setPartitions(int nPartitions) {
		Printer.nPartitions = nPartitions;

	}

	@Override
	public void run() {
		int count = 0;
		String line = (" " + printerNumber).repeat(inLine);
		while (count < nPartitions) {
			try {
				sleep(SLEEPING_TIME);

			} catch (InterruptedException e) {
				System.out.println(line);
				next.interrupt();
				count++;
			}
		}

	}

}
