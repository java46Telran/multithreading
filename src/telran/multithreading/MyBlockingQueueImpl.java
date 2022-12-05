package telran.multithreading;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueueImpl<E> implements BlockingQueue<E> {
	private List<E> queue = new LinkedList<>();
	private int capacity;
	private Lock monitor = new ReentrantLock();
	private Condition consumerWaitingCondition = monitor.newCondition();
	private Condition producerWaitingCondition = monitor.newCondition();

//TODO additional fields consider Lock, Condition 
	public MyBlockingQueueImpl(int capacity) {
		this.capacity = capacity;
	}

	public MyBlockingQueueImpl() {
		this(Integer.MAX_VALUE);
	}

	@Override
	public E remove() {
		try {
			monitor.lock();
			if (queue.size() == 0) {
				throw new NoSuchElementException();
			}
			E result = queue.remove(0);
			producerWaitingCondition.signal();
			return result;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E poll() {
		E result = null;
		try {
			monitor.lock();
			if (queue.size() != 0) {
				result = queue.remove(0);
				producerWaitingCondition.signal();
			}

			return result;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E element() {
		try {
			monitor.lock();
			if (queue.size() == 0) {
				throw new NoSuchElementException();
			}
			return queue.get(0);
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E peek() {
		E result = null;
		try {
			monitor.lock();
			if (queue.size() != 0) {
				result = queue.get(0);
				producerWaitingCondition.signal();
			}

			return result;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public int size() {
		try {
			monitor.lock();

			return queue.size();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		try {
			monitor.lock();
			return queue.isEmpty();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public Iterator<E> iterator() {
		try {
			monitor.lock();
			return queue.iterator();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public Object[] toArray() {
		try {
			monitor.lock();
			return queue.toArray();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public <T> T[] toArray(T[] a) {
		try {
			monitor.lock();
			return queue.toArray(a);
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		try {
			monitor.lock();
			return queue.containsAll(c);
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		int size = size();
		c.forEach(this::add);
		return size != size();
	}

	@Override
	public boolean removeAll(Collection<?> c) {

		try {
			monitor.lock();
			boolean res = queue.removeAll(c);
			if (res) {
				producerWaitingCondition.signal();
			}
			return res;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		try {
			monitor.lock();
			boolean res = queue.retainAll(c);
			if (res) {
				producerWaitingCondition.signal();
			}
			return res;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public void clear() {
		try {
			monitor.lock();
			queue.clear();
			producerWaitingCondition.signal();
		} finally {
			monitor.unlock();
		}

	}

	@Override
	public boolean add(E e) {
		try {
			monitor.lock();
			if (queue.size() == capacity) {
				throw new IllegalStateException();
			}
			boolean res = queue.add(e);
			consumerWaitingCondition.signal();
			return res;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean offer(E e) {
		boolean res = true;
		try {
			monitor.lock();
			if (queue.size() == capacity) {
				res = false;
			} else {
				queue.add(e);
				consumerWaitingCondition.signal();
			}

			return res;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public void put(E e) throws InterruptedException {
		try {
			monitor.lock();
			while (queue.size() == capacity) {
				producerWaitingCondition.await();
			}
			queue.add(e);
			consumerWaitingCondition.signal();

		} finally {
			monitor.unlock();
		}


	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		try {
			monitor.lock();
			while (queue.size() == capacity) {
				if (!producerWaitingCondition.await(timeout, unit)) {
					return false;
				}
			}
			queue.add(e);
			consumerWaitingCondition.signal();
			return true;

		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E take() throws InterruptedException {
		try {
			monitor.lock();
			while (queue.isEmpty()) {
				consumerWaitingCondition.await();
			}
			E res = queue.remove(0);
			producerWaitingCondition.signal();
			return res;

		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		try {
			monitor.lock();
			while (queue.isEmpty()) {
				if (!consumerWaitingCondition.await(timeout, unit)) {
					return null;
				}
			}
			E res = queue.remove(0);
			if (res != null) {
				producerWaitingCondition.signal();
			}
			
			return res;

		} finally {
			monitor.unlock();
		}
	}

	@Override
	public int remainingCapacity() {
		try {
			monitor.lock();
			return capacity - queue.size();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean remove(Object o) {
		// No implement
		return false;
	}

	@Override
	public boolean contains(Object o) {
		try {
			monitor.lock();
			return queue.contains(o);
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public int drainTo(Collection<? super E> c) {
		// No implement
		return 0;
	}

	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		// No implement
		return 0;
	}
}