package by.training.karpilovich.task02.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class Element {

	private int value;
	private Lock lock = new ReentrantLock();

	public Element(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean changeElement() {
		if (lock.tryLock()) {
			int value = Integer.parseInt(Thread.currentThread().getName());
			this.value = value;
			return true;
		}
		return false;
	}

	public void unlock() {
		lock.unlock();
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		return value == other.value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [value=" + value + "]";
	}
}
