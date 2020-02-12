package by.training.karpilovich.task02.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;

public class MatrixChangerService implements Callable<Integer> {

	private Matrix matrix = Matrix.getInstance();
	List<Element> locked = new ArrayList<>();
	private int diagonalIndex = 0;
	private CyclicBarrier barrier;
	private int name;

	private static final Logger LOGGER = LogManager.getLogger(MatrixChangerService.class);

	public MatrixChangerService(CyclicBarrier barrier, int name) {
		this.barrier = barrier;
		this.name = name;
	}

	@Override
	public Integer call() {
		int sum = 0;
		try {
			Thread.currentThread().setName(String.valueOf(name));
			changeDiagonalElement();
			changeNonDiagonalElement();
			LOGGER.debug("await barrier");
			barrier.await();
			LOGGER.debug("barrier is cancelled");
			releaseElementLock();
			LOGGER.debug("elements is released");
			sum = countSum();
		} catch (BrokenBarrierException | InterruptedException e) {
			releaseElementLock();
			LOGGER.error(e.getClass().getSimpleName());
		}
		return Integer.valueOf(sum);
	}

	private void changeDiagonalElement() {
		while (!(isChanged(diagonalIndex, diagonalIndex))) {
			diagonalIndex = (diagonalIndex == matrix.getLength() - 1) ? 0 : diagonalIndex + 1;
			LOGGER.debug("index " + diagonalIndex);
		}
		LOGGER.debug("set diagonal index " + diagonalIndex);
	}

	private void changeNonDiagonalElement() {
		Random random = new Random();
		boolean change = false;
		int index;
		boolean isRow;
		while (!change) {
			if ((index = random.nextInt(matrix.getLength())) != diagonalIndex) {
				change = (isRow = random.nextBoolean()) ? isChanged(diagonalIndex, index)
						: isChanged(index, diagonalIndex);

				LOGGER.debug("change = " + change + " isRow= " + isRow
						+ (isRow ? (diagonalIndex + ", " + index) : (index + ", " + diagonalIndex)));
			}
		}
	}

	private boolean isChanged(int i, int j) {
		Element element = matrix.getElement(i, j);
		if (element.changeElement()) {
			return locked.add(element);
		}
		return false;
	}

	private void releaseElementLock() {
		for (Element element : locked) {
			element.unlock();
		}
	}

	private int countSum() {
		int sum = countRowSum();
		sum += countColumnSum();
		return sum - matrix.getElement(diagonalIndex, diagonalIndex).getValue();
	}

	private int countRowSum() {
		int sum = 0;
		for (int i = 0; i < matrix.getLength(); i++) {
			sum += matrix.getElement(diagonalIndex, i).getValue();
		}
		return sum;
	}

	private int countColumnSum() {
		int sum = 0;
		for (int i = 0; i < matrix.getLength(); i++) {
			sum += matrix.getElement(i, diagonalIndex).getValue();
		}
		return sum;
	}

}
