package telran.multithreading;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MessageBox {
	BlockingQueue<String> queue = new LinkedBlockingQueue<>(1000); //FIXME replace with MyBlockingQueueImpl
	public void put(String message) throws InterruptedException {
		queue.put(message);
	}

	public String get() throws InterruptedException {
		return queue.take();

	}
	public String take() {
	
		return queue.poll();
		
	}

}
