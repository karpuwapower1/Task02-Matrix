package by.training.karpilovich.task02.service;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;

public class Service implements Callable<Integer> {

	private Matrix matrix = Matrix.getInstance();
	private Random random = new Random();
	private CyclicBarrier barrier;
	private int diagonalIndex;
	private boolean isRow;
	private int index;
	private int value;

	private static final Logger LOGGER = LogManager.getLogger(Service.class);

	public Service(CyclicBarrier barrier, int value) {
		this.barrier = barrier;
		this.value = value;
	}

	public Integer call() {
		changeDiagonalElement();
		changeNonDiagonalElement();
		try {
			barrier.await();
			LOGGER.debug("await barrier");
		} catch (BrokenBarrierException e) {
			LOGGER.error("BrokenBarrierException " + e);
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException " + e);
		}
		LOGGER.debug("barrier is cancelled");
		releaseElementLock(matrix.getElement(diagonalIndex, diagonalIndex));
		LOGGER.debug("diag is released");
		releaseElementLock(isRow ? matrix.getElement(diagonalIndex, index) : matrix.getElement(index, diagonalIndex));
		LOGGER.debug("el is released");
		int sum = countRowSum();
		sum += countColumnSum();
		sum -= matrix.getElement(diagonalIndex, diagonalIndex).getValue();
		return Integer.valueOf(sum);
	}

	private void changeDiagonalElement() {
		int index = 0;
		while (!(isChanged(index, index))) {
			index = (index == matrix.length() - 1) ? 0 : index + 1;
			LOGGER.debug("index " + index);
		}
		diagonalIndex = index;
		LOGGER.debug("set diagonal index " + index);
	}

	private void changeNonDiagonalElement() {
		boolean change = false;
		while (!change) {
			index = random.nextInt(matrix.length());
			if (index == diagonalIndex) {
				continue;
			}
			isRow = random.nextBoolean();

			change = (isRow) ? isChanged(diagonalIndex, index) : isChanged(index, diagonalIndex);
			LOGGER.debug("change = " + change + " isRow= " + isRow
					+ (isRow ? (diagonalIndex + ", " + index) : (index + ", " + diagonalIndex)));
		}
	}

	private boolean isChanged(int i, int j) {
		return (matrix.getElement(i, j).changeElement(value));
	}

	private void releaseElementLock(Element element) {
		element.unlock();
	}

	private int countRowSum() {
		int sum = 0;
		for (int i = 0; i < matrix.length(); i++) {
			sum += matrix.getElement(diagonalIndex, i).getValue();
		}
		return sum;
	}

	private int countColumnSum() {
		int sum = 0;
		for (int i = 0; i < matrix.length(); i++) {
			sum += matrix.getElement(i, diagonalIndex).getValue();
		}
		return sum;
	}
}
