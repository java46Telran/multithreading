package telran.multithreading.executers;

import java.util.concurrent.BlockingQueue;

public class ServerImitator extends Thread {
BlockingQueue<Request> queue;

public ServerImitator(BlockingQueue<Request> queue) {
	super();
	this.queue = queue;
}
@Override
public void run() {
	while(true) {
		try {
			Request request = queue.take();
			Thread thread = new Thread(request);
			thread.start();
		} catch (InterruptedException e) {
			processRequests();
		}
	}
}
private void processRequests() {
	Request request = null;
	while((request = queue.poll())!=null) {
		Thread thread = new Thread(request);
		thread.start();
	}
	
}
}
